package org.jared.dungeoncrawler.generation.states;

import com.google.common.collect.Lists;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.data.Tuple;
import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.api.generation.State;
import org.jared.dungeoncrawler.api.generation.block.BlockPlaceTask;
import org.jared.dungeoncrawler.api.generation.cell.CellType;
import org.jared.dungeoncrawler.api.generation.cell.IHallway;
import org.jared.dungeoncrawler.api.generation.cell.IRoom;
import org.jared.dungeoncrawler.api.generation.theme.Theme;
import org.jared.dungeoncrawler.api.material.IMaterialAndData;
import org.jared.dungeoncrawler.api.material.XMaterial;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.api.settings.DungeonSetting;
import org.jared.dungeoncrawler.api.structures.AABB;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class BuildState implements State
{
    @Override
    public int order()
    {
        return 8;
    }

    @Override
    public void run(IGenerationContext generationContext)
    {
        DungeonCrawler.LOG.info("Building...");

        Random random = ThreadLocalRandom.current();
        Theme theme = (Theme) generationContext.getDungeonSettings().get(DungeonSetting.THEME).getValue();

        List<IRoom> cellsMaster = generationContext.getCellsMaster();
        cellsMaster.addAll(generationContext.getIntersectingCells());

        //List<Vector> rooms = Lists.newArrayList();
        //List<Vector> none = Lists.newArrayList();

        Iterator<IRoom> cellIterator = generationContext.getIntersectingCells().iterator();
        while (cellIterator.hasNext())
        {
            IRoom cell = cellIterator.next();
            List<Tuple<Vector, IMaterialAndData>> roomBuildData = Lists.newArrayList();

            /*
            if (cell.getType() == CellType.ROOM)
            {
                rooms.addAll(buildVectors.values());
            }
            else */
            if (cell.getType() == CellType.FILL)
            {
                cellIterator.remove();
            }

            Map<XMaterial, Integer> miscMaterials = theme.getMisc();

            for (AABB.Face face : AABB.Face.values())
            {
                if (face == AABB.Face.TOP || face == AABB.Face.CORNERS)
                    continue;

                List<Vector> buildVectors = cell.getAABB().getFacePoints(face);
                Map<XMaterial, Integer> mainMaterials;

                for (Vector buildVector : buildVectors)
                {
                    XMaterial material = null;

                    int mainChance = random.nextInt(100);

                    if (face == AABB.Face.BOTTOM)
                    {
                        mainMaterials = theme.getFloor();
                    }
                    else if (face == AABB.Face.WALLS)
                    {
                        mainMaterials = theme.getWalls();
                    }
                    else if (mainChance == 1)
                    {
                        mainMaterials = theme.getMisc();
                    }
                    else
                    {
                        mainMaterials = miscMaterials;
                    }

                    int currentChance = 0;

                    for (Map.Entry<XMaterial, Integer> materialEntry : mainMaterials.entrySet())
                    {
                        XMaterial materialLoop = materialEntry.getKey();
                        int chance = materialEntry.getValue();

                        currentChance += chance;

                        if (mainChance < currentChance)
                        {
                            material = materialLoop;
                            break;
                        }
                    }

                    roomBuildData.add(new Tuple<>(buildVector, material.parseMaterial()));
                }
            }

            generationContext.drawInQueue(new BlockPlaceTask(generationContext.getBaseLocation(), roomBuildData, null));
        }


        for (IHallway hallway : generationContext.getHallways())
        {
            generationContext.drawImmediately(hallway.getFloor(), XMaterial.NETHER_BRICKS.parseMaterial());
            generationContext.drawImmediately(hallway.getWalls(), XMaterial.NETHER_BRICKS.parseMaterial());
        }

        generationContext.setState(new DoorwayState());
        generationContext.runState();
    }
}
