package org.jared.dungeoncrawler.api.pathfinding;

/**
 * https://bukkit.org/threads/lib-a-pathfinding-algorithm.129786/ ALL CREDIT GOES TO THIS THREAD aka Adamki11s
 */

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.*;

public class AStar
{
    private final int startX, startY, startZ, endX, endY, endZ;

    private PathingResult result;

    private Map<String, Tile> open = Maps.newHashMap();
    private Map<String, Tile> closed = Maps.newHashMap();

    private void addToOpenList(Tile t, boolean modify)
    {
        if (open.containsKey(t.getUID()))
        {
            if (modify)
            {
                open.put(t.getUID(), t);
            }
        }
        else
        {
            open.put(t.getUID(), t);
        }
    }

    private void addToClosedList(Tile t)
    {
        if (!closed.containsKey(t.getUID()))
        {
            closed.put(t.getUID(), t);
        }
    }

    private final int range;
    private final String endUID;

    public AStar(Vector start, Vector end, int range) throws InvalidPathException
    {
        this.startX = start.getBlockX();
        this.startY = start.getBlockY();
        this.startZ = start.getBlockZ();
        this.endX = end.getBlockX();
        this.endY = end.getBlockY();
        this.endZ = end.getBlockZ();

        this.range = range;

        short sh = 0;
        Tile tile = new Tile(sh, sh, sh, null);
        tile.calculateBoth(startX, startY, startZ, endX, endY, endZ, true);
        this.open.put(tile.getUID(), tile);
        this.processAdjacentTiles(tile);

        this.endUID = String.valueOf(endX - startX) + (endY - startY) + (endZ - startZ);
    }

    public PathingResult getPathingResult()
    {
        return this.result;
    }

    boolean checkOnce = false;

    public List<Tile> iterate()
    {

        if (!checkOnce)
        {
            // invert the boolean flag
            checkOnce ^= true;
            if ((Math.abs(startX - endX) > range) || (Math.abs(startY - endY) > range) || (Math.abs(startZ - endZ) > range))
            {
                this.result = PathingResult.NO_PATH;
                return null;//jump out
            }
        }
        // while not at end
        Tile current = null;

        while (canContinue())
        {

            // get lowest F cost square on open list
            current = this.getLowestFTile();

            // process tiles
            this.processAdjacentTiles(current);
        }

        if (this.result != PathingResult.SUCCESS)
        {
            return null;
        }
        else
        {
            // path found
            List<Tile> routeTrace = Lists.newLinkedList();
            Tile parent;

            routeTrace.add(current);

            while ((parent = current != null ? current.getParent() : null) != null)
            {
                routeTrace.add(parent);
                current = parent;
            }

            Collections.reverse(routeTrace);

            return Lists.newArrayList(routeTrace);
        }
    }

    private boolean canContinue()
    {
        // check if open list is empty, if it is no path has been found
        if (open.size() == 0)
        {
            this.result = PathingResult.NO_PATH;
            return false;
        }
        else
        {
            if (closed.containsKey(this.endUID))
            {
                this.result = PathingResult.SUCCESS;
                return false;
            }
            else
            {
                return true;
            }
        }
    }

    private Tile getLowestFTile()
    {
        double f = 0;
        Tile drop = null;

        // get lowest F cost square
        for (Tile t : open.values())
        {
            if (f == 0)
            {
                t.calculateBoth(startX, startY, startZ, endX, endY, endZ, true);
                f = t.getF();
                drop = t;
            }
            else
            {
                t.calculateBoth(startX, startY, startZ, endX, endY, endZ, true);
                double posF = t.getF();
                if (posF < f)
                {
                    f = posF;
                    drop = t;
                }
            }
        }

        // drop from open list and add to closed

        this.open.remove(drop != null ? drop.getUID() : null);
        this.addToClosedList(drop);

        return drop;
    }

    private boolean isOnClosedList(Tile t)
    {
        return closed.containsKey(t.getUID());
    }

