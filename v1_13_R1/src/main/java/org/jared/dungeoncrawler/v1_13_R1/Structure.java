package org.jared.dungeoncrawler.v1_13_R1;

import com.google.common.collect.Maps;
import net.minecraft.server.v1_13_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.craftbukkit.v1_13_R1.util.CraftMagicNumbers;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.material.IMaterialAndData;
import org.jared.dungeoncrawler.api.material.MaterialAndData;
import org.jared.dungeoncrawler.api.structures.AbstractStructure;
import org.jared.dungeoncrawler.api.util.VectorUtil;

import java.util.Map;

public class Structure extends AbstractStructure
{
    public Structure(DefinedStructure definedStructure)
    {
        NBTTagCompound tag = definedStructure.a(new NBTTagCompound());

        this.dimensions = new int[]{tag.getList("size", 3).h(0), tag.getList("size", 3).h(1), tag.getList("size", 3).h(2)};

        NBTTagList states = tag.getList("palette", 10);
        NBTTagList blocks = tag.getList("blocks", 10);


        for (int i = 0; i < blocks.size(); i++)
        {
            NBTTagCompound blockTag = blocks.getCompound(i);

            Vector position = new Vector(blockTag.getList("pos", 3).h(0), blockTag.getList("pos", 3).h(1), blockTag.getList("pos", 3).h(2));

            IBlockData data = GameProfileSerializer.d(states.getCompound(blockTag.getInt("state")));
            Block block = Block.REGISTRY.get(new MinecraftKey(states.getCompound(blockTag.getInt("state")).getString("Name")));

            Material material = CraftMagicNumbers.getMaterial(block);

            if (material.name().contains("SIGN"))
            {
                this.edgeCases.add(position);
            }
            else
            {
                this.blockMap.put(position, new MaterialAndData(material, "", (byte) 0));
            }
        }
    }

    @Override
    public void place(Location base)
    {

    }

    @Override
    public void rotate(int angle)
    {
        Map<Vector, IMaterialAndData> blockMapCopy = Maps.newHashMap();

        for (Map.Entry<Vector, IMaterialAndData> blockEntry : blockMap.entrySet())
        {
            Vector vector = blockEntry.getKey();
            IMaterialAndData materialAndData = blockEntry.getValue();
            Material material = materialAndData.getMaterial();
            BlockData blockData = Bukkit.createBlockData(materialAndData.getBlockData());

            if (blockData instanceof Directional)
            {
                Directional directional = (Directional) blockData;
                Vector directionVector = VectorUtil.toVector(directional.getFacing());
                directionVector = VectorUtil.rotateVector(directionVector, angle);
                ((Directional) blockData).setFacing(VectorUtil.fromVector(directionVector));
            }

            Vector offset = VectorUtil.rotateVector(vector, angle);
            blockMapCopy.put(offset, new MaterialAndData(material, blockData.getAsString(), (byte) 0));
        }

        this.blockMap = blockMapCopy;
    }
}
