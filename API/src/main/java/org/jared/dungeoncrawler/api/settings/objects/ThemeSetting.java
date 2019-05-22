package org.jared.dungeoncrawler.api.settings.objects;

import com.google.common.collect.Maps;
import org.bukkit.configuration.file.FileConfiguration;
import org.jared.dungeoncrawler.api.generation.theme.Theme;
import org.jared.dungeoncrawler.api.material.XMaterial;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.api.settings.AbstractSetting;

import java.util.Map;

public class ThemeSetting extends AbstractSetting<Theme>
{
    public ThemeSetting(String path, Theme value)
    {
        super(path, value);
    }

    @Override
    public void addDefault(FileConfiguration config)
    {
        config.addDefault(path, value.getName().toLowerCase());
    }

    @Override
    public void setValue(FileConfiguration config)
    {
        String themeName = config.getString(path);

        String themePath = "themes." + themeName + ".materials";

        Map<XMaterial, Integer> floorMaterial = Maps.newHashMap();
        Map<XMaterial, Integer> wallMaterial = Maps.newHashMap();
        Map<XMaterial, Integer> miscMaterial = Maps.newHashMap();

        DungeonCrawler.LOG.severe(themePath);
        DungeonCrawler.LOG.severe(config.getConfigurationSection(themePath));

        for (String placementKey : config.getConfigurationSection(themePath).getKeys(false))
        {
            String materialPlacementPath = themePath + "." + placementKey;

            for (String materialKey : config.getConfigurationSection(materialPlacementPath).getKeys(false))
            {
                String materialPath = materialPlacementPath + "." + materialKey;

                XMaterial material = XMaterial.fromString(materialKey);
                int chance = config.getInt(materialPath + ".chance");

                if (placementKey.equalsIgnoreCase("floor"))
                {
                    floorMaterial.put(material, chance);
                }
                else if (placementKey.equalsIgnoreCase("walls"))
                {
                    wallMaterial.put(material, chance);
                }
                else if (placementKey.equalsIgnoreCase("misc"))
                {
                    miscMaterial.put(material, chance);
                }
            }
        }

        DungeonCrawler.LOG.debug(floorMaterial);

        this.value = new Theme(themeName, floorMaterial, wallMaterial, miscMaterial);
    }
}