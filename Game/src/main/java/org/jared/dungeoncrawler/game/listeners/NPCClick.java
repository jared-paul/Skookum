package org.jared.dungeoncrawler.game.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.IDungeon;
import org.jared.dungeoncrawler.api.data.Tuple;
import org.jared.dungeoncrawler.api.generation.block.BlockPlaceTask;
import org.jared.dungeoncrawler.api.material.IMaterialAndData;
import org.jared.dungeoncrawler.api.material.MaterialAndData;
import org.jared.dungeoncrawler.api.player.IDungeonPlayer;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.api.util.VectorUtil;

import java.util.List;
import java.util.UUID;

public class NPCClick implements Listener
{
    @EventHandler
    public void onRightClickNPC(PlayerInteractEntityEvent interactEvent)
    {
        Entity entity = interactEvent.getRightClicked();

        if (!(entity instanceof Villager))
            return;

        if (!entity.getCustomName().equals("The Gatekeeper"))
            return;

        if (entity.hasMetadata("triggered"))
        {
            interactEvent.setCancelled(true);
            return;
        }

        IDungeonPlayer dungeonPlayer = DungeonCrawler.getDungeonPlayerRegistry().getDungeonPlayer(interactEvent.getPlayer().getUniqueId());

        if (dungeonPlayer == null)
            return;

        IDungeon dungeon = dungeonPlayer.getDungeon();

        if (dungeon == null)
            return;

        interactEvent.setCancelled(true);
        entity.setMetadata("triggered", new FixedMetadataValue(DungeonCrawler.getPlugin(), true));

        for (UUID playerUUID : dungeon.getGameContext().getPlayers())
        {
            Player player = Bukkit.getPlayer(playerUUID);

            if (player != null)
            {
                player.sendMessage(ChatColor.DARK_RED + "So you've decided to attempt " + dungeon.getName());
                sendDelayedMessage(player, ChatColor.DARK_RED + "you really think you can make it out alive?", 3);
                sendDelayedMessage(player, ChatColor.DARK_RED + "I've seen things...", (3 * 2));
                sendDelayedMessage(player, ChatColor.DARK_RED + "Good luck", (3 * 3));
            }
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                List<Tuple<Vector, IMaterialAndData>> blockData = VectorUtil.toBlockData(dungeon.getGenerationContext().getGate(), new MaterialAndData(Material.AIR));
                dungeon.getGenerationContext().getBlockPlacer().addTask(new BlockPlaceTask(dungeon.getGenerationContext().getBaseLocation(), blockData, false, null));
            }
        }.runTaskLater(DungeonCrawler.getPlugin(), (long) (20 * (3 * 4)));
    }

    private void sendDelayedMessage(Player player, String message, double delayInSeconds)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                player.sendMessage(message);
            }
        }.runTaskLater(DungeonCrawler.getPlugin(), (long) (20 * delayInSeconds));
    }
}
