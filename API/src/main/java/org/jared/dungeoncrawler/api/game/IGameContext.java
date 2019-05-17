package org.jared.dungeoncrawler.api.game;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.api.generation.cell.IHallway;

import java.util.List;
import java.util.UUID;

public interface IGameContext
{
    void setState(State state);

    State getState();

    void runState();

    IGenerationContext getGenerationContext();

    List<Vector> getGate();

    Location getBaseLocation();

    Vector getBaseVector();

    IRoom getStartingRoom();

    List<IRoom> getRooms();

    void setRooms(List<IRoom> rooms);

    List<IHallway> getHallways();

    List<UUID> getPlayers();

    World getWorld();
}
