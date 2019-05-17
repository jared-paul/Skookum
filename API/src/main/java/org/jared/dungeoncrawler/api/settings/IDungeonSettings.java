package org.jared.dungeoncrawler.api.settings;

import java.util.Map;

public interface IDungeonSettings
{
    Map<DungeonSetting, SettingObject> getDungeonSettings(String dungeonName);
}
