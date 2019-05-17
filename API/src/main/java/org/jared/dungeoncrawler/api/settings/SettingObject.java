package org.jared.dungeoncrawler.api.settings;

import org.bukkit.configuration.file.FileConfiguration;

public interface SettingObject<T> extends Cloneable
{
    boolean hasKey(FileConfiguration config);

    void addDefault(FileConfiguration config);

    String getPath();

    void setPath(String path);

    T getValue();

    void setValue(FileConfiguration config);

    SettingObject<T> clone();
}
