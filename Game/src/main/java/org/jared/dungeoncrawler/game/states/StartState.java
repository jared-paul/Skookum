package org.jared.dungeoncrawler.game.states;

import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.game.IGameContext;
import org.jared.dungeoncrawler.api.game.IRoom;
import org.jared.dungeoncrawler.api.game.State;
import org.jared.dungeoncrawler.api.player.IDungeonPlayer;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.api.util.VectorUtil;

import java.util.UUID;

public class StartState implements State
{
    @Override
    public void run(IGameContext gameContext)
    {
        IRoom start = gameContext.getRooms().get(0);
        IRoom end = gameContext.getRooms().get(gameContext.getRooms().size() - 1);

        for (UUID playerUUID : gameContext.getPlayers())
        {
            IDungeonPlayer dungeonPlayer = DungeonCrawler.getDungeonPlayerRegistry().getDungeonPlayer(playerUUID);
            dungeonPlayer.getPlayer().teleport(VectorUtil.toLocation(start.getCenter().clone().add(new Vector(0, 2, 0)), gameContext.getBaseLocation()));
        }
    }

}
