package org.jared.dungeoncrawler.api.plugin;

import de.slikey.effectlib.EffectManager;
import org.bukkit.plugin.Plugin;
import org.jared.dungeoncrawler.api.generation.block.IBlockUtil;
import org.jared.dungeoncrawler.api.generation.decorations.registry.IDecorationRegistry;
import org.jared.dungeoncrawler.api.maps.IMapUtil;
import org.jared.dungeoncrawler.api.maps.MapRegistry;
import org.jared.dungeoncrawler.api.player.registry.IDungeonPlayerRegistry;
import org.jared.dungeoncrawler.api.settings.IDungeonSettings;
import org.jared.dungeoncrawler.api.structures.IStructureUtil;
import org.jared.dungeoncrawler.api.util.ITitleUtil;

public interface IDungeonCrawlerMain extends Plugin
{
    IDecorationRegistry getDecorationRegistry();

    IDungeonPlayerRegistry getDungeonPlayerRegistry();

    IDungeonSettings getDungeonSettings();

    IStructureUtil getStructureUtil();

    IBlockUtil getBlockUtil();

    ITitleUtil getTitleUtil();

    EffectManager getEffectManager();

    MapRegistry getMapRegistry();

    IMapUtil getMapUtil();
}
