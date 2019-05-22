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

// From https://github.com/bergerhealer/BKCommonLib

package org.jared.dungeoncrawler.api.maps.colour;

import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;

import java.awt.*;
import java.util.Arrays;

public class MapColourSpaceData implements Cloneable
{
    private final Color[] colors = new Color[256];
    private final byte[] data = new byte[1 << 24];

    public MapColourSpaceData()
    {
        Arrays.fill(this.colors, new Color(0, 0, 0, 0));
    }


    public final void clearRGBData()
    {
        Arrays.fill(this.data, (byte) 0);
    }


    public final void clear()
    {
        Arrays.fill(this.colors, new Color(0, 0, 0, 0));
        Arrays.fill(this.data, (byte) 0);
    }


    public void readFrom(MapColourSpaceData data)
    {
        DungeonCrawler.LOG.debug("3");
        System.arraycopy(data.data, 0, this.data, 0, this.data.length);
        System.arraycopy(data.colors, 0, this.colors, 0, this.colors.length);
    }


    public final void setColor(byte code, Color color)
    {
        this.colors[code & 0xFF] = color;
    }

    public final Color getColor(byte code)
    {
        return this.colors[code & 0xFF];
    }

    public final void set(int r, int g, int b, byte code)
    {
        this.data[getDataIndex(r, g, b)] = code;
    }

    public final byte get(int r, int g, int b)
    {
        return this.data[getDataIndex(r, g, b)];
    }

    public final void set(int index, byte code)
    {
        this.data[index] = code;
    }

    public final byte get(int index)
    {
        return this.data[index];
    }

    @Override
    public MapColourSpaceData clone()
    {
        MapColourSpaceData clone = new MapColourSpaceData();
        System.arraycopy(this.colors, 0, clone.colors, 0, this.colors.length);
        System.arraycopy(this.data, 0, clone.data, 0, this.data.length);
        return clone;
    }

    private static final int getDataIndex(int r, int g, int b)
    {
        return (r & 0xFF) + ((g & 0xFF) << 8) + ((b & 0xFF) << 16);
    }
}