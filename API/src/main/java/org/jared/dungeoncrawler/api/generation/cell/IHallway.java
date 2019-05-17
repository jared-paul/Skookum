package org.jared.dungeoncrawler.api.generation.cell;

import org.bukkit.util.Vector;

import java.util.List;

public interface IHallway
{
    List<Vector> getFloor();

    List<Vector> getWalls();

    IRoom getStartingRoom();

    IRoom getEndingRoom();
}
