package org.jared.dungeoncrawler.game.states.preparation;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.game.IGameContext;
import org.jared.dungeoncrawler.api.game.IRoom;
import org.jared.dungeoncrawler.api.game.State;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.api.util.Direction;
import org.jared.dungeoncrawler.api.util.VectorUtil;

public class NPCState implements State
{
    @Override
    public void run(IGameContext gameContext)
    {
        IRoom start = gameContext.getStartingRoom();

        Vector doorway = start.getDoorways().get(0);

        Direction direction = Direction.test(start.getCenter(), doorway);
        DungeonCrawler.LOG.severe(direction);

        Vector spawn = doorway.clone().add(direction.toVector().multiply(1.5)).add(new Vector(0, 1.5, 0));

        Location location = VectorUtil.toLocation(spawn, gameContext.getBaseLocation());

        Villager villager = (Villager) gameContext.getWorld().spawnEntity(location, EntityType.VILLAGER);
        villager.setProfession(Villager.Profession.PRIEST);
        villager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 1000, false, false));
        villager.setCustomName("The Gatekeeper");
        villager.setCustomNameVisible(true);
        villager.setRemoveWhenFarAway(false);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (villager.getLocation().distanceSquared(location) >= 1)
                {
                    villager.teleport(location.setDirection(villager.getLocation().getDirection()));
                }
            }
        }.runTaskTimer(DungeonCrawler.getPlugin(), 4 * 20, 4 * 20);

        gameContext.setState(new CountdownState());
        gameContext.runState();
    }
}
