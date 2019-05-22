/*
 MIT License

 Copyright (c) inventivetalent

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
*/

//From https://github.com/InventivetalentDev/MapManager

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
