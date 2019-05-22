package org.jared.dungeoncrawler.api.settings;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jared.dungeoncrawler.api.game.mobs.Mob;
import org.jared.dungeoncrawler.api.generation.theme.Theme;
import org.jared.dungeoncrawler.api.material.XMaterial;
import org.jared.dungeoncrawler.api.settings.objects.MobSetting;
import org.jared.dungeoncrawler.api.settings.objects.ThemeSetting;

import java.util.Map;

public enum DungeonSetting
{
    THEME,
    MOBS;

    private SettingObject settingObject;

    static
    {
        Map<XMaterial, Integer> floor = Maps.newHashMap();
        floor.put(XMaterial.STONE_BRICKS, 60);
        floor.put(XMaterial.MOSSY_STONE_BRICKS, 20);
        floor.put(XMaterial.CRACKED_STONE_BRICKS, 20);

        Map<XMaterial, Integer> walls = Maps.newHashMap();
        walls.put(XMaterial.STONE_BRICKS, 100);

        final Theme DEFAULT_THEME = new Theme("default", floor, walls, Maps.newHashMap());

        THEME.settingObject = new ThemeSetting("generation.theme", DEFAULT_THEME);
        MOBS.settingObject = new MobSetting("generation.mobs", Lists.newArrayList(Mob.SKELETON));
    }

    public SettingObject getSettingObject()
    {
        return settingObject;
    }
}

