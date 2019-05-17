package org.jared.dungeoncrawler.api.game;

public interface IRoom extends org.jared.dungeoncrawler.api.generation.cell.IRoom
{
    boolean hasBeenTriggered();

    void setTriggered(boolean triggered);
}
