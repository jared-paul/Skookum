package org.jared.dungeoncrawler.api.maps.controllers;

import org.bukkit.entity.Player;
import org.jared.dungeoncrawler.api.maps.ArrayImage;

import java.awt.image.BufferedImage;

public interface MapController
{
    void addViewer(Player player);

    void removeViewer(Player player);

    void clearViewers();

    boolean isViewing(Player player);

    void update(BufferedImage image);

    void update(ArrayImage image);

    ArrayImage getImage();

    void sendImage(Player player);
}
