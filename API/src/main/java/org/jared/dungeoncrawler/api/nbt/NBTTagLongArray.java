package org.jared.dungeoncrawler.api.nbt;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.LongSet;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NBTTagLongArray extends NBTList<NBTTagLong>
{
    private long[] data;

    NBTTagLongArray()
    {
    }

    public NBTTagLongArray(long[] value)
    {
        this.data = value;
    }

    public NBTTagLongArray(LongSet longSet)
    {
        this.data = longSet.toLongArray();
    }

    public NBTTagLongArray(List<Long> longList)
    {
        this(toArray(longList));
    }

    private static long[] toArray(List<Long> longList)
    {
        long[] along = new long[longList.size()];

        for (int i = 0; i < longList.size(); ++i)
        {
            Long olong = longList.get(i);
            along[i] = olong == null ? 0L : olong;
        }

        return along;
    }

    public void write(DataOutput output) throws IOException
    {
        output.writeInt(this.data.length);
        long[] var2 = this.data;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            long i = var2[var4];
            output.writeLong(i);
        }

    }

    public void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(192L);
        int i = input.readInt();
        sizeTracker.read((long) (64 * i));
        this.data = new long[i];

        for (int j = 0; j < i; ++j)
        {
            this.data[j] = input.readLong();
        }

    }

    public byte getTypeId()
    {
        return 12;
    }

    public String toString()
    {
        StringBuilder stringbuilder = new StringBuilder("[L;");

        for (int i = 0; i < this.data.length; ++i)
        {
            if (i != 0)
            {
                stringbuilder.append(',');
            }

            stringbuilder.append(this.data[i]).append('L');
        }

        return stringbuilder.append(']').toString();
    }

    public NBTTagLongArray copy()
    {
        long[] along = new long[this.data.length];
        System.arraycopy(this.data, 0, along, 0, this.data.length);
        return new NBTTagLongArray(along);
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else
        {
            return p_equals_1_ instanceof NBTTagLongArray && Arrays.equals(this.data, ((NBTTagLongArray) p_equals_1_).data);
        }
    }

    public int hashCode()
    {
        return Arrays.hashCode(this.data);
    }

    public long[] getLongArray()
    {
        return this.data;
    }

    public int size()
    {
        return this.data.length;
    }

    public NBTTagLong getTag(int index)
    {
        return new NBTTagLong(this.data[index]);
    }

    public void setTag(int index, INBTBase value)
    {
        this.data[index] = ((NBTNumber) value).asLong();
    }

    public void removeTag(int index)
    {
        this.data = ArrayUtils.remove(this.data, index);
    }
}
