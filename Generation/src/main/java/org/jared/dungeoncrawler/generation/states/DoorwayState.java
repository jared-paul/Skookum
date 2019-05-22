package org.jared.dungeoncrawler.generation.states;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.data.Tuple;
import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.api.generation.State;
import org.jared.dungeoncrawler.api.generation.block.BlockPlaceTask;
import org.jared.dungeoncrawler.api.generation.cell.IRoom;
import org.jared.dungeoncrawler.api.material.IMaterialAndData;
import org.jared.dungeoncrawler.api.material.MaterialAndData;
import org.jared.dungeoncrawler.api.material.XMaterial;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.api.structures.AABB;
import org.jared.dungeoncrawler.api.util.VectorUtil;

import java.util.List;

public class DoorwayState implements State
{
    @Override
    public int order()
    {
        return 10;
    }

    @Override
    public void run(IGenerationContext generationContext)
    {
        DungeonCrawler.LOG.info("Clearing Doorways...");

        List<Tuple<Vector, IMaterialAndData>> doorwayData = Lists.newArrayList();

        List<Vector> gate = Lists.newArrayList();

        for (int i = 0; i < generationContext.getRooms().size(); i++)
        {
            IRoom cell = generationContext.getRooms().get(i);
            List<Vector> walls = null;

            if (i == 0)
            {
                walls = cell.getAABB().getFacePoints(AABB.Face.WALLS);
            }

            for (Vector doorway : cell.getDoorways())
            {
                if (i == 0)
                {
                    DungeonCrawler.LOG.severe("CENTER: "  + VectorUtil.toLocation(cell.getCenter(), generationContext.getBaseLocation()));
                    DungeonCrawler.LOG.severe("DOORWAY: " + test(doorway, cell.getCenter()));
                }

                List<Vector> doorwayPoints = VectorUtil.getDoorwayVectors(doorway);

                for (Vector vector : doorwayPoints)
                {
                    if (walls != null && walls.contains(vector))
                    {
                        gate.add(vector);
                        doorwayData.add(new Tuple<>(vector, new MaterialAndData(XMaterial.IRON_BARS.parseMaterial().getMaterial())));
                    }
                    else
                    {
                        doorwayData.add(new Tuple<>(vector, new MaterialAndData(Material.AIR)));
                    }
                }
            }
        }

        generationContext.setGate(gate);

        generationContext.getBlockPlacer().addTask(new BlockPlaceTask(generationContext.getBaseLocation(), doorwayData, null));

        generationContext.setState(new ChestState());
        generationContext.runState();
    }

    public List<Vector> getDoorwayVectors(Vector doorwayPoint, Vector roomCenter)
    {
        List<Vector> vectors = Lists.newArrayList();

        return vectors;
    }

    public Vector test(Vector doorwayPoint, Vector roomCenter)
    {
        return doorwayPoint.clone().subtract(roomCenter.clone());
    }
}