    // pass in the current tile as the parent
    private void processAdjacentTiles(Tile current)
    {

        // set of possible walk to locations adjacent to current tile
        Set<Tile> possible = new HashSet<>(26);

        for (byte x = -1; x <= 1; x++)
        {
            for (byte y = -1; y <= 1; y++)
            {
                for (byte z = -1; z <= 1; z++)
                {

                    if (x == 0 && y == 0 && z == 0)
                    {
                        continue;// don't check current square
                    }

                    Tile t = new Tile((short) (current.getX() + x), (short) (current.getY() + y), (short) (current.getZ() + z), current);

                    if (!t.isInRange(this.range))
                    {
                        // if block is out of bounds continue
                        continue;
                    }

                    if (x != 0 && z != 0 && (y == 0 || y == 1))
                    {
                        // check to stop jumping through diagonal blocks
                        Tile xOff = new Tile((short) (current.getX() + x), (short) (current.getY() + y), current.getZ(), current);
                        Tile zOff = new Tile(current.getX(), (short) (current.getY() + y), (short) (current.getZ() + z), current);
                        if (!this.isTileWalkable(xOff) && !this.isTileWalkable(zOff))
                        {
                            continue;
                        }
                    }

                    if (this.isOnClosedList(t))
                    {
                        // ignore tile
                        continue;
                    }

                    // only process the tile if it can be walked on
                    if (this.isTileWalkable(t))
                    {
                        t.calculateBoth(startX, startY, startZ, endX, endY, endZ, true);

                        possible.add(t);
                    }

                }
            }
        }

        for (Tile t : possible)
        {
            // get the reference of the object in the array
            Tile openRef;
            if ((openRef = this.isOnOpenList(t)) == null)
            {
                // not on open list, so add
                this.addToOpenList(t, false);
            }
            else
            {
                // is on open list, check if path to that square is better using
                // G cost
                if (t.getG() < openRef.getG())
                {
                    // if current path is better, change parent
                    openRef.setParent(current);
                    // force updates of F, G and H values.
                    openRef.calculateBoth(startX, startY, startZ, endX, endY, endZ, true);
                }

            }
        }

    }

    private Tile isOnOpenList(Tile t)
    {
        return (open.containsKey(t.getUID()) ? open.get(t.getUID()) : null);
        /*
         * for (Tile o : open) { if (o.equals(t)) { return o; } } return null;
		 */
    }

    private boolean isTileWalkable(Tile t)
    {
        return true;

        /*
        Location l = new Location(world, (startX + t.getX()), (startY + t.getY()), (startZ + t.getZ()));

        if (l.getBlockY() != start.getBlockY())
            return false;

        Block b = l.getBlock();
        int i = b.getTypeId();

        // lava, fire, wheat and ladders cannot be walked on, and of course air
        // 85, 107 and 113 stops npcs climbing fences and fence gates
        if (i != 8 && i != 9 && i != 10 && i != 11 && i != 51 && i != 59 && i != 85 && i != 107 && i != 113)
        {
            if (!canBlockBeWalkedThrough(i) || i == 0)
            {
                // make sure the blocks above are air

                if (b.getRelative(BlockFace.UP).getTypeId() == 107)
                {
                    // fench gate check, if closed continue
                    Gate g = new Gate(b.getRelative(BlockFace.UP).getData());
                    return (g.isOpen() && (b.getRelative(0, 2, 0).getTypeId() == 0));
                }
                return (canBlockBeWalkedThrough(b.getRelative(BlockFace.UP).getTypeId()) && b.getRelative(0, 2, 0).getTypeId() == 0);

            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
        */
    }

    private boolean isLocationWalkable(Location l)
    {
        return true;

        /*
        if (l != null)
        {
            Block b = l.getBlock();
            int i = b.getTypeId();

            if (i != 8 && i != 9 && i != 10 && i != 11 && i != 51 && i != 59)
            {
                if (!canBlockBeWalkedThrough(i) || i == 0)
                {
                    // make sure the blocks above are air or can be walked through
                    return (canBlockBeWalkedThrough(b.getRelative(0, 1, 0).getTypeId()) && b.getRelative(0, 2, 0).getTypeId() == 0);
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }

        return false;
        */
    }

    private boolean canBlockBeWalkedThrough(int id)
    {
        return (id == 0 || id == 6 || id == 50 || id == 63 || id == 30 || id == 31 || id == 32 || id == 37 || id == 38 || id == 39 || id == 40 || id == 55 || id == 66 || id == 75
                || id == 76 || id == 78 || id == 83 || id == 175);
    }

    @SuppressWarnings("serial")
    public class InvalidPathException extends Exception
    {

        private final boolean s, e;

        public InvalidPathException(boolean s, boolean e)
        {
            this.s = s;
            this.e = e;
        }

        public String getErrorReason()
        {
            StringBuilder sb = new StringBuilder();
            if (!s)
            {
                sb.append("Start Location was air. ");
            }
            if (!e)
            {
                sb.append("End Location was air.");
            }
            return sb.toString();
        }

        public boolean isStartNotSolid()
        {
            return (!s);
        }

        public boolean isEndNotSolid()
        {
            return (!e);
        }
    }

}
