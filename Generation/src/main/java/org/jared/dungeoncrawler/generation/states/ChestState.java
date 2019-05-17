package org.jared.dungeoncrawler.generation.states;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.data.Tuple;
import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.api.generation.State;
import org.jared.dungeoncrawler.api.generation.block.BlockPlaceTask;
import org.jared.dungeoncrawler.api.generation.block.IMaterialAndData;
import org.jared.dungeoncrawler.api.generation.block.MaterialAndData;
import org.jared.dungeoncrawler.api.generation.cell.CellUtil;
import org.jared.dungeoncrawler.api.generation.cell.IHallway;
import org.jared.dungeoncrawler.api.generation.cell.IRoom;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.api.util.Direction;
import org.jared.dungeoncrawler.api.util.VectorUtil;

import java.util.List;

public class ChestState implements State
{
    @Override
    public int order()
    {
        return 11;
    }

    @Override
    public void run(IGenerationContext generationContext)
    {
        DungeonCrawler.LOG.info("Spawning Supply Chests...");

        for (IRoom cell : generationContext.getRooms())
        {
            int chance = 5;
            int random = generationContext.getRandom().nextInt(100);

            if (random < chance)
            {
                int mainCount = 0;
                for (Vector doorway : cell.getDoorways())
                {
                    for (Direction direction : Direction.getMainDirections())
                    {
                        Vector offset = doorway.clone().add(direction.toVector());

                        for (IHallway hallway : generationContext.getHallways())
                        {
                            if (hallway.getFloor().contains(offset))
                                doorway = offset;
                        }
                    }

                    if (mainCount >= 1)
                        continue;

                    //dependant on hallway width
                    double xmin = -3;
                    double xmax = 3;
                    double zmin = -3;
                    double zmax = 3;

                    boolean rand = generationContext.getRandom().nextBoolean();
                    Vector toAddCase1;
                    Vector toAddCase2;

                    if (rand)
                    {
                        xmin = 0;
                        xmax = 0;
                        toAddCase1 = new Vector(0, 0, -1);
                        toAddCase2 = new Vector(0, 0, 1);
                    }
                    else
                    {
                        zmin = 0;
                        zmax = 0;
                        toAddCase1 = new Vector(-1, 0, 0);
                        toAddCase2 = new Vector(1, 0, 0);
                    }

                    DungeonCrawler.LOG.info("1");

                    int count = 0;
                    for (Vector corner : VectorUtil.getCorners(xmin, xmax, zmin, zmax))
                    {
                        if (count >= 1)
                            continue;

                        DungeonCrawler.LOG.info("2");

                        int x = corner.getBlockX();
                        int z = corner.getBlockZ();

                        corner = corner.clone().add(doorway);

                        if (CellUtil.isPointInCells(corner, null, generationContext.getRooms()))
                        {
                            continue;
                        }

                        Vector up = new Vector(0, 1, 0);
                        Vector vector;

                        if (x == -3 || z == -3)
                        {
                            vector = corner.clone().add(toAddCase2).add(up);
                        }
                        else
                        {
                            vector = corner.clone().add(toAddCase1).add(up);
                        }

                        BlockFace direction = getDirection(vector, generationContext);

                        if (direction != null)
                        {
                            DungeonCrawler.LOG.warning(VectorUtil.toLocation(vector, generationContext.getBaseLocation()));

                            List<Tuple<Vector, IMaterialAndData>> blockData = Lists.newArrayList();
                            //blockData.add(new Tuple<>(vector, new MaterialAndData(Material.CHEST, toData(direction)))); //TODO add support for this direction, 1.13 :(
                            blockData.add(new Tuple<>(vector, new MaterialAndData(Material.CHEST)));

                            generationContext.drawInQueue(new BlockPlaceTask(generationContext.getBaseLocation(), blockData, null));
                            mainCount++;
                            count++;
                        }
                    }
                }
            }
        }

        generationContext.setState(new DecorationState());
        generationContext.runState();
    }

    private BlockFace getDirection(Vector vector, IGenerationContext context)
    {
        for (Direction direction : Direction.getMainDirections())
        {
            Vector testPoint = vector.clone().add(direction.toVector());

            if (CellUtil.getCell(testPoint, context.getRooms()) == null)
                continue;

            return direction.toBlockFace().getOppositeFace();
        }

        return null;
    }

    private byte toData(BlockFace blockFace)
    {
        byte data;

        switch (blockFace.ordinal())
        {
            case 1:
                data = 2;
                break;
            case 2:
            default:
                data = 5;
                break;
            case 3:
                data = 3;
                break;
            case 4:
                data = 4;
                break;
        }

        return data;
    }
}
