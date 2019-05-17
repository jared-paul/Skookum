package org.jared.dungeoncrawler.api.util;

import org.bukkit.entity.Player;

public interface ITitleUtil
{
    void sendSubtitle(Player player, String subtitleMsg, int fadeIn, int stayTime, int fadeOut);
}
