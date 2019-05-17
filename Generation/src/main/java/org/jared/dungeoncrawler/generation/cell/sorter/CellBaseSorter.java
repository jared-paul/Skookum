package org.jared.dungeoncrawler.generation.cell.sorter;

import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.generation.cell.IRoom;

import java.util.Comparator;

public class CellBaseSorter implements Comparator<IRoom>
{
    private Vector base;

    public CellBaseSorter(Vector base)
    {
        this.base = base;
    }

    @Override
    public int compare(IRoom cell1, IRoom cell2)
    {
        Double distance1 = cell1.getCenter().distanceSquared(base);
        Double distance2 = cell2.getCenter().distanceSquared(base);

        return distance1.compareTo(distance2);
    }
}
