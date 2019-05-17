package org.jared.dungeoncrawler.api.maps;

import org.jared.dungeoncrawler.api.maps.colour.MapColourPalette;
import org.jared.dungeoncrawler.api.maps.util.MapImageUtil;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class ArrayImage
{
    public byte[] data;

    private int width;
    private int height;

    private int imageType = BufferedImage.TYPE_3BYTE_BGR;

    public int minX = 0;
    public int minY = 0;
    public int maxX = MapConstants.MAP_WIDTH;
    public int maxY = MapConstants.MAP_HEIGHT;

    private BufferedImage originalBufferedImage;

    public ArrayImage(BufferedImage image)
    {
        this.originalBufferedImage = image;
        this.imageType = image.getType();

        image = MapImageUtil.resizeImage(image, maxX, maxY);

        this.width = image.getWidth();
        this.height = image.getHeight();

        this.data = MapImageUtil.convertToBytes(image);
    }

    public ArrayImage(byte[] data)
    {
        this.data = data;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public BufferedImage getOriginalBufferedImage()
    {
        return originalBufferedImage;
    }

    public BufferedImage toBufferedImage()
    {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), this.imageType);
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                image.setRGB(x, y, MapColourPalette.getRealColor(data[y * getWidth() + x]).getRGB());
            }
        }
        return image;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;

        if (obj == null || obj.getClass() != getClass())
            return false;

        ArrayImage image = (ArrayImage) obj;

        if (image.getWidth() != width || image.getHeight() != height)
            return false;

        return Arrays.equals(data, image.data);
    }

    @Override
    public int hashCode()
    {
        int result = data != null ? Arrays.hashCode(data) : 0;
        result = 31 * result + width;
        result = 31 * result + height;
        return result;
    }
}
