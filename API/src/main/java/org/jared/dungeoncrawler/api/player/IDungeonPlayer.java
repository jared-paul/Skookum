package org.jared.dungeoncrawler.api.player;

import org.bukkit.entity.Player;
import org.jared.dungeoncrawler.api.IDungeon;

import java.util.UUID;

public interface IDungeonPlayer
{
    UUID getUUID();

    Player getPlayer();

    IDungeon getDungeon();

    void setDungeon(IDungeon dungeon);
}
