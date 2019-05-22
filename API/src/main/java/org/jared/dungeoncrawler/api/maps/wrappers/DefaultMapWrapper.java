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

package org.jared.dungeoncrawler.api.maps.wrappers;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.jared.dungeoncrawler.api.maps.ArrayImage;
import org.jared.dungeoncrawler.api.maps.controllers.DefaultMapController;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.UUID;

public class DefaultMapWrapper implements MapWrapper
{
    private Map<UUID, Short> viewers = Maps.newHashMap();
    private ArrayImage image;

    DefaultMapController mapController = new DefaultMapController()
    {
        @Override
        public void addViewer(Player player)
        {
            if (viewers.containsKey(player.getUniqueId()))
                return;

            viewers.put(player.getUniqueId(), DungeonCrawler.getMapRegistry().getNextFreeMapID(player));
        }

        @Override
        public void removeViewer(Player player)
        {
            viewers.remove(player.getUniqueId());
        }

        @Override
        public void clearViewers()
        {
            viewers.clear();
        }

        @Override
        public boolean isViewing(Player player)
        {
            return viewers.containsKey(player.getUniqueId());
        }

        @Override
        public short getMapID(Player player)
        {
            if (viewers.containsKey(player.getUniqueId()))
                return viewers.get(player.getUniqueId());

            return -1;
        }

        @Override
        public void update(BufferedImage image)
        {
            update(new ArrayImage(image));
        }

        @Override
        public void update(ArrayImage image)
        {
            DefaultMapWrapper.this.image = image;

            for (UUID uuid : viewers.keySet())
            {
                sendImage(Bukkit.getPlayer(uuid));
            }
        }

        @Override
        public ArrayImage getImage()
        {
            return image;
        }

        @Override
        public void sendImage(Player player)
        {
            if (!isViewing(player))
                return;

            int mapID = getMapID(player);
            DungeonCrawler.getMapUtil().sendMap(mapID, image, player);
        }

        @Override
        public void showInInventory(Player player, int slot)
        {
            if (!isViewing(player))
                return;

            if (slot < 9)
            {
                slot += 36;
            }
            else if (slot > 35 && slot != 45)
            {
                slot = 8 - (slot - 36);
            }

            DungeonCrawler.getMapUtil().showInInventory(player, slot, getMapID(player));
        }

        @Override
        public void showInHand(Player player)
        {
            showInInventory(player, player.getInventory().getHeldItemSlot());
        }

        @Override
        public void showInFrame(Player player, ItemFrame frame)
        {
            showInFrame(player, frame.getEntityId());
        }

        @Override
        public void showInFrame(Player player, int itemFrameID)
        {
            if (!isViewing(player))
                return;

            DungeonCrawler.getMapUtil().showInItemFrame(player, itemFrameID, getMapID(player));
        }

        @Override
        public void clearFrame(Player player, ItemFrame frame)
        {

        }

        @Override
        public void clearFrame(Player player, int itemFrameID)
        {

        }
    };

    public DefaultMapWrapper(ArrayImage image)
    {
        this.image = image;
    }

    @Override
    public DefaultMapController getMapController()
    {
        return mapController;
    }

    public ArrayImage getImage()
    {
        return image;
    }
}
