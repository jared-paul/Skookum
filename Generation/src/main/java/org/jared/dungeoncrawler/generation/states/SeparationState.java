package org.jared.dungeoncrawler.generation.states;

import com.google.common.collect.Lists;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.api.generation.ImageState;
import org.jared.dungeoncrawler.api.generation.State;
import org.jared.dungeoncrawler.api.generation.block.XMaterial;
import org.jared.dungeoncrawler.api.generation.cell.IRoom;
import org.jared.dungeoncrawler.api.generation.util.PhysicsUtil;
import org.jared.dungeoncrawler.api.generation.util.XORShiftRandom;
import org.jared.dungeoncrawler.api.maps.MapConstants;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.api.structures.AABB;
import org.jbox2d.dynamics.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;

public class SeparationState implements State, ImageState
{
    @Override
    public int order()
    {
        return 2;
    }

    @Override
    public void run(IGenerationContext generationContext)
    {
        DungeonCrawler.LOG.info("Separating Cells...");

        List<IRoom> rooms = generationContext.getRooms();

        moveRooms(generationContext.getPhysicsWorld(), rooms, generationContext);

        generationContext.setState(new SelectRoomsState());
        generationContext.runState();
    }

    public XMaterial getRandomWool()
    {
        XMaterial[] xMaterials = XMaterial.values();
        List<XMaterial> xMaterialList = Lists.newArrayList(xMaterials);
        Collections.shuffle(xMaterialList);

        for (XMaterial xMaterial : xMaterialList)
        {
            if (xMaterial.toString().contains("WOOL"))
            {
                return xMaterial;
            }
        }

        return XMaterial.BLACK_WOOL;
    }

    private void moveRooms(World physicsWorld, List<IRoom> rooms, IGenerationContext generationContext)
    {
        float timeStep = 1.0F / 20.0F;
        int velocityIterations = 8;
        int positionIterations = 3;

        int timeOut = 0;

        while (isAnyOverlap(rooms))
        {
            if (timeOut >= 5000)
            {
                break;
            }

            physicsWorld.step(timeStep, velocityIterations, positionIterations);

            BufferedImage oldImage = null;
            for (IRoom room : rooms)
            {
                int newCenterX = PhysicsUtil.round(room.getPhysicsBody().getWorldCenter().x, 0.1);
                int newCenterZ = PhysicsUtil.round(room.getPhysicsBody().getWorldCenter().y, 0.1);

                room.getAABB().setCenter(newCenterX, 0, newCenterZ);

                oldImage = createImage(room, oldImage);
            }

            generationContext.getMapContext().offerImage(oldImage, MapConstants.SMOOTH_TICK_DELAY);
            timeOut++;
        }

        //dispose of physics world, not needed anymore
        generationContext.setPhysicsWorld(null);
    }

    private BufferedImage createImage(IRoom room, BufferedImage oldImage)
    {
        oldImage = getImage(oldImage, MapConstants.SCALE_FACTOR, MapConstants.SCALE_FACTOR);
        Graphics2D graphics2D = oldImage.createGraphics();

        int scaledX = getScaledPosition(room.getAABB().getMinX(), oldImage.getWidth());
        int scaledZ = getScaledPosition(room.getAABB().getMinZ(), oldImage.getHeight());

        graphics2D.setPaint(Color.BLUE);
        graphics2D.fillRect(scaledX, scaledZ, room.getWidth(), room.getDepth());

        graphics2D.setPaint(Color.WHITE);
        graphics2D.drawRect(scaledX, scaledZ, room.getWidth(), room.getDepth());

        return oldImage;
    }

    private boolean isAnyOverlap(List<IRoom> cells)
    {
        IRoom cell;
        for (int i = 0; i < cells.size(); i++)
        {
            cell = cells.get(i);
            for (int j = 0; j < cells.size(); j++)
            {
                if (i == j)
                    continue;
                if (isOverlapping(cell, cells.get(j)))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOverlapping(IRoom room, IRoom otherRoom)
    {
        Vector min = room.getAABB().getMin();
        Vector max = room.getAABB().getMax();

        Vector otherMin = otherRoom.getAABB().getMin();
        Vector otherMax = otherRoom.getAABB().getMax();

        if (min.getBlockX() < otherMax.getBlockX() + 1 &&
                max.getBlockX() + 1 > otherMin.getBlockX() &&
                min.getBlockZ() < otherMax.getBlockZ() + 1 &&
                max.getBlockZ() + 1 > otherMin.getBlockZ()
        )
        {
            return true;
        }

        return false;
    }
}
