package org.jared.dungeoncrawler.v1_12_R1.util;

import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jared.dungeoncrawler.api.util.ITitleUtil;

public class TitleUtil implements ITitleUtil
{
    @Override
    public void sendSubtitle(Player player, String subtitleMsg, int fadeIn, int stayTime, int fadeOut)
    {
        PacketPlayOutTitle subtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitleMsg + "\"}"), fadeIn, stayTime, fadeOut);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subtitle);
    }
}
