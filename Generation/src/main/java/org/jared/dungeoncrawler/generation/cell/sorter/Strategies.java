package org.jared.dungeoncrawler.generation.cell.sorter;

import org.jared.dungeoncrawler.api.generation.cell.IRoom;

import java.util.Comparator;

public class Strategies
{
    private static final Comparator<IRoom> CELL_AREA_COMPARATOR = new CellAreaSorter();

    private static final Comparator<IRoom> CELL_DISTANCE_COMPARATOR = new CellDistanceSorter();

    public static Comparator<IRoom> getCellAreaComparator()
    {
        return CELL_AREA_COMPARATOR;
    }

    public static Comparator<IRoom> getCellDistanceComparator()
    {
        return CELL_DISTANCE_COMPARATOR;
    }
}
