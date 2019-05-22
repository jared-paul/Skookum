package org.jared.dungeoncrawler.api.structures;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.concurrency.Callback;
import org.jared.dungeoncrawler.api.data.Tuple;
import org.jared.dungeoncrawler.api.generation.block.BlockPlaceTask;
import org.jared.dungeoncrawler.api.generation.block.IBlockPlacer;
import org.jared.dungeoncrawler.api.material.IMaterialAndData;
import org.jared.dungeoncrawler.api.material.MaterialAndData;
import org.jared.dungeoncrawler.api.nbt.INBTBase;
import org.jared.dungeoncrawler.api.nbt.NBTTagCompound;
import org.jared.dungeoncrawler.api.nbt.NBTTagString;
import org.jared.dungeoncrawler.api.util.VectorUtil;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class AbstractStructure implements IStructure
{
    private String name;
    protected File file;
    protected int[] dimensions;
    protected Map<Vector, IMaterialAndData> blockMap = Maps.newHashMap();
    protected List<Vector> edgeCases = Lists.newArrayList();

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public File getFile()
    {
        return file;
    }

    @Override
    public void setFile(File file)
    {
        this.file = file;
    }

    @Override
    public int getVolume()
    {
        int width = dimensions[0];
        int height = dimensions[1];
        int depth = dimensions[2];
        return width * height * depth;
    }

    @Override
    public AABB getTestAABB(Vector position, int angle)
    {
        int maxX = getWidth() - 1;
        int maxZ = getDepth() - 1;
        int maxY = getHeight() - 1;

        AABB aabb = new AABB(0, 0, 0, maxX, maxY, maxZ);

        return aabb.rotateAlongY(position, angle);
    }

    @Override
    public int getWidth()
    {
        return dimensions[0];
    }

    @Override
    public int getHeight()
    {
        return dimensions[1];
    }

    @Override
    public int getDepth()
    {
        return dimensions[2];
    }

    @Override
    public int[] getDimensions()
    {
        return dimensions;
    }

    @Override
    public void place(Location base, IBlockPlacer blockPlacer, Callback<Boolean> callback)
    {
        List<Tuple<Vector, IMaterialAndData>> blockData = Lists.newArrayList();

        for (Map.Entry<Vector, IMaterialAndData> blockEntry : blockMap.entrySet())
        {
            Vector position = blockEntry.getKey();
            IMaterialAndData materialAndData = blockEntry.getValue();

            blockData.add(new Tuple<>(position, materialAndData));
        }

        blockPlacer.addTask(new BlockPlaceTask(base, blockData, callback));
    }

    @Override
    public List<Vector> getRotatedEdgeCases(Vector base, int angle)
    {
        List<Vector> newEdges = Lists.newArrayList();

        for (Vector edgeCase : edgeCases)
        {
            Vector newEdge = VectorUtil.rotateVector(edgeCase, angle);
            newEdges.add(base.clone().add(newEdge));
        }

        return newEdges;
    }

    @Override
    public Map<Vector, IMaterialAndData> getBlockMap()
    {
        return blockMap;
    }

    @Override
    public Map<Vector, IMaterialAndData> getPoints(Vector testPoint)
    {
        Map<Vector, IMaterialAndData> testPoints = Maps.newHashMap();

        for (Map.Entry<Vector, IMaterialAndData> pointEntry : blockMap.entrySet())
        {
            Vector vector = pointEntry.getKey();
            IMaterialAndData materialAndData = pointEntry.getValue();

            testPoints.put(testPoint.clone().add(vector), materialAndData);
        }

        return testPoints;
    }

    @Override
    public IStructure clone()
    {
        try
        {
            return (IStructure) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    protected class EntityInfo
    {
        private Vector position;
        private Vector blockPosition;
        private NBTTagCompound nbt;

        public EntityInfo(Vector position, Vector blockPosition, NBTTagCompound nbt)
        {
            this.position = position;
            this.blockPosition = blockPosition;
            this.nbt = nbt;
        }

        public Vector getPosition()
        {
            return position;
        }

        public Vector getBlockPosition()
        {
            return blockPosition;
        }

        public NBTTagCompound getNBT()
        {
            return nbt;
        }
    }

    //TODO probably needs to be per version
    public class NBTDataExtractor
    {
        public MaterialAndData getBlockInfo(NBTTagCompound data)
        {
            MaterialAndData blockInfo;

            if (!data.hasKey("Name"))
            {
                blockInfo = new MaterialAndData(Material.AIR, "", (byte) 0);
            }
            else
            {
                String materialName = data.getString("Name").replace("minecraft:", "").toUpperCase();
                Material material = Material.getMaterial(materialName);

                blockInfo = new MaterialAndData(material, "", (byte) 0);

                if (data.hasKey("Properties"))
                {
                    NBTTagCompound propertyTag = data.getCompound("Properties");

                    String blockDataString = toBlockData(propertyTag.getTagMap());

                    blockInfo = new MaterialAndData(material, blockDataString, (byte) 0);
                }
            }

            return blockInfo;
        }

        private String toBlockData(Map<String, INBTBase> properties)
        {
            StringBuilder stringBuilder = new StringBuilder("[");

            Iterator<Map.Entry<String, INBTBase>> entryIterator = properties.entrySet().iterator();
            while (entryIterator.hasNext())
            {
                Map.Entry<String, INBTBase> entry = entryIterator.next();
                String key = entry.getKey();
                NBTTagString value = (NBTTagString) entry.getValue();

                stringBuilder.append(key).append("=").append(value.asString());

                if (entryIterator.hasNext())
                {
                    stringBuilder.append(",");
                }
            }

            return stringBuilder.append("]").toString();
        }


//    public static BlockInfo getBlockInfo(NBTTagCompound data)
//    {
//        BlockInfo blockInfo;
//
//        if (!data.contains("Name", 8))
//        {
//            blockInfo = new BlockInfo(Material.AIR, Material.AIR.createBlockData());
//        }
//        else
//        {
//            String materialName = data.getString("Name").replace("minecraft:", "").toUpperCase();
//            Material material = Material.getMaterial(materialName);
//
//            blockInfo = new BlockInfo(material, material.createBlockData());
//
//            if (data.contains("Properties", 10))
//            {
//                NBTTagCompound propertyTag = data.getCompound("Properties");
//
//                String blockDataString = toBlockData(propertyTag.getTagMap());
//
//                blockInfo = new BlockInfo(material, material.createBlockData(blockDataString));
//            }
//        }
//
//        return blockInfo;
//    }
//
//    private static String toBlockData(Map<String, INBTBase> properties)
//    {
//        StringBuilder stringBuilder = new StringBuilder("[");
//
//        Iterator<Map.Entry<String, INBTBase>> entryIterator = properties.entrySet().iterator();
//        while (entryIterator.hasNext())
//        {
//            Map.Entry<String, INBTBase> entry = entryIterator.next();
//            String key = entry.getKey();
//            NBTTagString value = (NBTTagString) entry.getValue();
//
//            stringBuilder.append(key).append("=").append(value.asString());
//
//            if (entryIterator.hasNext())
//            {
//                stringBuilder.append(",");
//            }
//        }
//
//        return stringBuilder.append("]").toString();
//    }

    }
}

