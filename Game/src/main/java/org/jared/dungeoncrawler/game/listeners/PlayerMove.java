package org.jared.dungeoncrawler.game.listeners;

import com.google.common.collect.Lists;
import de.slikey.effectlib.effect.CircleEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.IDungeon;
import org.jared.dungeoncrawler.api.game.IGameContext;
import org.jared.dungeoncrawler.api.game.IRoom;
import org.jared.dungeoncrawler.api.generation.decorations.IDecoration;
import org.jared.dungeoncrawler.api.material.IMaterialAndData;
import org.jared.dungeoncrawler.api.player.IDungeonPlayer;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.api.structures.AABB;
import org.jared.dungeoncrawler.api.util.VectorUtil;
import org.jared.dungeoncrawler.game.util.RoomUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerMove implements Listener
{
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent moveEvent)
    {
        IDungeonPlayer dungeonPlayer = DungeonCrawler.getDungeonPlayerRegistry().getDungeonPlayer(moveEvent.getPlayer().getUniqueId());

        if (dungeonPlayer == null)
            return;

        IDungeon dungeon = dungeonPlayer.getDungeon();

        if (dungeon == null)
            return;

        IGameContext gameContext = dungeon.getGameContext();

        IRoom currentRoom = RoomUtil.getCurrentRoom(dungeonPlayer);

        if (currentRoom == null || currentRoom == gameContext.getStartingRoom() || currentRoom.hasBeenTriggered())
            return;

        AABB aabb = currentRoom.getAABB().clone();
        aabb.grow(-2, -1, -2);

        currentRoom.setTriggered(true);

        double amount = ((aabb.getArea() / 50) / 1.25) / 2;

        List<Vector> floor = Lists.newArrayList(aabb.getFacePoints(AABB.Face.BOTTOM));

        for (IDecoration decoration : currentRoom.getDecorations())
        {
            for (Map.Entry<Vector, IMaterialAndData> pointEntry : decoration.getStructure().getPoints(decoration.getPosition()).entrySet())
            {
                Vector vector = pointEntry.getKey();
                IMaterialAndData materialAndData = pointEntry.getValue();

                if (materialAndData.getMaterial() != Material.AIR)
                {
                    floor.remove(vector);
                }
            }
        }

        List<Vector> randomPoints = getRandomPointsFromList(floor, (int) Math.ceil(amount));

        for (Vector vector : randomPoints)
        {
            Location location = VectorUtil.toLocation(vector, gameContext.getBaseLocation());

            CircleEffect circleEffect = new CircleEffect(DungeonCrawler.getEffectManager());
            circleEffect.setLocation(location.clone().add(0, .1, 0));
            circleEffect.radius = 1;
            circleEffect.enableRotation = false;
            circleEffect.particle = Particle.DRAGON_BREATH;
            circleEffect.period = 2;
            circleEffect.iterations = 20;
            circleEffect.start();

            circleEffect.callback = new Runnable()
            {
                @Override
                public void run()
                {
                    gameContext.getWorld().spawnEntity(location, EntityType.ZOMBIE);
                }
            };
        }
    }

    private List<Vector> getRandomPointsFromList(List<Vector> objects, int amount)
    {
        List<Vector> copy = Lists.newArrayList(objects);
        List<Vector> randomElements = Lists.newArrayList();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < amount; i++)
        {
            int index = random.nextInt(copy.size());

            randomElements.add(copy.get(index).clone());
            copy.remove(index);
        }

        return randomElements;
    }
}
