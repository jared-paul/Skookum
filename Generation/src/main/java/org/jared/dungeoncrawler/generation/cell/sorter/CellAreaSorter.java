package org.jared.dungeoncrawler.generation.cell.sorter;


import org.jared.dungeoncrawler.api.generation.cell.IRoom;

import java.util.Comparator;

public class CellAreaSorter implements Comparator<IRoom>
{
    @Override
    public int compare(IRoom cell1, IRoom cell2)
    {
        if (cell1.getArea() < cell2.getArea())
        {
            return 1;
        }
        else if (cell1.getArea() > cell2.getArea())
        {
            return -1;
        }

        return 0;
    }
}
