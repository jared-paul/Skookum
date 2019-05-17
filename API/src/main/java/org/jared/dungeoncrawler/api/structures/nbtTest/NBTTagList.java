package org.jared.dungeoncrawler.api.structures.nbtTest;

import com.google.common.collect.Lists;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class NBTTagList extends NBTList<INBTBase>
{
    private List<INBTBase> tagList = Lists.newArrayList();
    private byte tagType = 0;

    public void write(DataOutput output) throws IOException
    {
        if (this.tagList.isEmpty())
        {
            this.tagType = 0;
        }
        else
        {
            this.tagType = (this.tagList.get(0)).getTypeId();
        }

        output.writeByte(this.tagType);
        output.writeInt(this.tagList.size());

        for (int i = 0; i < this.tagList.size(); ++i)
        {
            (this.tagList.get(i)).write(output);
        }

    }

    public void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(296L);
        if (depth > 512)
        {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        }
        else
        {
            this.tagType = input.readByte();
            int i = input.readInt();
            if (this.tagType == 0 && i > 0)
            {
                throw new RuntimeException("Missing type on ListTag");
            }
            else
            {
                sizeTracker.read(32L * (long) i);
                this.tagList = Lists.newArrayListWithCapacity(i);

                for (int j = 0; j < i; ++j)
                {
                    INBTBase inbtbase = INBTBase.create(this.tagType);
                    inbtbase.read(input, depth + 1, sizeTracker);
                    this.tagList.add(inbtbase);
                }

            }
        }
    }

    public byte getTypeId()
    {
        return 9;
    }

    public String toString()
    {
        StringBuilder stringbuilder = new StringBuilder("[");

        for (int i = 0; i < this.tagList.size(); ++i)
        {
            if (i != 0)
            {
                stringbuilder.append(',');
            }

            stringbuilder.append(this.tagList.get(i));
        }

        return stringbuilder.append(']').toString();
    }

    public boolean add(INBTBase value)
    {
        if (value.getTypeId() == 0)
        {
            //LOGGER.warn("Invalid TagEnd added to ListTag");
            return false;
        }
        else
        {
            if (this.tagType == 0)
            {
                this.tagType = value.getTypeId();
            }
            else if (this.tagType != value.getTypeId())
            {
                //LOGGER.warn("Adding mismatching tag types to tag list");
                return false;
            }

            this.tagList.add(value);
            return true;
        }
    }

    public INBTBase set(int p_set_1_, INBTBase p_set_2_)
    {
        if (p_set_2_.getTypeId() == 0)
        {
            //LOGGER.warn("Invalid TagEnd added to ListTag");
            return this.tagList.get(p_set_1_);
        }
        else if (p_set_1_ >= 0 && p_set_1_ < this.tagList.size())
        {
            if (this.tagType == 0)
            {
                this.tagType = p_set_2_.getTypeId();
            }
            else if (this.tagType != p_set_2_.getTypeId())
            {
                //LOGGER.warn("Adding mismatching tag types to tag list");
                return this.tagList.get(p_set_1_);
            }

            return this.tagList.set(p_set_1_, p_set_2_);
        }
        else
        {
            //LOGGER.warn("index out of bounds to set tag in tag list");
            return null;
        }
    }

    public INBTBase remove(int p_remove_1_)
    {
        return this.tagList.remove(p_remove_1_);
    }

    public boolean isEmpty()
    {
        return this.tagList.isEmpty();
    }

    public NBTTagCompound getCompound(int i)
    {
        if (i >= 0 && i < this.tagList.size())
        {
            INBTBase inbtbase = this.tagList.get(i);
            if (inbtbase.getTypeId() == 10)
            {
                return (NBTTagCompound) inbtbase;
            }
        }

        return new NBTTagCompound();
    }

    public NBTTagList getList(int iIn)
    {
        if (iIn >= 0 && iIn < this.tagList.size())
        {
            INBTBase inbtbase = this.tagList.get(iIn);
            if (inbtbase.getTypeId() == 9)
            {
                return (NBTTagList) inbtbase;
            }
        }

        return new NBTTagList();
    }

    public short getShort(int iIn)
    {
        if (iIn >= 0 && iIn < this.tagList.size())
        {
            INBTBase inbtbase = this.tagList.get(iIn);
            if (inbtbase.getTypeId() == 2)
            {
                return ((NBTTagShort) inbtbase).asShort();
            }
        }

        return 0;
    }

    public int getInt(int iIn)
    {
        if (iIn >= 0 && iIn < this.tagList.size())
        {
            INBTBase inbtbase = this.tagList.get(iIn);
            if (inbtbase.getTypeId() == 3)
            {
                return ((NBTTagInt) inbtbase).asInt();
            }
        }

        return 0;
    }

    public int[] getIntArray(int i)
    {
        if (i >= 0 && i < this.tagList.size())
        {
            INBTBase inbtbase = this.tagList.get(i);
            if (inbtbase.getTypeId() == 11)
            {
                return ((NBTTagIntArray) inbtbase).getIntArray();
            }
        }

        return new int[0];
    }

    public double getDouble(int i)
    {
        if (i >= 0 && i < this.tagList.size())
        {
            INBTBase inbtbase = this.tagList.get(i);
            if (inbtbase.getTypeId() == 6)
            {
                return ((NBTTagDouble) inbtbase).asDouble();
            }
        }

        return 0.0D;
    }

    public float getFloat(int i)
    {
        if (i >= 0 && i < this.tagList.size())
        {
            INBTBase inbtbase = this.tagList.get(i);
            if (inbtbase.getTypeId() == 5)
            {
                return ((NBTTagFloat) inbtbase).asFloat();
            }
        }

        return 0.0F;
    }

    public String getString(int i)
    {
        if (i >= 0 && i < this.tagList.size())
        {
            INBTBase inbtbase = this.tagList.get(i);
            return inbtbase.getTypeId() == 8 ? inbtbase.asString() : inbtbase.toString();
        }
        else
        {
            return "";
        }
    }

    public INBTBase get(int index)
    {
        return (index >= 0 && index < this.tagList.size() ? this.tagList.get(index) : new NBTTagEnd());
    }

    public int size()
    {
        return this.tagList.size();
    }

    public INBTBase getTag(int index)
    {
        return this.tagList.get(index);
    }

    public void setTag(int index, INBTBase value)
    {
        this.tagList.set(index, value);
    }

    public void removeTag(int index)
    {
        this.tagList.remove(index);
    }

    public NBTTagList copy()
    {
        NBTTagList nbttaglist = new NBTTagList();
        nbttaglist.tagType = this.tagType;
        Iterator<INBTBase> iterator = this.tagList.iterator();

        while (iterator.hasNext())
        {
            INBTBase inbtbase = iterator.next();
            INBTBase inbtbase1 = inbtbase.copy();
            nbttaglist.tagList.add(inbtbase1);
        }

        return nbttaglist;
    }

    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        else
        {
            return object instanceof NBTTagList && Objects.equals(this.tagList, ((NBTTagList) object).tagList);
        }
    }

    public int hashCode()
    {
        return this.tagList.hashCode();
    }

    public int getTagType()
    {
        return this.tagType;
    }
}
