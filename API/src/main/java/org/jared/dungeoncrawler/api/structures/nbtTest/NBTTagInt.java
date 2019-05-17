package org.jared.dungeoncrawler.api.structures.nbtTest;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagInt extends NBTNumber
{
    private int data;

    NBTTagInt()
    {
    }

    public NBTTagInt(int data)
    {
        this.data = data;
    }

    public void write(DataOutput output) throws IOException
    {
        output.writeInt(this.data);
    }

    public void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(96L);
        this.data = input.readInt();
    }

    public byte getTypeId()
    {
        return 3;
    }

    public String toString()
    {
        return String.valueOf(this.data);
    }

    public NBTTagInt copy()
    {
        return new NBTTagInt(this.data);
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else
        {
            return p_equals_1_ instanceof NBTTagInt && this.data == ((NBTTagInt) p_equals_1_).data;
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
        return (short) (this.data & '\uffff');
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
