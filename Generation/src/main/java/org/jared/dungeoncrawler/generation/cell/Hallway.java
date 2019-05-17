package org.jared.dungeoncrawler.generation.cell;

import com.google.common.collect.Lists;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.generation.cell.IHallway;
import org.jared.dungeoncrawler.api.generation.cell.IRoom;

import java.util.List;

public class Hallway implements IHallway
{
    private List<Vector> floor;
    private List<Vector> walls;
    private IRoom start;
    private IRoom end;

    public Hallway(IRoom start, IRoom end)
    {
        this.start = start;
        this.end = end;
        this.floor = Lists.newArrayList();
        this.walls = Lists.newArrayList();
    }

    @Override
    public List<Vector> getFloor()
    {
        return floor;
    }

    @Override
    public List<Vector> getWalls()
    {
        return walls;
    }

    @Override
    public IRoom getStartingRoom()
    {
        return start;
    }

    @Override
    public IRoom getEndingRoom()
    {
        return end;
    }
}
