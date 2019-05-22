package org.jared.dungeoncrawler.v1_13_R2.packets;

import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.Packet;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketNMS
{
    public static void sendPacket(Object packet, Player player)
    {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        entityPlayer.playerConnection.sendPacket((Packet<?>) packet);

    }
}
