package org.jared.dungeoncrawler.generation.states;

import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.generation.DungeonMap;
import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.api.generation.State;
import org.jared.dungeoncrawler.api.generation.cell.CellType;
import org.jared.dungeoncrawler.api.generation.cell.CellUtil;
import org.jared.dungeoncrawler.api.generation.cell.IRoom;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.api.structures.AABB;
import org.jared.dungeoncrawler.generation.cell.Room;

import java.util.List;

public class FillState implements State
{
    @Override
    public int order()
    {
        return 3;
    }

    @Override
    public void run(IGenerationContext generationContext)
    {
        DungeonCrawler.LOG.info("Filling DungeonMap...");

        List<IRoom> cells = generationContext.getRooms();
        List<IRoom> fillerCells = generationContext.getFillerCells();

        DungeonMap dungeonMap = DungeonMap.get(generationContext.getBaseLocation(), cells);
        generationContext.setMap(dungeonMap);

        Vector baseVector = generationContext.getBaseVector();

        for (double x = dungeonMap.getLeft(); x < dungeonMap.getRight(); x++)
        {
            for (double z = dungeonMap.getBottom(); z < dungeonMap.getTop(); z++)
            {
                baseVector.setX(x);
                baseVector.setZ(z);

                if (CellUtil.getCell(baseVector, cells) == null)
                {
                    IRoom cell = new Room();
                    cell.setAABB(new AABB((int) x, baseVector.getBlockY(), (int) z, (int) x, baseVector.getBlockY(), (int) z));
                    cell.setType(CellType.FILL);
                    fillerCells.add(cell);
                }
            }
        }

        generationContext.setState(new SelectRoomsState());
        generationContext.runState();
    }
}
