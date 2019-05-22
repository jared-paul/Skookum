package org.jared.dungeoncrawler.api.nbt;

import org.apache.commons.lang3.ArrayUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NBTTagByteArray extends NBTList<NBTTagByte>
{
    private byte[] data;

    NBTTagByteArray()
    {
    }

    public NBTTagByteArray(byte[] data)
    {
        this.data = data;
    }

    public NBTTagByteArray(List<Byte> byteList)
    {
        this(toArray(byteList));
    }

    private static byte[] toArray(List<Byte> byteList)
    {
        byte[] abyte = new byte[byteList.size()];

        for (int i = 0; i < byteList.size(); ++i)
        {
            Byte obyte = byteList.get(i);
            abyte[i] = obyte == null ? 0 : obyte;
        }

        return abyte;
    }

    public void write(DataOutput output) throws IOException
    {
        output.writeInt(this.data.length);
        output.write(this.data);
    }

    public void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(192L);
        int i = input.readInt();
        sizeTracker.read((long) (8 * i));
        this.data = new byte[i];
        input.readFully(this.data);
    }

    public byte getTypeId()
    {
        return 7;
    }

    public String toString()
    {
        StringBuilder stringbuilder = new StringBuilder("[B;");

        for (int i = 0; i < this.data.length; ++i)
        {
            if (i != 0)
            {
                stringbuilder.append(',');
            }

            stringbuilder.append(this.data[i]).append('B');
        }

        return stringbuilder.append(']').toString();
    }

    public INBTBase copy()
    {
        byte[] abyte = new byte[this.data.length];
        System.arraycopy(this.data, 0, abyte, 0, this.data.length);
        return new NBTTagByteArray(abyte);
    }

    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        else
        {
            return object instanceof NBTTagByteArray && Arrays.equals(this.data, ((NBTTagByteArray) object).data);
        }
    }

    public int hashCode()
    {
        return Arrays.hashCode(this.data);
    }

    public byte[] getByteArray()
    {
        return this.data;
    }

    @Override
    public int size()
    {
        return this.data.length;
    }

    @Override
    public NBTTagByte getTag(int index)
    {
        return new NBTTagByte(this.data[index]);
    }

    @Override
    public void setTag(int index, INBTBase value)
    {
        this.data[index] = ((NBTNumber) value).asByte();
    }

    @Override
    public void removeTag(int index)
    {
        this.data = ArrayUtils.remove(this.data, index);
    }
}
