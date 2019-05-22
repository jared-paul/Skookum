package org.jared.dungeoncrawler.v1_13_R1.block;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.jared.dungeoncrawler.api.generation.block.AbstractBlockUtil;
import org.jared.dungeoncrawler.api.material.IMaterialAndData;

public class BlockUtil extends AbstractBlockUtil
{
    @Override
    public boolean setBlockFast(Location location, IMaterialAndData materialAndData)
    {
        return setBlock(location, materialAndData);
    }

    @Override
    public boolean setBlockSlow(Location location, IMaterialAndData materialAndData)
    {
        return setBlock(location, materialAndData);
    }

    public boolean setBlock(Location location, IMaterialAndData materialAndData)
    {
        Material material = materialAndData.getMaterial();
        String blockData = materialAndData.getBlockData();

        Block block = location.getBlock();
        BlockState state = block.getState();

        if (!blockData.equals(""))
        {
            BlockData data = Bukkit.getServer().createBlockData(blockData);
            state.setBlockData(data);
        }

        state.setType(material);
        state.update(true, true);

        return true;
    }
}
