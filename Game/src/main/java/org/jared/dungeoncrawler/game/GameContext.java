package org.jared.dungeoncrawler.game;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.game.IGameContext;
import org.jared.dungeoncrawler.api.game.IRoom;
import org.jared.dungeoncrawler.api.game.State;
import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.api.generation.cell.IHallway;

import java.util.List;
import java.util.UUID;

public class GameContext implements IGameContext
{
    private State state;

    private IGenerationContext generationContext;
    private List<IRoom> rooms = Lists.newArrayList();

    private World world;
    private List<UUID> players;

    public GameContext(World world, List<UUID> players, IGenerationContext generationContext)
    {
        this.world = world;
        this.players = players;
        this.generationContext = generationContext;
    }

    @Override
    public State getState()
    {
        return state;
    }

    @Override
    public void setState(State state)
    {
        this.state = state;
    }

    @Override
    public void runState()
    {
        state.run(this);
    }

    @Override
    public IGenerationContext getGenerationContext()
    {
        return generationContext;
    }

    @Override
    public List<Vector> getGate()
    {
        return generationContext.getGate();
    }

    @Override
    public Location getBaseLocation()
    {
        return generationContext.getBaseLocation();
    }

    @Override
    public Vector getBaseVector()
    {
        return generationContext.getBaseVector();
    }

    @Override
    public IRoom getStartingRoom()
    {
        return rooms.get(0);
    }

    @Override
    public List<IRoom> getRooms()
    {
        return rooms;
    }

    @Override
    public void setRooms(List<IRoom> rooms)
    {
        this.rooms = rooms;
    }

    @Override
    public List<IHallway> getHallways()
    {
        return generationContext.getHallways();
    }

    @Override
    public List<UUID> getPlayers()
    {
        return players;
    }

    @Override
    public World getWorld()
    {
        return world;
    }
}
