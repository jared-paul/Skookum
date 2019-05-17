package org.jared.dungeoncrawler.generation.maps;

import com.google.common.collect.Lists;
import org.bukkit.entity.Player;
import org.jared.dungeoncrawler.api.generation.maps.IMapContext;
import org.jared.dungeoncrawler.api.generation.maps.ImageInfo;
import org.jared.dungeoncrawler.api.generation.maps.MapState;
import org.jared.dungeoncrawler.api.maps.wrappers.MultiMapWrapper;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;

import java.awt.image.BufferedImage;
import java.util.Queue;

public class MapContext implements IMapContext
{
    private Queue<ImageInfo> imageQueue = Lists.newLinkedList();

    private ImageInfo previousImage;

    private int[][] itemFrameIDMatrix;
    private final int rows;
    private final int columns;

    private MapState mapState;

    public MapContext(int[][] itemFrameIDMatrix, int rows, int columns)
    {
        this.itemFrameIDMatrix = itemFrameIDMatrix;
        this.rows = rows;
        this.columns = columns;
    }

    @Override
    public int getWidth()
    {
        return columns * 128;
    }

    @Override
    public int getHeight()
    {
        return rows * 128;
    }

    @Override
    public int[][] getItemFrameIDMatrix()
    {
        return itemFrameIDMatrix;
    }

    @Override
    public ImageInfo getNextImage()
    {
        return imageQueue.poll();
    }

    @Override
    public ImageInfo getPreviousImage()
    {
        return previousImage;
    }

    @Override
    public void displayCurrentImage(Player player)
    {
        ImageInfo imageInfo = imageQueue.poll();

        if (imageInfo == null)
            return;

        MultiMapWrapper mapWrapper = imageInfo.getMapWrapper();

        if (mapWrapper == null)
            return;

        mapWrapper.getMapController().addViewer(player);
        mapWrapper.getMapController().showInFrames(player, itemFrameIDMatrix);
    }

    @Override
    public void offerImage(BufferedImage image, int nextDelayInTicks)
    {
        ImageInfo imageInfo = new ImageInfo(DungeonCrawler.getMapRegistry().wrapMultiImage(image, rows, columns), nextDelayInTicks);
        imageQueue.offer(imageInfo);
        this.previousImage = imageInfo;
    }
}
