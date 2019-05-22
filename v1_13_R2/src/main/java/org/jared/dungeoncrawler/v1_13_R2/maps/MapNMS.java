package org.jared.dungeoncrawler.v1_13_R2.maps;

import com.google.common.collect.Lists;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.jared.dungeoncrawler.api.maps.ArrayImage;
import org.jared.dungeoncrawler.api.maps.IMapUtil;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.v1_13_R2.packets.PacketNMS;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public class MapNMS implements IMapUtil
{
    @Override
    public void sendMap(int mapID, ArrayImage image, Player player)
    {
        PacketNMS.sendPacket(constructMapPacket(mapID, image), player);
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

        ItemStack item = new ItemStack(Material.FILLED_MAP, 1);
        MapMeta mapMeta = (MapMeta) item.getItemMeta();
        mapMeta.setMapId(mapID);
        item.setItemMeta(mapMeta);

        net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        PacketPlayOutSetSlot packetPlayOutSetSlot = new PacketPlayOutSetSlot(windowID, modifiedSlot, nmsItem);

        PacketNMS.sendPacket(packetPlayOutSetSlot, player);
    }

    @Override
    public void showInItemFrame(Player player, int itemFrameID, int mapID)
    {
        ItemStack item = new ItemStack(Material.FILLED_MAP, 1);
        MapMeta mapMeta = (MapMeta) item.getItemMeta();
        mapMeta.setMapId(mapID);
        item.setItemMeta(mapMeta);

        sendItemFrame(player, itemFrameID, item);
    }

    private void sendItemFrame(Player player, int itemFrameID, ItemStack item)
    {
        try
        {
            PacketPlayOutEntityMetadata packetPlayOutEntityMetadata = new PacketPlayOutEntityMetadata();

            Field idField = packetPlayOutEntityMetadata.getClass().getDeclaredField("a");
            idField.setAccessible(true);

            idField.set(packetPlayOutEntityMetadata, itemFrameID);

            net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

            List list = Lists.newArrayList();

            Field dataWatcherObjectField = EntityItemFrame.class.getDeclaredField("e");
            dataWatcherObjectField.setAccessible(true);

            DataWatcherObject<net.minecraft.server.v1_13_R2.ItemStack> dataWatcherObject = (DataWatcherObject<net.minecraft.server.v1_13_R2.ItemStack>) dataWatcherObjectField.get(null);

            DataWatcher.Item dataWatcherItem = new DataWatcher.Item(dataWatcherObject, nmsItem);

            list.add(dataWatcherItem);

            Field bField = packetPlayOutEntityMetadata.getClass().getDeclaredField("b");
            bField.setAccessible(true);

            bField.set(packetPlayOutEntityMetadata, list);

            PacketNMS.sendPacket(packetPlayOutEntityMetadata, player);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int spawnFakeItemFrame(Collection<? extends Player> players, Location location, BlockFace facingDirection)
    {
        net.minecraft.server.v1_13_R2.World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityItemFrame nmsItemFrame = new EntityItemFrame(nmsWorld);
        nmsItemFrame.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsItemFrame.setDirection(EnumDirection.valueOf(facingDirection.name()));

        PacketPlayOutSpawnEntity spawnEntityPacket = new PacketPlayOutSpawnEntity(nmsItemFrame, 71);

        for (Player player : players)
        {
            PacketNMS.sendPacket(spawnEntityPacket, player);
        }

        return nmsItemFrame.getId();
    }

    private ItemFrame getItemFrameByID(World world, int itemFrameID)
    {
        try
        {
            net.minecraft.server.v1_13_R2.World nmsWorld = ((CraftWorld) world).getHandle();

            DungeonCrawler.LOG.severe(nmsWorld.getClass().getName());

            Field entitiesIDField = net.minecraft.server.v1_13_R2.World.class.getDeclaredField("entitiesById");
            entitiesIDField.setAccessible(true);

            IntHashMap<Entity> entitiesByID = (IntHashMap<Entity>) entitiesIDField.get(nmsWorld);
            Entity entity = entitiesByID.get(itemFrameID);

            org.bukkit.entity.Entity bukkitEntity = entity.getBukkitEntity();

            if (bukkitEntity != null && bukkitEntity.getType() == EntityType.ITEM_FRAME)
            {
                return (ItemFrame) bukkitEntity;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
