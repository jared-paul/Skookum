package org.jared.dungeoncrawler.generation.cell.sorter;

import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.generation.cell.IRoom;

import java.util.Comparator;

public class CellDistanceSorter implements Comparator<IRoom>
{
    private Vector comparable = new Vector();

    @Override
    public int compare(IRoom cell1, IRoom cell2)
    {
        /*
        double distance1 = cell1.body.getWorldCenter().distanceSquared(comparable.getBlockX(), comparable.getBlockZ());
        double distance2 = cell2.body.getLocalCenter().distanceSquared(comparable.getBlockX(), comparable.getBlockZ());

        if (distance1 < distance2)
        {
            return 1;
        }
        else if (distance1 > distance2)
        {
            return -1;
        }
        */

        return 0;
    }
}
