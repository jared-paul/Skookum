package org.jared.dungeoncrawler.api.structures.nbtTest;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagFloat extends NBTNumber
{
    private float data;

    NBTTagFloat()
    {
    }

    public NBTTagFloat(float data)
    {
        this.data = data;
    }

    public void write(DataOutput output) throws IOException
    {
        output.writeFloat(this.data);
    }

    public void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(96L);
        this.data = input.readFloat();
    }

    public byte getTypeId()
    {
        return 5;
    }

    public String toString()
    {
        return this.data + "f";
    }

    public NBTTagFloat copy()
    {
        return new NBTTagFloat(this.data);
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else
        {
            return p_equals_1_ instanceof NBTTagFloat && this.data == ((NBTTagFloat) p_equals_1_).data;
        }
    }

    public int hashCode()
    {
        return Float.floatToIntBits(this.data);
    }

    public long asLong()
    {
        return (long) this.data;
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
        return this.data;
    }

    public Number getAsNumber()
    {
        return this.data;
    }

    public int floor(double var0) {
        int var2 = (int)var0;
        return var0 < (double)var2 ? var2 - 1 : var2;
    }
}
