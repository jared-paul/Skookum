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

package org.jared.dungeoncrawler.api.maps.colour.mcsd;

import org.bukkit.map.MapPalette;
import org.jared.dungeoncrawler.api.maps.colour.MapColourSpaceData;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;

public class MCSDGenBukkit extends MapColourSpaceData
{
    public void generate()
    {
        this.clear();
        for (int i = 0; i < 256; i++)
        {
            try
            {
                setColor((byte) i, MapPalette.getColor((byte) i));
            }
            catch (Throwable ignored) {}
        }
        for (int r = 0; r < 256; r++)
        {
            for (int g = 0; g < 256; g++)
            {
                for (int b = 0; b < 256; b++)
                {
                    set(r, g, b, MapPalette.matchColor(r, g, b));
                }
            }
        }

        DungeonCrawler.LOG.warning("DONE GENERATING");
    }
}