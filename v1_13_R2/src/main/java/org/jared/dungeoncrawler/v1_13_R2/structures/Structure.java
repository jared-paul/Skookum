package org.jared.dungeoncrawler.v1_13_R2.structures;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.material.IMaterialAndData;
import org.jared.dungeoncrawler.api.material.MaterialAndData;
import org.jared.dungeoncrawler.api.nbt.CompressedStreamTools;
import org.jared.dungeoncrawler.api.nbt.NBTTagCompound;
import org.jared.dungeoncrawler.api.nbt.NBTTagList;
import org.jared.dungeoncrawler.api.structures.AbstractStructure;
import org.jared.dungeoncrawler.api.util.VectorUtil;
import org.jared.dungeoncrawler.v1_13_R2.nbt.NBTUpdater;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Structure extends AbstractStructure
{
    private int dimensions[];
    private List<EntityInfo> entities = Lists.newArrayList();

    private NBTDataExtractor dataExtractor;

    public Structure(File file) throws IOException
    {
        this();
        loadFromFile(file);
    }

    public Structure()
    {
        this.dataExtractor = new NBTDataExtractor();
    }

    public void place(Location base)
    {
        for (Map.Entry<Vector, IMaterialAndData> blockEntry : blockMap.entrySet())
        {
            Vector position = blockEntry.getKey();
            IMaterialAndData data = blockEntry.getValue();

            base.clone().add(position).getBlock().setBlockData(data.getMaterial().createBlockData(data.getBlockData()));
        }

        for (EntityInfo entityInfo : entities)
        {
            String entityID = entityInfo.getNBT().getString("id");
            EntityType entityType = EntityType.valueOf(entityID.replace("minecraft:", "").toUpperCase());

            base.getWorld().spawnEntity(base.clone().add(entityInfo.getBlockPosition()), entityType);
        }
    }

    public void populateData(NBTTagCompound data)
    {
        NBTTagList sizeTag = data.getList("size", 3);
        this.dimensions = new int[]{sizeTag.getInt(0), sizeTag.getInt(1), sizeTag.getInt(2)};

        NBTTagList blockTags = data.getList("blocks", 10);
        NBTTagList paletteTags;

        if (data.contains("palette", 10))
        {
            paletteTags = data.getList("palette", 10);

            populateBlockStates(paletteTags, blockTags);
        }
        else
        {
            paletteTags = data.getList("palettes", 9);

            for (int i = 0; i < paletteTags.size(); i++)
            {
                populateBlockStates(paletteTags.getList(i), blockTags);
            }
        }

        NBTTagList entities = data.getList("entities", 10);
        populateEntities(entities);
    }

    private void populateBlockStates(NBTTagList paletteTags, NBTTagList blockTags)
    {
        for (int i = 0; i < blockTags.size(); i++)
        {
            NBTTagCompound blockTag = blockTags.getCompound(i);
            NBTTagList positionTags = blockTag.getList("pos", 3);
            NBTTagCompound stateTag = paletteTags.getCompound(blockTag.getInt("state"));

            Vector position = new Vector(positionTags.getInt(0), positionTags.getInt(1), positionTags.getInt(2));
            IMaterialAndData materialAndData = dataExtractor.getBlockInfo(stateTag);

            this.blockMap.put(position, materialAndData);
        }
    }

    private void populateEntities(NBTTagList entities)
    {
        for (int i = 0; i < entities.size(); i++)
        {
            NBTTagCompound entity = entities.getCompound(i);

            NBTTagList positionTags = entity.getList("pos", 6);
            Vector position = new Vector(positionTags.getDouble(0), positionTags.getDouble(1), positionTags.getDouble(2));

            NBTTagList blockPosTags = entity.getList("blockPos", 3);
            Vector blockPosition = new Vector(blockPosTags.getInt(0), blockPosTags.getInt(1), blockPosTags.getInt(2));

            if (entity.hasKey("nbt"))
            {
                NBTTagCompound nbt = entity.getCompound("nbt");
                this.entities.add(new EntityInfo(position, blockPosition, nbt));
            }
        }
    }

//    public void populateData(NBTTagCompound data)
//    {
//        NBTTagList sizeTag = data.getList("size", 3);
//        this.dimensions = new int[]{sizeTag.getInt(0), sizeTag.getInt(1), sizeTag.getInt(2)};
//
//        NBTTagList blockTags = data.getList("blocks", 10);
//        NBTTagList paletteTags;
//
//        if (data.contains("palette", 9))
//        {
//            paletteTags = data.getList("palette", 10);
//
//            for (int i = 0; i < blockTags.size(); i++)
//            {
//                NBTTagCompound blockTag = blockTags.getCompound(i);
//                NBTTagList positionTags = blockTag.getList("pos", 3);
//                NBTTagCompound stateTag = paletteTags.getCompound(blockTag.getInt("state"));
//
//                Vector position = new Vector(positionTags.getInt(0), positionTags.getInt(1), positionTags.getInt(2));
//                NBTDataExtractor.BlockInfo blockInfo = NBTDataExtractor.getBlockInfo(stateTag);
//
//                this.blockMap.put(position, blockInfo.getData());
//            }
//        }
//    }

    public void loadFromFile(File file) throws IOException
    {
        FileInputStream fileInputStream = new FileInputStream(file);

//        NBTInputStream inputStream = new NBTInputStream(fileInputStream, true);
//        CompoundTag data = (CompoundTag) inputStream.readNamedTag().getTag();
//        data = NBTUpdater.updateData(data);
//        populateData(data);


        NBTTagCompound data = CompressedStreamTools.readCompressed(fileInputStream);
        data = NBTUpdater.updateData(data);

        populateData(data);

        //populateData(NBTUpdater.updateData(CompressedStreamTools.readCompressed(new FileInputStream(file))));
    }

    //TODO jeez this is bad, can't use iterator, produces CME exception, temp fix
    public void rotate(int angle)
    {
        Map<Vector, IMaterialAndData> blockMapCopy = Maps.newHashMap();

        for (Map.Entry<Vector, IMaterialAndData> blockEntry : blockMap.entrySet())
        {
            Vector vector = blockEntry.getKey();
            IMaterialAndData data = blockEntry.getValue();
            BlockData blockData = data.getMaterial().createBlockData(data.getBlockData());

            if (blockData instanceof Directional)
            {
                Directional directional = (Directional) blockData;
                Vector directionVector = VectorUtil.toVector(directional.getFacing());
                directionVector = VectorUtil.rotateVector(directionVector, angle);
                ((Directional) blockData).setFacing(VectorUtil.fromVector(directionVector));
            }

            Vector offset = VectorUtil.rotateVector(vector, angle);

            blockMapCopy.put(offset, new MaterialAndData(data.getMaterial(), blockData.getAsString(), (byte) 0));
        }

        this.blockMap = blockMapCopy;

//        Iterator<Vector> blockEntryIterator = blockMap.keySet().iterator();
//        while (blockEntryIterator.hasNext())
//        {
//            Vector position = blockEntryIterator.next();
//            BlockData blockData = blockMap.get(position);
//
//            if (blockData instanceof Directional)
//            {
//                Directional directional = (Directional) blockData;
//                BlockFace facing = directional.getFacing();
//
//                Vector directionVector = VectorUtil.toVector(facing);
//                directionVector = VectorUtil.rotateVector(directionVector, angle);
//                ((Directional) blockData).setFacing(VectorUtil.fromVector(directionVector));
//            }
//
//            Vector rotated = VectorUtil.rotateVector(position, angle);
//
//            this.blockMap.put(rotated, blockData);
//
//            blockEntryIterator.remove();
//        }
    }
}
