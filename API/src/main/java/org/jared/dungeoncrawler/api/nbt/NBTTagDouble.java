package org.jared.dungeoncrawler.api.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagDouble extends NBTNumber
{
    private double data;

    NBTTagDouble()
    {
    }

    public NBTTagDouble(double data)
    {
        this.data = data;
    }

    public void write(DataOutput output) throws IOException
    {
        output.writeDouble(this.data);
    }

    public void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(128L);
        this.data = input.readDouble();
    }

    public byte getTypeId()
    {
        return 6;
    }

    public String toString()
    {
        return this.data + "d";
    }

    public NBTTagDouble copy()
    {
        return new NBTTagDouble(this.data);
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else
        {
            return p_equals_1_ instanceof NBTTagDouble && this.data == ((NBTTagDouble) p_equals_1_).data;
        }
    }

    public int hashCode()
    {
        long i = Double.doubleToLongBits(this.data);
        return (int) (i ^ i >>> 32);
    }

    public long asLong()
    {
        return (long) Math.floor(this.data);
    }

    public int asInt()
    {
        return floor(this.data);
    }

    public short asShort()
    {
        return (short) (floor(this.data) & '\uffff');
    }

    public byte asByte()
    {
        return (byte) (floor(this.data) & 255);
    }

    public double asDouble()
    {
        return this.data;
    }

    public float asFloat()
    {
        return (float) this.data;
    }

    public Number asNumber()
    {
        return this.data;
    }

    public int floor(double var0)
    {
        int var2 = (int) var0;
        return var0 < (double) var2 ? var2 - 1 : var2;
    }
}
