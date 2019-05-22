package org.jared.dungeoncrawler.generation.states;

import com.google.common.collect.Lists;
import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.api.generation.ImageState;
import org.jared.dungeoncrawler.api.generation.State;
import org.jared.dungeoncrawler.api.generation.cell.CellType;
import org.jared.dungeoncrawler.api.generation.cell.IRoom;
import org.jared.dungeoncrawler.api.maps.MapConstants;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.generation.cell.sorter.CellBaseSorter;
import org.jared.dungeoncrawler.generation.cell.sorter.Strategies;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class SelectRoomsState implements State, ImageState
{
    @Override
    public int order()
    {
        return 4;
    }

    @Override
    public void run(IGenerationContext generationContext)
    {
        DungeonCrawler.LOG.info("Selecting Rooms...");

        List<IRoom> rooms = generationContext.getRooms();
        int roomAreaThreshold = generationContext.getRoomAreaThreshold();
        int minRooms = generationContext.getMinRooms();

        if (rooms.size() < minRooms)
        {
            List<IRoom> orderedRooms = Lists.newArrayList(rooms);

            if (!rooms.isEmpty())
                orderedRooms.removeAll(rooms);

            orderedRooms.sort(Strategies.getCellAreaComparator());

            BufferedImage oldImage = generationContext.getMapContext().getPreviousImage().getMapWrapper().getMainBufferedImage();
            IRoom room;
            while (rooms.size() < minRooms)
            {
                room = orderedRooms.remove(0);
                room.setType(CellType.ROOM);
                rooms.add(room);

                DungeonCrawler.LOG.debug("test");

                oldImage = createImage(room, oldImage);
                generationContext.getMapContext().offerImage(oldImage, 4);
            }
        }

        rooms.sort(new CellBaseSorter(generationContext.getBaseVector()));

        generationContext.setState(new TriangulateState());
        generationContext.runState();
    }

    private BufferedImage createImage(IRoom room, BufferedImage oldImage)
    {
        oldImage = getImage(oldImage, MapConstants.SCALE_FACTOR, MapConstants.SCALE_FACTOR);
        Graphics2D graphics2D = oldImage.createGraphics();

        int scaledX = getScaledPosition(room.getAABB().getMinX(), oldImage.getWidth());
        int scaledZ = getScaledPosition(room.getAABB().getMinZ(), oldImage.getHeight());

        graphics2D.setPaint(Color.RED);
        graphics2D.fillRect(scaledX, scaledZ, room.getWidth(), room.getDepth());

        graphics2D.setPaint(Color.WHITE);
        graphics2D.drawRect(scaledX, scaledZ, room.getWidth(), room.getDepth());

        return oldImage;
    }
}
