package org.jared.dungeoncrawler.game.states.preparation;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.concurrency.Callback;
import org.jared.dungeoncrawler.api.data.Tuple;
import org.jared.dungeoncrawler.api.game.IGameContext;
import org.jared.dungeoncrawler.api.game.IRoom;
import org.jared.dungeoncrawler.api.game.State;
import org.jared.dungeoncrawler.api.generation.block.BlockPlaceTask;
import org.jared.dungeoncrawler.api.generation.block.IMaterialAndData;
import org.jared.dungeoncrawler.api.generation.block.MaterialAndData;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.api.util.RandomUtil;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CheckPointState implements State
{
    @Override
    public void run(IGameContext gameContext)
    {
        List<IRoom> possibleRooms = Lists.newArrayList(gameContext.getRooms());
        List<IRoom> checkPoints = getCheckPointRooms(possibleRooms);

        //checkpoints max is like 50 blocks, no need to split blocks up
        List<Tuple<Vector, IMaterialAndData>> blockData = Lists.newArrayList();

        for (IRoom checkPoint : checkPoints)
        {
            Vector[] post = {checkPoint.getCenter().clone().add(new Vector(0, 1, 0)), checkPoint.getCenter().clone().add(new Vector(0, 2, 0)), checkPoint.getCenter().clone().add(new Vector(0, 3, 0))};

            for (int j = 0; j < 3; j++)
            {
                Vector vector = post[j];
                Tuple<Vector, IMaterialAndData> data;

                if (j == 2)
                {
                    data = new Tuple<>(vector, new MaterialAndData(Material.GLOWSTONE));
                }
                else
                {
                    data = new Tuple<>(vector, new MaterialAndData(Material.DARK_OAK_FENCE));
                }

                blockData.add(data);
            }
        }

        gameContext.getGenerationContext().getBlockPlacer().addTask(new BlockPlaceTask(gameContext.getBaseLocation(), blockData, new Callback<Boolean>()
        {
            @Override
            public void onSuccess(Boolean aBoolean)
            {
                gameContext.setState(new NPCState());
                gameContext.runState();
            }

            @Override
            public void onFailure(Throwable cause)
            {

            }
        }));
    }

    private List<IRoom> getCheckPointRooms(List<IRoom> roomsCopy)
    {
        roomsCopy.remove(0);
        roomsCopy.remove(roomsCopy.size() - 1);

        int[] notPreferred = {2, 4};
        int amount = RandomUtil.generatePreferredNumbers(notPreferred, 4, 2);

        DungeonCrawler.LOG.console("---------");
        DungeonCrawler.LOG.debug(amount);
        DungeonCrawler.LOG.console("---------");

        List<IRoom> checkPointRooms = Lists.newArrayList();

        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < amount; i++)
        {
            int index = random.nextInt(roomsCopy.size());

            while (index == 0)
            {
                index = random.nextInt(roomsCopy.size());
            }

            IRoom room = roomsCopy.get(index);

            roomsCopy.remove(room);
            checkPointRooms.add(room);
        }

        return checkPointRooms;
    }
}
