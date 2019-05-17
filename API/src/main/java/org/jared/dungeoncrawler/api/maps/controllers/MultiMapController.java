package org.jared.dungeoncrawler.api.maps.controllers;

import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

public interface MultiMapController extends MapController
{
    void showInFrames(Player player, ItemFrame[][] frameMatrix);

    void showInFrames(Player player, int[][] itemFrameIDMatrix);

    void clearFrames(Player player, ItemFrame[][] frameMatrix);

    void clearFrames(Player player, int[][] itemFrameIDMatrix);
}
