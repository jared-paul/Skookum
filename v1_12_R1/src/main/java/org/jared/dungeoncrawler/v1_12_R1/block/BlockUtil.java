package org.jared.dungeoncrawler.v1_12_R1.block;

import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.IBlockData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.material.MaterialData;
import org.jared.dungeoncrawler.api.generation.block.AbstractBlockUtil;
import org.jared.dungeoncrawler.api.generation.block.IMaterialAndData;

public class BlockUtil extends AbstractBlockUtil
{
    @Override
    public boolean setBlockSlow(Location location, IMaterialAndData materialAndData)
    {
        Block block = location.getBlock();
        BlockState state = block.getState();
        state.setType(materialAndData.getMaterial());
        state.setData(new MaterialData(materialAndData.getBlockDataLegacy()));
        state.update(true, true);

        return true;
    }

    @Override
    public boolean setBlockFast(Location location, IMaterialAndData materialAndData)
    {
        Material material = materialAndData.getMaterial();
        byte legacyData = materialAndData.getBlockDataLegacy();

        setBlockFast(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), material.getId(), legacyData);

        return true;
    }

    private void setBlockFast(World world, int x, int y, int z, int blockId, byte data)
    {
        net.minecraft.server.v1_12_R1.World w = ((CraftWorld) world).getHandle();
        net.minecraft.server.v1_12_R1.Chunk chunk = w.getChunkAt(x >> 4, z >> 4);
        BlockPosition bp = new BlockPosition(x, y, z);
        int combined = blockId + (data << 12);
        IBlockData ibd = net.minecraft.server.v1_12_R1.Block.getByCombinedId(combined);
        chunk.a(bp, ibd);
    }
}
