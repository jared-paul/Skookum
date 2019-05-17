package org.jared.dungeoncrawler.generation.states;

import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.api.generation.State;
import org.jared.dungeoncrawler.api.generation.cell.IRoom;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.generation.cell.sorter.CellBaseSorter;

import java.util.List;

public class FinishState implements State
{
    @Override
    public int order()
    {
        return 13;
    }

    @Override
    public void run(IGenerationContext generationContext)
    {
        List<IRoom> cells = generationContext.getRooms();
        cells.sort(new CellBaseSorter(generationContext.getBaseVector()));

        generationContext.isFinished().onSuccess(true);

        DungeonCrawler.LOG.debug("FINISHED");
    }
}
