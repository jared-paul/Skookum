package org.jared.dungeoncrawler.api.generation.maps;

public enum MapState
{
    GENERATION(10),
    SEPARATION(1);

    int tickSpeed;

    MapState(int tickSpeed)
    {
        this.tickSpeed = tickSpeed;
    }

    public int getTickSpeed()
    {
        return tickSpeed;
    }
}
