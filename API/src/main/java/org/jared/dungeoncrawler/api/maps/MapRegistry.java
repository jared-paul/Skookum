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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.entity.Player;
import org.jared.dungeoncrawler.api.maps.util.MapImageUtil;
import org.jared.dungeoncrawler.api.maps.wrappers.DefaultMapWrapper;
import org.jared.dungeoncrawler.api.maps.wrappers.MultiMapWrapper;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Set;

public class MapRegistry
{
    private Set<Short> USED_MAP_IDS = Sets.newHashSet();
    private List<DefaultMapWrapper> MAPS = Lists.newCopyOnWriteArrayList();

    public MultiMapWrapper wrapMultiImage(ArrayImage mainImage, int rows, int columns)
    {
        return wrapMultiImage(mainImage.getOriginalBufferedImage(), rows, columns);
    }

    public MultiMapWrapper wrapMultiImage(BufferedImage mainImage, int rows, int columns)
    {
        mainImage = MapImageUtil.scaleImageToMultiMap(mainImage, rows, columns);

        DefaultMapWrapper[][] wrapperMatrix = new DefaultMapWrapper[rows][columns];

        ArrayImage[][] splitImage = MapImageUtil.splitImage(mainImage, rows, columns);
        for (int x = 0; x < rows; x++)
        {
            for (int y = 0; y < columns; y++)
            {
                wrapperMatrix[x][y] = wrapImage(splitImage[x][y]);
            }
        }

        return new MultiMapWrapper(mainImage, wrapperMatrix, rows, columns);
    }

    public DefaultMapWrapper wrapImage(BufferedImage image)
    {
        return wrapImage(new ArrayImage(image));
    }

    public DefaultMapWrapper wrapImage(ArrayImage image)
    {
        for (DefaultMapWrapper mapWrapper : MAPS)
        {
            if (image.equals(mapWrapper.getImage()))
            {
                return mapWrapper;
            }
        }

        return wrapNewImage(image);
    }

    public DefaultMapWrapper wrapNewImage(ArrayImage image)
    {
        DefaultMapWrapper mapWrapper = new DefaultMapWrapper(image);
        MAPS.add(mapWrapper);
        return mapWrapper;
    }

    public void registerMapID(short mapID)
    {
        USED_MAP_IDS.add(mapID);
    }

    public void unregisterMapID(short mapID)
    {
        USED_MAP_IDS.remove(mapID);
    }

    public short getNextFreeMapID(Player player)
    {
        Set<Short> usedIDs = getUsedIDs(player);
        usedIDs.addAll(USED_MAP_IDS);

        int largest = Integer.MIN_VALUE;
        for (short id : usedIDs)
        {
            if (id > largest)
                largest = id;
        }

        if (largest + 1 < Short.MAX_VALUE)
            return (short) (largest + 1);

        for (short id = 0; id < Short.MAX_VALUE; id++)
        {
            if (!usedIDs.contains(id))
                return id;
        }

        return -1;
    }

    public Set<Short> getUsedIDs(Player player)
    {
        Set<Short> mapIDs = Sets.newHashSet();
        for (DefaultMapWrapper mapWrapper : MAPS)
        {
            short id;
            if ((id = mapWrapper.getMapController().getMapID(player)) >= 0)
            {
                mapIDs.add(id);
            }
        }

        return mapIDs;
    }
}
