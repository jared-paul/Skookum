package org.jared.dungeoncrawler.api.structures.nbtTest;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagShort extends NBTNumber
{
    private short data;

    public NBTTagShort()
    {
    }

    public NBTTagShort(short data)
    {
        this.data = data;
    }

    public void write(DataOutput output) throws IOException
    {
        output.writeShort(this.data);
    }

    public void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(80L);
        this.data = input.readShort();
    }

    public byte getTypeId()
    {
        return 2;
    }

    public String toString()
    {
        return this.data + "s";
    }

    public NBTTagShort copy()
    {
        return new NBTTagShort(this.data);
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else
        {
            return p_equals_1_ instanceof NBTTagShort && this.data == ((NBTTagShort) p_equals_1_).data;
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
        return this.data;
    }

    public byte asByte()
    {
        return (byte) (this.data & 255);
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
