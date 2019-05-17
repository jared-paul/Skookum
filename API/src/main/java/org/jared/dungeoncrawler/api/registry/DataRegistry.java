package org.jared.dungeoncrawler.api.registry;

import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;

import java.io.File;

public abstract class DataRegistry implements Registry
{
    protected File folder;

    public DataRegistry(String folderName)
    {
        this.folder = new File(DungeonCrawler.getPlugin().getDataFolder() + "\\" + folderName);

        if (!folder.exists())
        {
            folder.mkdirs();
        }
    }
}
