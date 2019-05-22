package org.jared.dungeoncrawler.plugin.commands;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jared.dungeoncrawler.api.IDungeon;
import org.jared.dungeoncrawler.api.concurrency.Callback;
import org.jared.dungeoncrawler.api.game.IGameContext;
import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.api.player.IDungeonPlayer;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.game.GameContext;
import org.jared.dungeoncrawler.generation.GenerationContext;
import org.jared.dungeoncrawler.plugin.Dungeon;

public class Commands implements CommandClass
{
    @Command(
            aliases = {"dungeon"},
            usage = "/dungeon create <name>",
            description = "creates a dungeon",
            initializer = "create",
            minArgs = 1,
            maxArgs = 1
    )
    public void create(CommandContext args, CommandSender sender)
    {
        Player player = (Player) sender;
        IDungeonPlayer dungeonPlayer = DungeonCrawler.getDungeonPlayerRegistry().getDungeonPlayer(player.getUniqueId());

        String dungeonName = args.getString(0);

        Location playerLocation = player.getLocation();
        int x = (int) playerLocation.getX();
        int y = (int) playerLocation.getY();
        int z = (int) playerLocation.getZ();

        Location location = new Location(player.getWorld(), x, y, z);

        IGenerationContext generationContext = new GenerationContext(location, dungeonName);
        IGameContext gameContext = new GameContext(player.getWorld(), Lists.newArrayList(player.getUniqueId()), generationContext);
        IDungeon dungeon = new Dungeon(generationContext, gameContext);
        dungeonPlayer.setDungeon(dungeon);

        for (Player player1 : Bukkit.getOnlinePlayers())
            player1.teleport(new Location(player.getWorld(), 42, 83, 575));

        /*
        BukkitTask loading = new BukkitRunnable()
        {
            boolean start = true;
            int counter = 0;

            @Override
            public void run()
            {
                String message = null;

                if (counter == 0)
                {
                    message = "loading.";
                }
                else if (counter == 1)
                {
                    message = "loading..";
                }
                else if (counter == 2)
                {
                    message = "loading...";
                    counter = -1;
                }

                if (start)
                {
                    player.sendTitle(ChatColor.RED + "The Dungeon is being built!", message, 10, 100000, 10);
                    start = false;
                }
                else
                {
                    DungeonCrawler.getTitleUtil().sendSubtitle(player, message, 10, 20, 10);
                }

                counter++;
            }
        }.runTaskTimer(DungeonCrawler.getPlugin(), 0, 20);
        */

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                dungeon.getGenerationContext().start(new Callback<Boolean>()
                {
                    @Override
                    public void onSuccess(Boolean aBoolean)
                    {
                        dungeon.getGameContext().setState(new org.jared.dungeoncrawler.game.states.preparation.InitializeState());
                        dungeon.getGameContext().runState();
                    }

                    @Override
                    public void onFailure(Throwable cause)
                    {
                        cause.printStackTrace();
                    }
                });
            }
        }.runTaskLaterAsynchronously(DungeonCrawler.getPlugin(), 20 * 5);
    }
}
