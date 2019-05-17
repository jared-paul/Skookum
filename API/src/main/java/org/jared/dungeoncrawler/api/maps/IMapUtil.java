package org.jared.dungeoncrawler.api.maps;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface IMapUtil
{
    void sendMap(int mapID, ArrayImage image, Player player);

    void showInInventory(Player player, int modifiedSlot, short mapID);

    void showInItemFrame(Player player, int itemFrameID, int mapID);

    int spawnFakeItemFrame(Collection<? extends Player> player, Location location, BlockFace facingDirection);
}
