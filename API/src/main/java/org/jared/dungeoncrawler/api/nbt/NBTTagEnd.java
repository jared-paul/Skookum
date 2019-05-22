package org.jared.dungeoncrawler.api.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagEnd implements INBTBase
{
    public void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(64L);
    }

    public void write(DataOutput output) throws IOException
    {
    }

    public byte getTypeId()
    {
        return 0;
    }

    public String toString()
    {
        return "END";
    }

    public NBTTagEnd copy()
    {
        return new NBTTagEnd();
    }

    public boolean equals(Object p_equals_1_)
    {
        return p_equals_1_ instanceof NBTTagEnd;
    }

    public int hashCode()
    {
        return this.getTypeId();
    }
}
