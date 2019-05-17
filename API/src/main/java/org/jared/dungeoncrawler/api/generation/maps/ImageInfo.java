package org.jared.dungeoncrawler.api.generation.maps;

import org.jared.dungeoncrawler.api.maps.wrappers.MultiMapWrapper;

public class ImageInfo
{
    private MultiMapWrapper mapWrapper;
    private int nextDelayInTicks;

    public ImageInfo(MultiMapWrapper mapWrapper, int nextDelayInTicks)
    {
        this.mapWrapper = mapWrapper;
        this.nextDelayInTicks = nextDelayInTicks;
    }

    public MultiMapWrapper getMapWrapper()
    {
        return mapWrapper;
    }

    public int getNextDelayInTicks()
    {
        return nextDelayInTicks;
    }

    public void setNextDelayInTicks(int nextDelayInTicks)
    {
        this.nextDelayInTicks = nextDelayInTicks;
    }
}
