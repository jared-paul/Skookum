package org.jared.dungeoncrawler.api.player.registry;

import org.jared.dungeoncrawler.api.player.IDungeonPlayer;
import org.jared.dungeoncrawler.api.registry.Registry;

import java.util.UUID;

public interface IDungeonPlayerRegistry extends Registry
{
    IDungeonPlayer getDungeonPlayer(UUID playerUUID);

    void registerDungeonPlayer(UUID playerUUID);

    void unregisterDungeonPlayer(UUID playerUUID);
}
