package org.jared.dungeoncrawler.game.rooms;

import org.jared.dungeoncrawler.api.generation.cell.AbstractRoom;
import org.jared.dungeoncrawler.api.generation.cell.IRoom;

//wrapper class
public class Room extends AbstractRoom implements org.jared.dungeoncrawler.api.game.IRoom
{
    private boolean triggered = false;

    public Room(IRoom cell)
    {
        this.type = cell.getType();
        this.aabb = cell.getAABB();
        this.decorations = cell.getDecorations();
        this.doorways = cell.getDoorways();
    }

    @Override
    public boolean hasBeenTriggered()
    {
        return triggered;
    }

    @Override
    public void setTriggered(boolean triggered)
    {
        this.triggered = triggered;
    }
}
