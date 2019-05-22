package org.jared.dungeoncrawler.generation.states;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.api.generation.State;
import org.jared.dungeoncrawler.api.generation.delaunay.DisjointSetForest;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.generation.maps.MapContext;

import java.util.Collection;

public class InitializeState implements State
{
    @Override
    public int order()
    {
        return 0;
    }

    @Override
    public void run(IGenerationContext generationContext)
    {
        int[][] itemFrameIDMatrix = spawnItemFrameMatrix(
                Bukkit.getOnlinePlayers(),
                new Location(generationContext.getBaseLocation().getWorld(), 42, 82, 584),
                BlockFace.NORTH,
                6,
                6
        );

        generationContext.setMapContext(new MapContext(itemFrameIDMatrix, 6, 6));

        generationContext.setBaseVector(new Vector());
        generationContext.setForest(new DisjointSetForest<>());

        generationContext.setState(new GenerateCellsState());
        generationContext.runState();
    }

    private int[][] spawnItemFrameMatrix(Collection<? extends Player> players, Location location, BlockFace facingDirection, int rows, int columns)
    {
        Block block = location.getBlock().getRelative(facingDirection);

        int xOffset = getXOffset(facingDirection);
        int zOffset = getZOffset(facingDirection);

        int[][] itemFrameIDMatrix = new int[rows][columns];
        for (int row = 0; row < rows; row++)
        {
            for (int column = 0; column < columns; column++)
            {
                Location bukkitLocation = block.getRelative(column * xOffset, -row, column * zOffset).getLocation();

                int itemFrameID = DungeonCrawler.getMapUtil().spawnFakeItemFrame(players, bukkitLocation, facingDirection);

                itemFrameIDMatrix[row][column] = itemFrameID;
            }
        }

        return itemFrameIDMatrix;
    }

    private int getXOffset(BlockFace direction)
    {
        if (direction == BlockFace.SOUTH)
        {
            return 1;
        }
        else if (direction == BlockFace.NORTH)
        {
            return -1;
        }

        return 0;
    }

    private int getZOffset(BlockFace direction)
    {
        if (direction == BlockFace.EAST)
        {
            return -1;
        }
        else if (direction == BlockFace.WEST)
        {
            return 1;
        }

        return 0;
    }
}
