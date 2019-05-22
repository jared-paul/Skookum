package org.jared.dungeoncrawler.api.generation.block;

import com.google.common.collect.Lists;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.concurrency.Callback;
import org.jared.dungeoncrawler.api.data.Tuple;
import org.jared.dungeoncrawler.api.material.IMaterialAndData;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.api.util.VectorUtil;

import javax.annotation.Nullable;
import java.util.List;

public class BlockPlaceTask implements IBlockPlaceTask
{
    private Location base;
    private List<Tuple<Vector, IMaterialAndData>> blockData;
    private boolean fast = true;

    private boolean isDone = false;
    private Callback<Boolean> callback;

    private List<Chunk> chunks;

    public BlockPlaceTask(Location base, Tuple<Vector, IMaterialAndData> blockData, @Nullable Callback<Boolean> callback)
    {
        this(base, Lists.newArrayList(blockData), callback);
    }

    public BlockPlaceTask(Location base, List<Tuple<Vector, IMaterialAndData>> blockData, @Nullable Callback<Boolean> callback)
    {
        this(base, blockData, true, callback);
    }

    public BlockPlaceTask(Location base, List<Tuple<Vector, IMaterialAndData>> blockData, boolean fast, @Nullable Callback<Boolean> callback)
    {
        this(base, blockData, fast, null, callback);
    }

    public BlockPlaceTask(Location base, List<Tuple<Vector, IMaterialAndData>> blockData, boolean fast, @Nullable List<Chunk> chunks, @Nullable Callback<Boolean> callback)
    {
        this.base = base;
        this.blockData = blockData;
        this.chunks = chunks;
        this.callback = callback;
    }

    @Override
    public boolean isDone()
    {
        return isDone;
    }

    @Override
    public void run()
    {
        new BukkitRunnable()
        {
            List<Vector> vectors = Lists.newArrayList();

            @Override
            public void run()
            {
                if (blockData.isEmpty())
                {
                    cancel();
                    isDone = true;

                    if (fast)
                    {
                        if (chunks != null)
                        {
                            DungeonCrawler.getBlockUtil().refreshChunks(chunks);
                        }
                        else
                        {
                            DungeonCrawler.getBlockUtil().refreshChunkVectors(vectors, base);
                        }
                    }

                    if (callback != null)
                        callback.onSuccess(true);
                }
                else
                {
                    for (int index = 0; index <= 5000; index++)
                    {
                        if (blockData.isEmpty())
                        {
                            break;
                        }

                        Tuple<Vector, IMaterialAndData> data = blockData.get(0);
                        blockData.remove(0);

                        if (data == null)
                            continue;

                        Vector vector = data.getA();
                        Location location = VectorUtil.toLocation(vector, base);

                        IMaterialAndData materialAndData = data.getB();

                        if (fast)
                        {
                            DungeonCrawler.getBlockUtil().setBlockFast(location, materialAndData);
                        }
                        else
                        {
                            DungeonCrawler.getBlockUtil().setBlockSlow(location, materialAndData);
                        }

                        vectors.add(vector);

                        index++;
                    }
                }
            }
        }.runTaskTimer(DungeonCrawler.getPlugin(), 0, 5);
    }
}
