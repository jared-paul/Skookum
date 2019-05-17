package org.jared.dungeoncrawler.generation.maps;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jared.dungeoncrawler.api.generation.maps.IMapContext;
import org.jared.dungeoncrawler.api.generation.maps.ImageInfo;
import org.jared.dungeoncrawler.api.maps.wrappers.MultiMapWrapper;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;

import java.util.Iterator;
import java.util.List;

public class MapPresenter extends BukkitRunnable
{
    private IMapContext mapContext;
    private List<Player> viewers;

    public MapPresenter(IMapContext mapContext, List<Player> viewers)
    {
        this.mapContext = mapContext;
        this.viewers = viewers;
    }

    @Override
    public void run()
    {
        ImageInfo imageInfo = mapContext.getNextImage();

        if (imageInfo == null)
        {
            DungeonCrawler.LOG.warning("shit");
            return;
        }

        Iterator<Player> playerIterator = viewers.iterator();
        while (playerIterator.hasNext())
        {
            Player player = playerIterator.next();

            if (player == null)
            {
                playerIterator.remove();
                return;
            }

            displayCurrentImage(player, imageInfo.getMapWrapper());
            imageInfo.getMapWrapper().getMainBufferedImage().flush();
        }

        new MapPresenter(mapContext, viewers).runTaskLaterAsynchronously(DungeonCrawler.getPlugin(), imageInfo.getNextDelayInTicks());
    }

    private void displayCurrentImage(Player player, MultiMapWrapper mapWrapper)
    {
        mapWrapper.getMapController().addViewer(player);
        mapWrapper.getMapController().sendImage(player);
        mapWrapper.getMapController().showInFrames(player, mapContext.getItemFrameIDMatrix());
    }
}
