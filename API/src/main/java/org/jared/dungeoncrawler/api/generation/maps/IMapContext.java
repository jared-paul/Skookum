package org.jared.dungeoncrawler.api.generation.maps;

import org.bukkit.entity.Player;

import java.awt.image.BufferedImage;

public interface IMapContext
{
    int getWidth();

    int getHeight();

    int[][] getItemFrameIDMatrix();

    ImageInfo getNextImage();

    ImageInfo getPreviousImage();

    void displayCurrentImage(Player player);

    void offerImage(BufferedImage image, int delayInTicks);
}
