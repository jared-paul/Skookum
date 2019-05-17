package org.jared.dungeoncrawler.plugin.registry;

import org.jared.dungeoncrawler.api.player.IDungeonPlayer;
import org.jared.dungeoncrawler.api.player.registry.AbstractDungeonPlayerRegistry;

import java.util.UUID;

public class DungeonPlayerRegistry extends AbstractDungeonPlayerRegistry
{
    @Override
    protected IDungeonPlayer createDungeonPlayer(UUID playerUUID)
    {
        return new DungeonPlayer(playerUUID);
    }
}
