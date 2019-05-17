package org.jared.dungeoncrawler.api.maps.colour.mcsd;

import org.bukkit.map.MapPalette;
import org.jared.dungeoncrawler.api.maps.colour.MapColourSpaceData;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;

public class MCSDGenBukkit extends MapColourSpaceData
{
    public void generate()
    {
        this.clear();
        for (int i = 0; i < 256; i++)
        {
            try
            {
                setColor((byte) i, MapPalette.getColor((byte) i));
            }
            catch (Throwable ignored) {}
        }
        for (int r = 0; r < 256; r++)
        {
            for (int g = 0; g < 256; g++)
            {
                for (int b = 0; b < 256; b++)
                {
                    set(r, g, b, MapPalette.matchColor(r, g, b));
                }
            }
        }

        DungeonCrawler.LOG.warning("DONE GENERATING");
    }
}