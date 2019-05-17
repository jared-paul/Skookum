package org.jared.dungeoncrawler.api.maps.util;

import org.jared.dungeoncrawler.api.maps.ArrayImage;
import org.jared.dungeoncrawler.api.maps.MapConstants;
import org.jared.dungeoncrawler.api.maps.colour.MapColourPalette;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MapImageUtil
{
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    public static BufferedImage resizeImage(BufferedImage image, int newWidth, int newHeight)
    {
        if (image.getWidth() == newWidth && image.getHeight() == newHeight)
        {
            return image;
        }

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_3BYTE_BGR);
        Graphics scaledGraphics = resizedImage.getGraphics();

        Image oldImageScaled = image.getScaledInstance(newWidth, newHeight, Image.SCALE_FAST);
        scaledGraphics.drawImage(oldImageScaled,0, 0, null);

        image.flush();
        oldImageScaled.flush();
        scaledGraphics.dispose();

        return resizedImage;
    }

    public static byte[] convertToBytes(Image image)
    {
        BufferedImage temp = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = temp.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();

        int[] pixels = new int[temp.getWidth() * temp.getHeight()];
        temp.getRGB(0, 0, temp.getWidth(), temp.getHeight(), pixels, 0, temp.getWidth());

        byte[] result = new byte[temp.getWidth() * temp.getHeight()];
        for (int i = 0; i < pixels.length; i++)
        {
            result[i] = MapColourPalette.getColor(new Color(pixels[i], true));
        }

        temp.flush();

        return result;
    }

    public static ArrayImage[][] splitImage(BufferedImage image, int rows, int columns)
    {
        int chunkWidth = image.getWidth() / columns;
        int chunkHeight = image.getHeight() / rows;

        ArrayImage[][] imageMatrix = new ArrayImage[rows][columns];
        for (int x = 0; x < rows; x++)
        {
            for (int y = 0; y < columns; y++)
            {
                BufferedImage rawImage = new BufferedImage(chunkWidth, chunkHeight, image.getType());

                Graphics2D graphics2D = rawImage.createGraphics();
                graphics2D.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
                graphics2D.dispose();

                imageMatrix[x][y] = new ArrayImage(rawImage);
                rawImage.flush();
            }
        }

        return imageMatrix;
    }

    public static BufferedImage scaleImageToMultiMap(BufferedImage image, int rows, int columns)
    {
        return resizeImage(image, columns * MapConstants.MAP_WIDTH, rows * MapConstants.MAP_HEIGHT);
    }
}
