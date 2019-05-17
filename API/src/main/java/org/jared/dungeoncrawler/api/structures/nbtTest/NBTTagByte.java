package org.jared.dungeoncrawler.api.structures.nbtTest;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagByte extends NBTNumber
{
    private byte data;

    NBTTagByte()
    {
    }

    public NBTTagByte(byte data)
    {
        this.data = data;
    }

    public void write(DataOutput output) throws IOException
    {
        output.writeByte(this.data);
    }

    public void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(72L);
        this.data = input.readByte();
    }

    public byte getTypeId()
    {
        return 1;
    }

    public String toString()
    {
        return this.data + "b";
    }

    public NBTTagByte copy()
    {
        return new NBTTagByte(this.data);
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else
        {
            return p_equals_1_ instanceof NBTTagByte && this.data == ((NBTTagByte) p_equals_1_).data;
        }
    }

    public int hashCode()
    {
        return this.data;
    }

    public long asLong()
    {
        return (long) this.data;
    }

    public int asInt()
    {
        return this.data;
    }

    public short asShort()
    {
        return (short) this.data;
    }

    public byte asByte()
    {
        return this.data;
    }

    public double asDouble()
    {
        return (double) this.data;
    }

    public float asFloat()
    {
        return (float) this.data;
    }

    public Number getAsNumber()
    {
        return this.data;
    }
}
