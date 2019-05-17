package org.jared.dungeoncrawler.api.maps.controllers;

import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

public interface DefaultMapController extends MapController
{
    short getMapID(Player player);

    void showInInventory(Player player, int slot);

    void showInHand(Player player);

    void showInFrame(Player player, ItemFrame frame);

    void showInFrame(Player player, int itemFrameID);

    void clearFrame(Player player, ItemFrame frame);

    void clearFrame(Player player, int itemFrameID);
}
