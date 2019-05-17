package org.jared.dungeoncrawler.api.game.mobs;

public enum Mob
{
    SKELETON(100),
    ZOMBIE(100),;

    int chance;

    Mob(int chance)
    {
        this.chance = chance;
    }

    public int getChance()
    {
        return chance;
    }

    public void setChance(int chance)
    {
        this.chance = chance;
    }
}
