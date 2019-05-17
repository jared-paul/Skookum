package org.jared.dungeoncrawler.api.settings.objects;


import com.google.common.collect.Lists;
import org.bukkit.configuration.file.FileConfiguration;
import org.jared.dungeoncrawler.api.game.mobs.Mob;
import org.jared.dungeoncrawler.api.settings.AbstractSetting;

import java.util.List;

public class MobSetting extends AbstractSetting<List<Mob>>
{
    public MobSetting(String path, List<Mob> mobs)
    {
        super(path, mobs);
    }

    @Override
    public void addDefault(FileConfiguration config)
    {
        for (Mob mob : value)
        {
            String mobPath = path + "." + mob;

            config.addDefault(mobPath + ".chance", mob.getChance());
        }
    }

    @Override
    public void setValue(FileConfiguration config)
    {
        List<Mob> mobs = Lists.newArrayList();

        for (String mobName : config.getConfigurationSection(path).getKeys(false))
        {
            String mobPath = path + "." + mobName;

            int chance = config.getInt(mobPath + ".chance");

            Mob mob = Mob.valueOf(mobName.toUpperCase());
            mob.setChance(chance);

            mobs.add(mob);
        }

        this.value = mobs;
    }
}