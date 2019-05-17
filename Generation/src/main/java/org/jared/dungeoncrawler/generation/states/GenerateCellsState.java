package org.jared.dungeoncrawler.generation.states;

import com.google.common.collect.Lists;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.bukkit.Bukkit;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.api.generation.ImageState;
import org.jared.dungeoncrawler.api.generation.State;
import org.jared.dungeoncrawler.api.generation.cell.IRoom;
import org.jared.dungeoncrawler.api.generation.maps.IMapContext;
import org.jared.dungeoncrawler.api.generation.util.PhysicsUtil;
import org.jared.dungeoncrawler.api.generation.util.XORShiftRandom;
import org.jared.dungeoncrawler.api.maps.MapConstants;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.api.structures.AABB;
import org.jared.dungeoncrawler.generation.cell.Room;
import org.jared.dungeoncrawler.generation.maps.MapPresenter;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class GenerateCellsState implements State, ImageState
{
    @Override
    public int order()
    {
        return 1;
    }

    @Override
    public void run(IGenerationContext generationContext)
    {
        DungeonCrawler.LOG.info("Generating Cells...");

        int roomCount = generationContext.getRoomCount();
        XORShiftRandom random = generationContext.getRandom();
        Vector cellSize = generationContext.getRoomSize();
        Vector areaCenter = generationContext.getAreaCenter();
        int radius = generationContext.getRadius();
        List<IRoom> cells = generationContext.getRooms();

        BufferedImage oldImage = null;
        for (int i = 0; i < roomCount; i++)
        {
            IRoom room = generateRoom(radius, cells, generationContext.getPhysicsWorld());

            oldImage = createImage(room, oldImage);
            generationContext.getMapContext().offerImage(oldImage, MapConstants.DEFAULT_TICK_DELAY);

            if (i == 0)
            {
                startMapPresentation(generationContext.getMapContext());
            }
        }

        generationContext.setState(new SeparationState());
        generationContext.runState();
    }

    private void startMapPresentation(IMapContext mapContext)
    {
        new MapPresenter(mapContext, Lists.newArrayList(Bukkit.getOnlinePlayers())).runTaskAsynchronously(DungeonCrawler.getPlugin());
    }

    public BufferedImage createImage(IRoom room, BufferedImage oldImage)
    {
        oldImage = getImage(oldImage, MapConstants.SCALE_FACTOR, MapConstants.SCALE_FACTOR);
        Graphics2D graphics2D = oldImage.createGraphics();

        int scaledX = getScaledPosition(room.getAABB().getMinX(), oldImage.getWidth());
        int scaledZ = getScaledPosition(room.getAABB().getMinZ(), oldImage.getHeight());

        graphics2D.setPaint(Color.BLUE);
        graphics2D.fillRect(
                scaledX,
                scaledZ,
                room.getAABB().getWidth(),
                room.getAABB().getDepth()
        );

        graphics2D.setPaint(Color.WHITE);
        graphics2D.drawRect(scaledX, scaledZ, room.getWidth(), room.getDepth());

        return oldImage;
    }

    private IRoom generateRoom(int radius, List<IRoom> cells, World physicsWorld)
    {
        IRoom room = new Room();

        Vector spawnPoint = PhysicsUtil.getRandomPointInCircle(radius);

        NormalDistribution normalDistribution = new NormalDistribution(15, 2);

        int sizeX = PhysicsUtil.round(normalDistribution.sample(), 0.1);
        int sizeZ = PhysicsUtil.round(normalDistribution.sample(), 0.1);
        room.setWidth(sizeX);
        room.setDepth(sizeZ);

        int minX = spawnPoint.getBlockX() - sizeX / 2;
        int minZ = spawnPoint.getBlockZ() - sizeZ / 2;

        AABB aabb = new AABB(minX, spawnPoint.getBlockY(), minZ, minX + sizeX, spawnPoint.getBlockY() + 10, minZ + sizeZ);
        room.setAABB(aabb);

        initializePhysicsBody(room, physicsWorld);

        cells.add(room);

        return room;
    }

    private void initializePhysicsBody(IRoom room, World physicsWorld)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set((float) room.getCenter().getX(), (float) room.getCenter().getZ());
        Body body = physicsWorld.createBody(bodyDef);
        body.setSleepingAllowed(true);

        PolygonShape shape = new PolygonShape();

        shape.setAsBox((float) (room.getWidth() / 2) + 2F, (float) (room.getDepth() / 2) + 2F);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0;
        fixtureDef.friction = 0;

        body.createFixture(fixtureDef);

        room.setPhysicsBody(body);
    }
}
