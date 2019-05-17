package org.jared.dungeoncrawler.plugin.registry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jared.dungeoncrawler.api.IDungeon;
import org.jared.dungeoncrawler.api.player.IDungeonPlayer;

import java.util.UUID;

public class DungeonPlayer implements IDungeonPlayer
{
    UUID playerUUID;
    IDungeon dungeon;

    public DungeonPlayer(UUID playerUUID)
    {
        this.playerUUID = playerUUID;
    }

    @Override
    public UUID getUUID()
    {
        return playerUUID;
    }

    @Override
    public Player getPlayer()
    {
        return Bukkit.getPlayer(playerUUID);
    }

    @Override
    public IDungeon getDungeon()
    {
        return dungeon;
    }

    @Override
    public void setDungeon(IDungeon dungeon)
    {
        this.dungeon = dungeon;
    }
}
