package org.jared.dungeoncrawler.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jared.dungeoncrawler.api.concurrency.Callback;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;

import java.util.List;
import java.util.UUID;

public class Countdown extends BukkitRunnable
{
    private int totalSeconds;
    private int seconds;
    private String message;
    private Callback<Boolean> callback;

    private List<UUID> players;

    public Countdown(int seconds, String message, List<UUID> players, Callback<Boolean> callback)
    {
        this.totalSeconds = seconds;
        this.seconds = seconds;
        this.message = message;
        this.callback = callback;

        this.players = players;
    }

    @Override
    public void run()
    {
        if (seconds == 0)
        {
            cancel();
            callback.onSuccess(true);
        }

        for (UUID playerUUID : players)
        {
            Player player = Bukkit.getPlayer(playerUUID);

            if (player != null)
            {
                if (seconds == totalSeconds)
                {
                    player.sendTitle(message, "starting in: " + seconds, 10, 20 * seconds, 20);
                }

                DungeonCrawler.getTitleUtil().sendSubtitle(player, "starting in: " + seconds, 10, 20, 10);
            }
        }

        seconds--;
    }
}
