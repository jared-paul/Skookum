package org.jared.dungeoncrawler.api.generation.block;

import java.util.concurrent.BlockingQueue;

public interface IBlockPlacer extends Runnable
{
    BlockingQueue<IBlockPlaceTask> getPlacingQueue();

    void addTask(IBlockPlaceTask blockPlaceTask);
}
