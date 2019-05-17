package org.jared.dungeoncrawler.api.plugin;

import de.slikey.effectlib.EffectManager;
import org.jared.dungeoncrawler.api.generation.block.IBlockUtil;
import org.jared.dungeoncrawler.api.generation.decorations.registry.IDecorationRegistry;
import org.jared.dungeoncrawler.api.logging.Log;
import org.jared.dungeoncrawler.api.maps.IMapUtil;
import org.jared.dungeoncrawler.api.maps.MapRegistry;
import org.jared.dungeoncrawler.api.player.registry.IDungeonPlayerRegistry;
import org.jared.dungeoncrawler.api.settings.IDungeonSettings;
import org.jared.dungeoncrawler.api.structures.IStructureUtil;
import org.jared.dungeoncrawler.api.util.ITitleUtil;

public class DungeonCrawler
{
    private static IDungeonCrawlerMain PLUGIN;
    public static final Log LOG = new Log("[DungeonCrawler]");

    public static void setPlugin(IDungeonCrawlerMain plugin)
    {
        if (PLUGIN != null)
            return;

        PLUGIN = plugin;
    }

    public static IDungeonCrawlerMain getPlugin()
    {
        return PLUGIN;
    }

    public static IDecorationRegistry getDecorationRegistry()
    {
        return PLUGIN.getDecorationRegistry();
    }

    public static IDungeonPlayerRegistry getDungeonPlayerRegistry()
    {
        return PLUGIN.getDungeonPlayerRegistry();
    }

    public static IDungeonSettings getDungeonSettings()
    {
        return PLUGIN.getDungeonSettings();
    }

    public static IStructureUtil getStructureUtil()
    {
        return PLUGIN.getStructureUtil();
    }

    public static IBlockUtil getBlockUtil()
    {
        return PLUGIN.getBlockUtil();
    }

    public static ITitleUtil getTitleUtil()
    {
        return PLUGIN.getTitleUtil();
    }

    public static EffectManager getEffectManager()
    {
        return PLUGIN.getEffectManager();
    }

    public static MapRegistry getMapRegistry()
    {
        return PLUGIN.getMapRegistry();
    }

    public static IMapUtil getMapUtil()
    {
        return PLUGIN.getMapUtil();
    }
}
