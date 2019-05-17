package org.jared.dungeoncrawler.v1_13_R2.block;

import net.minecraft.server.v1_13_R2.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.block.data.CraftBlockData;
import org.jared.dungeoncrawler.api.generation.block.AbstractBlockUtil;
import org.jared.dungeoncrawler.api.generation.block.IMaterialAndData;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;

public class BlockUtil extends AbstractBlockUtil
{
    @Override
    public boolean setBlockFast(Location location, IMaterialAndData materialAndData)
    {
        return setBlockFast(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), materialAndData.getMaterial(), materialAndData.getBlockData());
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


    private boolean setBlockFast(World world, int x, int y, int z, Material material, String blockData)
    {
        DungeonCrawler.LOG.warning(blockData);
        CraftBlockData craftBlockData = CraftBlockData.newData(material, blockData);
        net.minecraft.server.v1_13_R2.World w = ((CraftWorld) world).getHandle();
        net.minecraft.server.v1_13_R2.Chunk chunk = w.getChunkAt(x >> 4, z >> 4);
        BlockPosition bp = new BlockPosition(x, y, z);
        //int combined = blockId + (data << 12);
        //IBlockData ibd = net.minecraft.server.v1_13_R2.Block.getByCombinedId(combined);
        chunk.setType(bp, craftBlockData.getState(), true);

        return true;
    }
}
