package org.jared.dungeoncrawler.v1_13_R2.nbt;

import com.mojang.datafixers.DataFixTypes;
import com.mojang.datafixers.Dynamic;
import net.minecraft.server.v1_13_R2.DynamicOpsNBT;
import net.minecraft.server.v1_13_R2.NBTBase;
import org.jared.dungeoncrawler.api.nbt.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class NBTUpdater
{
    private static final LocalDataFixer LOCAL_DATA_FIXER = new LocalDataFixer();

    /*
    public static CompoundTag updateData(CompoundTag data)
    {
        net.minecraft.server.v1_13_R2.NBTTagCompound nmsData = (net.minecraft.server.v1_13_R2.NBTTagCompound) toNMS(data);
        nmsData = (net.minecraft.server.v1_13_R2.NBTTagCompound) LOCAL_DATA_FIXER.getAttachedDataFixer().update(DataFixTypes.STRUCTURE, new Dynamic<>(DynamicOpsNBT.a, nmsData), data.getInt("DataVersion"), 1631).getValue();
        return (CompoundTag) fromNMS(nmsData);
    }

    //TODO update "toNMS" and "fromNMS" to OOP principles, might be able to use the "read" function found in all children of NBTBase by creating an extension of "DataInput"
    //The functions "toNMS" and "fromNMS" are here because we can't add these functions locally to each object
    //we could add a "toNMS" function locally, but this would require you to implement ALL of the Tags separately for each Minecraft version
    public static NBTBase toNMS(Tag base)
    {
        switch (base.getTypeId())
        {
            case 0:
                return new net.minecraft.server.v1_13_R2.NBTTagEnd();
            case 1:
                return new net.minecraft.server.v1_13_R2.NBTTagByte(((ByteTag) base).getValue());
            case 2:
                return new net.minecraft.server.v1_13_R2.NBTTagShort(((ShortTag) base).getValue());
            case 3:
                return new net.minecraft.server.v1_13_R2.NBTTagInt(((IntTag) base).getValue());
            case 4:
                return new net.minecraft.server.v1_13_R2.NBTTagLong(((LongTag) base).getValue());
            case 5:
                return new net.minecraft.server.v1_13_R2.NBTTagFloat(((FloatTag) base).getValue());
            case 6:
                return new net.minecraft.server.v1_13_R2.NBTTagDouble(((DoubleTag) base).getValue());
            case 7:
                return new net.minecraft.server.v1_13_R2.NBTTagByteArray(((ByteArrayTag) base).getValue());
            case 8:
                return new net.minecraft.server.v1_13_R2.NBTTagString(((StringTag) base).getValue());
            case 9:
                net.minecraft.server.v1_13_R2.NBTTagList tagList = new net.minecraft.server.v1_13_R2.NBTTagList();
                for (Tag tag : ((ListTag) base).getValue())
                {
                    tagList.add(toNMS(tag));
                }
                return tagList;
            case 10:
                net.minecraft.server.v1_13_R2.NBTTagCompound tagCompound = new net.minecraft.server.v1_13_R2.NBTTagCompound();
                for (Map.Entry<String, Tag> tagEntry : ((CompoundTag) base).getValue().entrySet())
                {
                    String key = tagEntry.getKey();
                    Tag tag = tagEntry.getValue();

                    tagCompound.set(key, toNMS(tag));
                }
                return tagCompound;
            case 11:
                return new net.minecraft.server.v1_13_R2.NBTTagIntArray(((IntArrayTag) base).getValue());
            case 12:
                return new net.minecraft.server.v1_13_R2.NBTTagLongArray(((LongArrayTag) base).getValue());
            default:
                return new net.minecraft.server.v1_13_R2.NBTTagEnd();
        }
    }

    public static Tag fromNMS(NBTBase base)
    {
        switch (base.getTypeId())
        {
            case 0:
                return new EndTag();
            case 1:
                return new ByteTag(((net.minecraft.server.v1_13_R2.NBTTagByte) base).asByte());
            case 2:
                return new ShortTag(((net.minecraft.server.v1_13_R2.NBTTagShort) base).asShort());
            case 3:
                return new IntTag(((net.minecraft.server.v1_13_R2.NBTTagInt) base).asInt());
            case 4:
                return new LongTag(((net.minecraft.server.v1_13_R2.NBTTagLong) base).asLong());
            case 5:
                return new FloatTag(((net.minecraft.server.v1_13_R2.NBTTagFloat) base).asFloat());
            case 6:
                return new DoubleTag(((net.minecraft.server.v1_13_R2.NBTTagDouble) base).asDouble());
            case 7:
                return new ByteArrayTag(((net.minecraft.server.v1_13_R2.NBTTagByteArray) base).c());
            case 8:
                return new StringTag(((net.minecraft.server.v1_13_R2.NBTTagString) base).asString());
            case 9:
                ListTag tagList = new ListTag();
                for (NBTBase nbtBase : getList((net.minecraft.server.v1_13_R2.NBTTagList) base))
                {
                    tagList.getValue().add(fromNMS(nbtBase));
                }
                return tagList;
            case 10:
                CompoundTag tagCompound = new CompoundTag();
                for (Map.Entry<String, NBTBase> nbtBaseEntry : getTagMap((net.minecraft.server.v1_13_R2.NBTTagCompound) base).entrySet())
                {
                    String key = nbtBaseEntry.getKey();
                    NBTBase nbtBase = nbtBaseEntry.getValue();

                    tagCompound.set(key, fromNMS(nbtBase));
                }
                return tagCompound;
            case 11:
                return new IntArrayTag(((net.minecraft.server.v1_13_R2.NBTTagIntArray) base).d());
            case 12:
                return new LongArrayTag(((net.minecraft.server.v1_13_R2.NBTTagLongArray) base).d());
            default:
                return new EndTag();
        }
    }
    */


    //* Use these methods if you want to copy Minecrafts NBT classes (fixes rotational bugs)

    public static NBTTagCompound updateData(NBTTagCompound data)
    {
        net.minecraft.server.v1_13_R2.NBTTagCompound nmsData = (net.minecraft.server.v1_13_R2.NBTTagCompound) toNMS(data);
        nmsData = (net.minecraft.server.v1_13_R2.NBTTagCompound) LOCAL_DATA_FIXER.getAttachedDataFixer().update(DataFixTypes.STRUCTURE, new Dynamic<>(DynamicOpsNBT.a, nmsData), data.getInt("DataVersion"), 1631).getValue();
        return (NBTTagCompound) fromNMS(nmsData);
    }

    public static NBTBase toNMS(INBTBase base)
    {
        switch (base.getTypeId())
        {
            case 0:
                return new net.minecraft.server.v1_13_R2.NBTTagEnd();
            case 1:
                return new net.minecraft.server.v1_13_R2.NBTTagByte(((NBTTagByte) base).asByte());
            case 2:
                return new net.minecraft.server.v1_13_R2.NBTTagShort(((NBTTagShort) base).asShort());
            case 3:
                return new net.minecraft.server.v1_13_R2.NBTTagInt(((NBTTagInt) base).asInt());
            case 4:
                return new net.minecraft.server.v1_13_R2.NBTTagLong(((NBTTagFloat) base).asLong());
            case 5:
                return new net.minecraft.server.v1_13_R2.NBTTagFloat(((NBTTagFloat) base).asFloat());
            case 6:
                return new net.minecraft.server.v1_13_R2.NBTTagDouble(((NBTTagDouble) base).asDouble());
            case 7:
                return new net.minecraft.server.v1_13_R2.NBTTagByteArray(((NBTTagByteArray) base).getByteArray());
            case 8:
                return new net.minecraft.server.v1_13_R2.NBTTagString(((NBTTagString) base).asString());
            case 9:
                net.minecraft.server.v1_13_R2.NBTTagList tagList = new net.minecraft.server.v1_13_R2.NBTTagList();
                for (INBTBase nbtBase : ((NBTTagList) base).getTagList())
                {
                    tagList.add(toNMS(nbtBase));
                }
                return tagList;
            case 10:
                net.minecraft.server.v1_13_R2.NBTTagCompound tagCompound = new net.minecraft.server.v1_13_R2.NBTTagCompound();
                for (Map.Entry<String, INBTBase> nbtBaseEntry : ((NBTTagCompound) base).getTagMap().entrySet())
                {
                    String key = nbtBaseEntry.getKey();
                    INBTBase nbtBase = nbtBaseEntry.getValue();

                    tagCompound.set(key, toNMS(nbtBase));
                }
                return tagCompound;
            case 11:
                return new net.minecraft.server.v1_13_R2.NBTTagIntArray(((NBTTagIntArray) base).getIntArray());
            case 12:
                return new net.minecraft.server.v1_13_R2.NBTTagLongArray(((NBTTagLongArray) base).getLongArray());
            default:
                return new net.minecraft.server.v1_13_R2.NBTTagEnd();
        }
    }

    public static INBTBase fromNMS(NBTBase base)
    {
        switch (base.getTypeId())
        {
            case 0:
                return new NBTTagEnd();
            case 1:
                return new NBTTagByte(((net.minecraft.server.v1_13_R2.NBTTagByte) base).asByte());
            case 2:
                return new NBTTagShort(((net.minecraft.server.v1_13_R2.NBTTagShort) base).asShort());
            case 3:
                return new NBTTagInt(((net.minecraft.server.v1_13_R2.NBTTagInt) base).asInt());
            case 4:
                return new NBTTagLong(((net.minecraft.server.v1_13_R2.NBTTagFloat) base).asLong());
            case 5:
                return new NBTTagFloat(((net.minecraft.server.v1_13_R2.NBTTagFloat) base).asFloat());
            case 6:
                return new NBTTagDouble(((net.minecraft.server.v1_13_R2.NBTTagDouble) base).asDouble());
            case 7:
                return new NBTTagByteArray(((net.minecraft.server.v1_13_R2.NBTTagByteArray) base).c());
            case 8:
                return new NBTTagString(((net.minecraft.server.v1_13_R2.NBTTagString) base).asString());
            case 9:
                NBTTagList tagList = new NBTTagList();
                for (NBTBase nbtBase : getList((net.minecraft.server.v1_13_R2.NBTTagList) base))
                {
                    tagList.add(fromNMS(nbtBase));
                }
                return tagList;
            case 10:
                NBTTagCompound tagCompound = new NBTTagCompound();
                for (Map.Entry<String, NBTBase> nbtBaseEntry : getTagMap((net.minecraft.server.v1_13_R2.NBTTagCompound) base).entrySet())
                {
                    String key = nbtBaseEntry.getKey();
                    NBTBase nbtBase = nbtBaseEntry.getValue();

                    tagCompound.setTag(key, fromNMS(nbtBase));
                }
                return tagCompound;
            case 11:
                return new NBTTagIntArray(((net.minecraft.server.v1_13_R2.NBTTagIntArray) base).d());
            case 12:
                return new NBTTagLongArray(((net.minecraft.server.v1_13_R2.NBTTagLongArray) base).d());
            default:
                return new NBTTagEnd();
        }
    }

    private static List<NBTBase> getList(net.minecraft.server.v1_13_R2.NBTTagList tagList)
    {
        try
        {
            Field listField = net.minecraft.server.v1_13_R2.NBTTagList.class.getDeclaredField("list");
            listField.setAccessible(true);

            return (List<NBTBase>) listField.get(tagList);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private static Map<String, NBTBase> getTagMap(net.minecraft.server.v1_13_R2.NBTTagCompound tagCompound)
    {
        try
        {
            Field mapField = net.minecraft.server.v1_13_R2.NBTTagCompound.class.getDeclaredField("map");
            mapField.setAccessible(true);

            return (Map<String, NBTBase>) mapField.get(tagCompound);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
