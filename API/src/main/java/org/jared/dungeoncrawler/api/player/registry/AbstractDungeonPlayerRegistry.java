package org.jared.dungeoncrawler.api.player.registry;

import com.google.common.collect.Maps;
import org.jared.dungeoncrawler.api.player.IDungeonPlayer;

import java.util.Map;
import java.util.UUID;

public abstract class AbstractDungeonPlayerRegistry implements IDungeonPlayerRegistry
{
    protected final Map<UUID, IDungeonPlayer> dungeonPlayers = Maps.newConcurrentMap();

    protected abstract IDungeonPlayer createDungeonPlayer(UUID playerUUID);

    @Override
    public IDungeonPlayer getDungeonPlayer(UUID playerUUID)
    {
        return dungeonPlayers.get(playerUUID);
    }

    @Override
    public void registerDungeonPlayer(UUID playerUUID)
    {
        dungeonPlayers.put(playerUUID, createDungeonPlayer(playerUUID));
    }

    @Override
    public void unregisterDungeonPlayer(UUID playerUUID)
    {
        dungeonPlayers.remove(playerUUID);
    }
}
