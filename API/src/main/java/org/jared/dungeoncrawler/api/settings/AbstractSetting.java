package org.jared.dungeoncrawler.api.settings;

import org.bukkit.configuration.file.FileConfiguration;

public abstract class AbstractSetting<T> implements SettingObject<T>
{
    protected String path;
    protected T value;

    public AbstractSetting(String path, T value)
    {
        this.path = path;
        this.value = value;
    }

    @Override
    public boolean hasKey(FileConfiguration config)
    {
        return config.isSet(path);
    }

    @Override
    public String getPath()
    {
        return path;
    }

    @Override
    public void setPath(String path)
    {
        this.path = path;
    }

    @Override
    public T getValue()
    {
        return value;
    }

    @Override
    public SettingObject<T> clone()
    {
        try
        {
            return (SettingObject<T>) super.clone();
        } catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
