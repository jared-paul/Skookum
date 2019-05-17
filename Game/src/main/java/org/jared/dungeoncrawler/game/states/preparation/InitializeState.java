package org.jared.dungeoncrawler.game.states.preparation;

import com.google.common.collect.Lists;
import org.jared.dungeoncrawler.api.game.IGameContext;
import org.jared.dungeoncrawler.api.game.State;
import org.jared.dungeoncrawler.api.generation.cell.IRoom;
import org.jared.dungeoncrawler.game.rooms.Room;

import java.util.List;

public class InitializeState implements State
{
    @Override
    public void run(IGameContext gameContext)
    {
        List<IRoom> cells = gameContext.getGenerationContext().getRooms();
        List<org.jared.dungeoncrawler.api.game.IRoom> rooms = Lists.newArrayList();

        for (IRoom cell : cells)
        {
            Room room = new Room(cell);
            rooms.add(room);
        }

        gameContext.setRooms(rooms);


        gameContext.setState(new CheckPointState());
        gameContext.runState();
    }
}
