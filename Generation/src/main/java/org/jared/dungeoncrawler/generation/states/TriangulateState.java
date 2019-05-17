package org.jared.dungeoncrawler.generation.states;

import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.api.generation.ImageState;
import org.jared.dungeoncrawler.api.generation.State;
import org.jared.dungeoncrawler.api.generation.cell.IRoom;
import org.jared.dungeoncrawler.api.generation.delaunay.DT_Point;
import org.jared.dungeoncrawler.api.generation.delaunay.DT_Triangle;
import org.jared.dungeoncrawler.api.generation.delaunay.DelaunayTriangulation;
import org.jared.dungeoncrawler.api.generation.delaunay.EdgeList;
import org.jared.dungeoncrawler.api.maps.MapConstants;
import org.jared.dungeoncrawler.api.maps.util.MapImageUtil;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

public class TriangulateState implements State, ImageState
{
    @Override
    public int order()
    {
        return 5;
    }

    @Override
    public void run(IGenerationContext generationContext)
    {
        DungeonCrawler.LOG.info("Connecting Rooms...");

        DelaunayTriangulation delaunayTriangulation = new DelaunayTriangulation();
        generationContext.setDelaunayTriangulation(delaunayTriangulation);
        List<IRoom> rooms = generationContext.getRooms();

        BufferedImage oldImage = generationContext.getMapContext().getPreviousImage().getMapWrapper().getMainBufferedImage();
        for (IRoom room : rooms)
        {
            delaunayTriangulation.insertPoint(new DT_Point(room.getCenter().getBlockX(), room.getCenter().getBlockZ()));

            oldImage = createImage(delaunayTriangulation, oldImage);
            generationContext.getMapContext().offerImage(oldImage, MapConstants.DEFAULT_TICK_DELAY);
        }

        List<EdgeList.Edge> edges =  new EdgeList(delaunayTriangulation, generationContext.getForest()).getList();
        generationContext.setEdges(edges);

        generationContext.setState(new MinimumSpanningState());
        generationContext.runState();
    }

    private BufferedImage createImage(DelaunayTriangulation delaunayTriangulation, BufferedImage oldImage)
    {
        oldImage = getImage(oldImage, MapConstants.SCALE_FACTOR, MapConstants.SCALE_FACTOR);
        Graphics2D graphics2D = oldImage.createGraphics();

        graphics2D.setPaint(new Color(173, 93, 239));

        int x1, y1, x2, y2;

        Iterator<DT_Triangle> triangleIterator = delaunayTriangulation.trianglesIterator();
        while (triangleIterator.hasNext())
        {
            DT_Triangle triangle = triangleIterator.next();
            x1 = getScaledPosition((int) triangle.p1().x(), oldImage.getWidth());
            y1 = getScaledPosition((int) triangle.p1().y(), oldImage.getHeight());
            x2 = getScaledPosition((int) triangle.p2().x(), oldImage.getWidth());
            y2 = getScaledPosition((int) triangle.p2().y(), oldImage.getHeight());
            graphics2D.drawLine(x1, y1, x2, y2);

            if (!triangle.isHalfplane())
            {
                x1 = getScaledPosition((int) triangle.p2().x(), oldImage.getWidth());
                y1 = getScaledPosition((int) triangle.p2().y(), oldImage.getHeight());
                x2 = getScaledPosition((int) triangle.p3().x(), oldImage.getWidth());
                y2 = getScaledPosition((int) triangle.p3().y(), oldImage.getHeight());
                graphics2D.drawLine(x1, y1, x2, y2);

                x1 = getScaledPosition((int) triangle.p3().x(), oldImage.getWidth());
                y1 = getScaledPosition((int) triangle.p3().y(), oldImage.getHeight());
                x2 = getScaledPosition((int) triangle.p1().x(), oldImage.getWidth());
                y2 = getScaledPosition((int) triangle.p1().y(), oldImage.getHeight());
                graphics2D.drawLine(x1, y1, x2, y2);
            }
        }

        return oldImage;
    }
}
