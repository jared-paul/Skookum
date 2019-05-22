package org.jared.dungeoncrawler.api.nbt;

import org.apache.commons.lang3.ArrayUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NBTTagIntArray extends NBTList<NBTTagInt>
{
    private int[] intArray;

    NBTTagIntArray()
    {
    }

    public NBTTagIntArray(int[] value)
    {
        this.intArray = value;
    }

    public NBTTagIntArray(List<Integer> integerList)
    {
        this(toArray(integerList));
    }

    private static int[] toArray(List<Integer> integerList)
    {
        int[] aint = new int[integerList.size()];

        for (int i = 0; i < integerList.size(); ++i)
        {
            Integer integer = integerList.get(i);
            aint[i] = integer == null ? 0 : integer;
        }

        return aint;
    }

    public void write(DataOutput output) throws IOException
    {
        output.writeInt(this.intArray.length);
        for (int k : this.intArray) {
            output.writeInt(k);
        }
    }

    public void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(192L);
        int i = input.readInt();
        sizeTracker.read((long) (32 * i));
        this.intArray = new int[i];

        for (int j = 0; j < i; ++j)
        {
            this.intArray[j] = input.readInt();
        }

    }

    public byte getTypeId()
    {
        return 11;
    }

    public String toString()
    {
        StringBuilder stringbuilder = new StringBuilder("[I;");

        for (int i = 0; i < this.intArray.length; ++i)
        {
            if (i != 0)
            {
                stringbuilder.append(',');
            }

            stringbuilder.append(this.intArray[i]);
        }

        return stringbuilder.append(']').toString();
    }

    public NBTTagIntArray copy()
    {
        int[] aint = new int[this.intArray.length];
        System.arraycopy(this.intArray, 0, aint, 0, this.intArray.length);
        return new NBTTagIntArray(aint);
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else
        {
            return p_equals_1_ instanceof NBTTagIntArray && Arrays.equals(this.intArray, ((NBTTagIntArray) p_equals_1_).intArray);
        }
    }

    public int hashCode()
    {
        return Arrays.hashCode(this.intArray);
    }

    public int[] getIntArray()
    {
        return this.intArray;
    }

    public int size()
    {
        return this.intArray.length;
    }

    public NBTTagInt getTag(int index)
    {
        return new NBTTagInt(this.intArray[index]);
    }

    public void setTag(int index, INBTBase value)
    {
        this.intArray[index] = ((NBTNumber) value).asInt();
    }

    public void removeTag(int p_197649_1_)
    {
        this.intArray = ArrayUtils.remove(this.intArray, p_197649_1_);
    }
}
