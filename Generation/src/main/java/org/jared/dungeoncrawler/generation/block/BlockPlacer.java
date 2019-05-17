package org.jared.dungeoncrawler.generation.block;

import com.google.common.collect.Queues;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.data.Tuple;
import org.jared.dungeoncrawler.api.generation.block.IBlockPlaceTask;
import org.jared.dungeoncrawler.api.generation.block.IBlockPlacer;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

public class BlockPlacer implements IBlockPlacer
{
    private final BlockingQueue<Queue<Tuple<Vector, MaterialData>>> BLOCK_QUEUE = Queues.newLinkedBlockingQueue();

    private BlockingQueue<IBlockPlaceTask> blockPlaceTasks = Queues.newLinkedBlockingQueue();

    private IBlockPlaceTask currentTask;

    public BlockPlacer() {}

    @Override
    public BlockingQueue<IBlockPlaceTask> getPlacingQueue()
    {
        return blockPlaceTasks;
    }

    @Override
    public void addTask(IBlockPlaceTask blockPlaceTask)
    {
        blockPlaceTasks.add(blockPlaceTask);
    }

    @Override
    public void run()
    {
        try
        {
            if (currentTask == null && !blockPlaceTasks.isEmpty())
            {
                DungeonCrawler.LOG.debug(1);
                currentTask = blockPlaceTasks.take();
                currentTask.run();
            }
            else
            {
                if (currentTask != null && currentTask.isDone())
                    currentTask = null;
            }

        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
