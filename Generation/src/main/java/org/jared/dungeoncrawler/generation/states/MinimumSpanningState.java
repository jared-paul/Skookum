package org.jared.dungeoncrawler.generation.states;

import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.api.generation.ImageState;
import org.jared.dungeoncrawler.api.generation.State;
import org.jared.dungeoncrawler.api.generation.delaunay.DT_Point;
import org.jared.dungeoncrawler.api.generation.delaunay.DT_Triangle;
import org.jared.dungeoncrawler.api.generation.delaunay.DisjointSetForest;
import org.jared.dungeoncrawler.api.generation.delaunay.EdgeList;
import org.jared.dungeoncrawler.api.maps.MapConstants;
import org.jared.dungeoncrawler.api.maps.util.MapImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class MinimumSpanningState implements State, ImageState
{
    @Override
    public int order()
    {
        return 6;
    }

    @Override
    public void run(IGenerationContext generationContext)
    {
        List<EdgeList.Edge> edges = generationContext.getEdges();
        List<EdgeList.Edge> minTree = generationContext.getMinTree();
        List<EdgeList.Edge> discardedEdges = generationContext.getDiscardedEdges();
        DisjointSetForest<DT_Point> forest = generationContext.getForest();

        BufferedImage oldImage = generationContext.getMapContext().getPreviousImage().getMapWrapper().getMainBufferedImage();
        while (edges.size() > 0)
        {
            EdgeList.Edge edge = edges.remove(edges.size() - 1);

            if (!forest.findSet(edge.getNode1()).equals(forest.findSet(edge.getNode2())))
            {
                minTree.add(edge);
                forest.union(edge.getNode1(), edge.getNode2());

                oldImage = createImage(edge, oldImage);
                generationContext.getMapContext().offerImage(oldImage, MapConstants.DEFAULT_TICK_DELAY);
            }
            else
            {
                discardedEdges.add(edge);
            }
        }

        //EdgeList.Edge discardedEdge = discardedEdges.remove(discardedEdges.size() - 3);
        //minTree.add(discardedEdge);
        //generationContext.getMapContext().offerImage(createImage(discardedEdge, oldImage, 150, 150), 4);

        //gets the last image in the queue and adds a little delay
        //I wanna make them see the beauty of the triangulation and the minimum spanning tree :D
        generationContext.getMapContext().getPreviousImage().setNextDelayInTicks(20);

        generationContext.setState(new HallwayState());
        generationContext.runState();
    }

    private BufferedImage createImage(EdgeList.Edge edge, BufferedImage oldImage)
    {
        oldImage = getImage(oldImage, MapConstants.SCALE_FACTOR, MapConstants.SCALE_FACTOR);
        Graphics2D graphics2D = oldImage.createGraphics();

        graphics2D.setPaint(new Color(255, 239, 16));
        graphics2D.setStroke(new BasicStroke(2));

        int x1, y1, x2, y2;

        x1 = getScaledPosition((int) edge.getP1().x(), oldImage.getWidth());
        y1 = getScaledPosition((int) edge.getP1().y(), oldImage.getHeight());
        x2 = getScaledPosition((int) edge.getP2().x(), oldImage.getWidth());
        y2 = getScaledPosition((int) edge.getP2().y(), oldImage.getHeight());
        graphics2D.drawLine(x1, y1, x2, y2);

        //reset stroke
        graphics2D.setStroke(new BasicStroke(1));

        return oldImage;
    }
}
