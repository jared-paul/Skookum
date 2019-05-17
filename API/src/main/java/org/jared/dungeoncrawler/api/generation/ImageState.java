package org.jared.dungeoncrawler.api.generation;

import org.jared.dungeoncrawler.api.maps.util.MapImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface ImageState
{
    default int getScaledPosition(int positionComponent, int imageComponentSize)
    {
        return positionComponent + (imageComponentSize / 2);
    }

    default BufferedImage getImage(BufferedImage oldImage, int width, int height)
    {
        if (oldImage == null)
        {
            oldImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D graphics2D = oldImage.createGraphics();
            graphics2D.setColor(Color.BLACK);
            graphics2D.fillRect(0, 0, width, height);
        }
        else
        {
            oldImage = MapImageUtil.resizeImage(oldImage, width, height);
        }

        return oldImage;
    }
}
