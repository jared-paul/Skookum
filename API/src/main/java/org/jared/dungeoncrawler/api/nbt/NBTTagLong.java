package org.jared.dungeoncrawler.api.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagLong extends NBTNumber
{
    private long data;

    NBTTagLong()
    {
    }

    public NBTTagLong(long data)
    {
        this.data = data;
    }

    public void write(DataOutput output) throws IOException
    {
        output.writeLong(this.data);
    }

    public void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(128L);
        this.data = input.readLong();
    }

    public byte getTypeId()
    {
        return 4;
    }

    public String toString()
    {
        return this.data + "L";
    }

    public NBTTagLong copy()
    {
        return new NBTTagLong(this.data);
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else
        {
            return p_equals_1_ instanceof NBTTagLong && this.data == ((NBTTagLong) p_equals_1_).data;
        }
    }

    public int hashCode()
    {
        return (int) (this.data ^ this.data >>> 32);
    }

    public long asLong()
    {
        return this.data;
    }

    public int asInt()
    {
        return (int) (this.data & -1L);
    }

    public short asShort()
    {
        return (short) ((int) (this.data & 65535L));
    }

    public byte asByte()
    {
        return (byte) ((int) (this.data & 255L));
    }

    public double asDouble()
    {
        return (double) this.data;
    }

    public float asFloat()
    {
        return (float) this.data;
    }

    public Number asNumber()
    {
        return this.data;
    }
}
