package org.jared.dungeoncrawler.v1_12_R1.maps;

import com.google.common.collect.Lists;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jared.dungeoncrawler.api.maps.ArrayImage;
import org.jared.dungeoncrawler.api.maps.IMapUtil;
import org.jared.dungeoncrawler.v1_12_R1.PacketUtil;

import java.util.Collection;

public class MapUtil implements IMapUtil
{
    @Override
    public void sendMap(int mapID, ArrayImage image, Player player)
    {
        PacketUtil.sendPacket(constructMapPacket(mapID, image), player);
    }

    private Packet constructMapPacket(int mapID, ArrayImage image)
    {
        PacketPlayOutMap packetPlayOutMap = new PacketPlayOutMap(
                mapID,
                (byte) 0,
                false,
                Lists.newArrayList(),
                image.data,
                image.minX,
                image.minY,
                image.maxX,
                image.maxY
        );

        return packetPlayOutMap;
    }

    @Override
    public void showInInventory(Player player, int modifiedSlot, short mapID)
    {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        Container defaultContainer = entityPlayer.defaultContainer;
        int windowID = defaultContainer.windowId;

        ItemStack item = new ItemStack(Material.MAP, 1, mapID);
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        PacketPlayOutSetSlot packetPlayOutSetSlot = new PacketPlayOutSetSlot(windowID, modifiedSlot, nmsItem);

        PacketUtil.sendPacket(packetPlayOutSetSlot, player);
    }

    @Override
    public void showInItemFrame(Player player, int itemFrameID, int mapID)
    {

    }

    @Override
    public int spawnFakeItemFrame(Collection<? extends Player> player, Location location, BlockFace facingDirection)
    {
        return -1;
    }
}
