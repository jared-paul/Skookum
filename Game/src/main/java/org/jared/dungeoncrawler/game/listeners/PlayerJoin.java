package org.jared.dungeoncrawler.game.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.api.structures.IStructure;

import java.io.File;

public class PlayerJoin implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent joinEvent)
    {
        Player player = joinEvent.getPlayer();
        DungeonCrawler.getDungeonPlayerRegistry().registerDungeonPlayer(player.getUniqueId());

        try
        {
            IStructure structure = DungeonCrawler.getStructureUtil().loadStructure(new File(DungeonCrawler.getPlugin().getDataFolder() + "\\Decorations\\Floor\\Wall\\bigtest.nbt"));
            structure.place(player.getLocation());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent quitEvent)
    {
        Player player = quitEvent.getPlayer();
        DungeonCrawler.getDungeonPlayerRegistry().unregisterDungeonPlayer(player.getUniqueId());
    }
}
