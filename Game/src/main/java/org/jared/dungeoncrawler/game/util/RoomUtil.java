package org.jared.dungeoncrawler.game.util;

import org.jared.dungeoncrawler.api.IDungeon;
import org.jared.dungeoncrawler.api.game.IRoom;
import org.jared.dungeoncrawler.api.player.IDungeonPlayer;
import org.jared.dungeoncrawler.api.util.VectorUtil;

import java.util.List;

public class RoomUtil
{
    public static IRoom getCurrentRoom(IDungeonPlayer dungeonPlayer)
    {
        IDungeon dungeon = dungeonPlayer.getDungeon();

        if (dungeon == null)
            return null;

        List<IRoom> rooms = dungeon.getGameContext().getRooms();

        if (rooms == null)
            return null;

        for (IRoom room : rooms)
        {
            if (room.getAABB().intersects(VectorUtil.toVector(dungeonPlayer.getPlayer().getLocation(), dungeonPlayer.getDungeon().getGenerationContext().getBaseLocation())))
            {
                return room;
            }
        }

        return null;
    }
}
