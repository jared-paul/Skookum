package org.jared.dungeoncrawler.api.settings;

import com.google.common.collect.Maps;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class DungeonSettings implements IDungeonSettings
{
    private FileConfiguration config;

    private Map<String, Map<DungeonSetting, SettingObject>> dungeonSettings = Maps.newHashMap();

    public DungeonSettings() throws IOException
    {
        File configFile = new File(DungeonCrawler.getPlugin().getDataFolder() + "\\Generation\\config.yml");

        config = YamlConfiguration.loadConfiguration(configFile);

        config.options().copyDefaults(true);

        addDefaults();
        registerSettings();
        updateDefaults();

        config.save(configFile);
    }

    private void updateDefaults()
    {
        for (String dungeonName : config.getConfigurationSection("dungeons").getKeys(false))
        {
            for (SettingObject settingObject : dungeonSettings.get(dungeonName).values())
            {
                if (settingObject.hasKey(config))
                {
                    settingObject.setValue(config);
                }
                else
                {
                    settingObject.addDefault(config);
                }
            }
        }
    }

    private void registerSettings()
    {
        for (String dungeonName : config.getConfigurationSection("dungeons").getKeys(false))
        {
            dungeonSettings.put(dungeonName, Maps.newHashMap());

            for (DungeonSetting dungeonSetting : DungeonSetting.values())
            {
                SettingObject settingObject = dungeonSetting.getSettingObject().clone();
                settingObject.setPath("dungeons." + dungeonName + "." + settingObject.getPath());

                dungeonSettings.get(dungeonName).put(dungeonSetting, settingObject);
            }
        }
    }

    private void addDefaults()
    {
        //ice theme
        config.addDefault("themes.ice.materials.floor.SNOW_BLOCK.chance", 70);
        config.addDefault("themes.ice.materials.floor.DIRT.chance", 30);

        config.addDefault("themes.ice.materials.walls.SNOW_BLOCK.chance", 50);
        config.addDefault("themes.ice.materials.walls.DIRT.chance", 30);
        config.addDefault("themes.ice.materials.walls.ICE.chance", 20);

        config.addDefault("themes.ice.materials.misc.JUNGLE_PLANKS.chance", 100);

        //nether theme
        config.addDefault("themes.nether.materials.floor.NETHER_BRICKS.chance", 100);
        config.addDefault("themes.nether.materials.walls.NETHER_BRICKS.chance", 100);
        config.addDefault("themes.nether.materials.misc.NETHERRACK.chance", 100);

        /*
        config.addDefault("themes.ice.floor", XMaterial.toStringList(Lists.newArrayList(
                XMaterial.SNOW_BLOCK,
                XMaterial.DIRT
        )));
        config.addDefault("themes.ice.wall", XMaterial.toStringList(Lists.newArrayList(
                XMaterial.SNOW_BLOCK,
                XMaterial.DIRT,
                XMaterial.ICE
        )));
        config.addDefault("themes.ice.misc", XMaterial.toStringList(Lists.newArrayList(
                XMaterial.JUNGLE_PLANKS
        )));
        config.addDefault("themes.nether.floor", XMaterial.toStringList(Lists.newArrayList(
                XMaterial.NETHER_BRICKS
        )));
        config.addDefault("themes.nether.wall", XMaterial.toStringList(Lists.newArrayList(
                XMaterial.NETHER_BRICKS
        )));
        config.addDefault("themes.nether.misc", XMaterial.toStringList(Lists.newArrayList(
                XMaterial.NETHERRACK
        )));
         */

        config.addDefault("dungeons.dungeon1", "");
    }

    @Override
    public Map<DungeonSetting, SettingObject> getDungeonSettings(String dungeonName)
    {
        return dungeonSettings.get(dungeonName);
    }
}
