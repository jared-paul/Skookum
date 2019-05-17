package org.jared.dungeoncrawler.game.listeners;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;

public class PlayerJoin implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent joinEvent)
    {
        Player player = joinEvent.getPlayer();
        DungeonCrawler.getDungeonPlayerRegistry().registerDungeonPlayer(player.getUniqueId());

        Bukkit.getScheduler().runTaskLater(DungeonCrawler.getPlugin(), new Runnable()
        {
            @Override
            public void run()
            {
                player.damage(3D);
                player.damage(3D);
            }
        }, 20);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent quitEvent)
    {
        Player player = quitEvent.getPlayer();
        DungeonCrawler.getDungeonPlayerRegistry().unregisterDungeonPlayer(player.getUniqueId());
    }
}
