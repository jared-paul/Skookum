package org.jared.dungeoncrawler.api.nbt;

import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class NBTTagCompound implements INBTBase
{
    private static final Pattern SIMPLE_VALUE = Pattern.compile("[A-Za-z0-9._+-]+");
    private final Map<String, INBTBase> tagMap = Maps.newHashMap();

    public void write(DataOutput output) throws IOException
    {
        Iterator var2 = this.tagMap.keySet().iterator();

        while (var2.hasNext())
        {
            String s = (String) var2.next();
            INBTBase inbtbase = (INBTBase) this.tagMap.get(s);
            writeEntry(s, inbtbase, output);
        }

        output.writeByte(0);
    }

    public void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(384L);
        if (depth > 512)
        {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        }
        else
        {
            this.tagMap.clear();

            byte b0;
            while ((b0 = readType(input, sizeTracker)) != 0)
            {
                String s = readKey(input, sizeTracker);
                sizeTracker.read((long) (224 + 16 * s.length()));
                INBTBase inbtbase = readNBT(b0, s, input, depth + 1, sizeTracker);
                if (this.tagMap.put(s, inbtbase) != null)
                {
                    sizeTracker.read(288L);
                }
            }

        }
    }

    public Set<String> keySet()
    {
        return this.tagMap.keySet();
    }

    public Map<String, INBTBase> getTagMap()
    {
        return tagMap;
    }

    public byte getTypeId()
    {
        return 10;
    }

    public int size()
    {
        return this.tagMap.size();
    }

    public void setTag(String key, INBTBase value)
    {
        if (value == null)
        {
            throw new IllegalArgumentException("Invalid null NBT value with key " + key);
        }
        else
        {
            this.tagMap.put(key, value);
        }
    }

    public void setByte(String key, byte value)
    {
        this.tagMap.put(key, new NBTTagByte(value));
    }

    public void setShort(String key, short value)
    {
        this.tagMap.put(key, new NBTTagShort(value));
    }

    public void setInt(String key, int value)
    {
        this.tagMap.put(key, new NBTTagInt(value));
    }

    public void setLong(String key, long value)
    {
        this.tagMap.put(key, new NBTTagLong(value));
    }

    public void setUniqueId(String key, UUID value)
    {
        this.setLong(key + "Most", value.getMostSignificantBits());
        this.setLong(key + "Least", value.getLeastSignificantBits());
    }

    @Nullable
    public UUID getUniqueId(String key)
    {
        return new UUID(this.getLong(key + "Most"), this.getLong(key + "Least"));
    }

    public boolean hasUniqueId(String key)
    {
        return this.contains(key + "Most", 99) && this.contains(key + "Least", 99);
    }

    public void setFloat(String key, float value)
    {
        this.tagMap.put(key, new NBTTagFloat(value));
    }

    public void setDouble(String key, double value)
    {
        this.tagMap.put(key, new NBTTagDouble(value));
    }

    public void setString(String key, String value)
    {
        this.tagMap.put(key, new NBTTagString(value));
    }

    public void setByteArray(String key, byte[] value)
    {
        this.tagMap.put(key, new NBTTagByteArray(value));
    }

    public void setIntArray(String key, int[] value)
    {
        this.tagMap.put(key, new NBTTagIntArray(value));
    }

    public void setIntArray(String key, List<Integer> value)
    {
        this.tagMap.put(key, new NBTTagIntArray(value));
    }

    public void setLongArray(String key, long[] value)
    {
        this.tagMap.put(key, new NBTTagLongArray(value));
    }

    public void setLongArray(String key, List<Long> value)
    {
        this.tagMap.put(key, new NBTTagLongArray(value));
    }

    public void setBoolean(String key, boolean value)
    {
        this.setByte(key, (byte) (value ? 1 : 0));
    }

    public INBTBase getTag(String key)
    {
        return this.tagMap.get(key);
    }

    public byte getTagId(String key)
    {
        INBTBase inbtbase = this.tagMap.get(key);
        return inbtbase == null ? 0 : inbtbase.getTypeId();
    }

    public boolean hasKey(String key)
    {
        return this.tagMap.containsKey(key);
    }

    public boolean contains(String key, int type)
    {
        int i = this.getTagId(key);
        if (i == type)
        {
            return true;
        }
        else if (type != 99)
        {
            return false;
        }
        else
        {
            return i == 1 || i == 2 || i == 3 || i == 4 || i == 5 || i == 6;
        }
    }

    public byte getByte(String key)
    {
        try
        {
            if (this.contains(key, 99))
            {
                return ((NBTNumber) this.tagMap.get(key)).asByte();
            }
        }
        catch (ClassCastException var3)
        {
        }

        return 0;
    }

    public short getShort(String key)
    {
        try
        {
            if (this.contains(key, 99))
            {
                return ((NBTNumber) this.tagMap.get(key)).asShort();
            }
        }
        catch (ClassCastException var3)
        {
        }

        return 0;
    }

    public int getInt(String key)
    {
        try
        {
            if (this.contains(key, 99))
            {
                return ((NBTNumber) this.tagMap.get(key)).asInt();
            }
        }
        catch (ClassCastException var3)
        {
        }

        return 0;
    }

    public long getLong(String key)
    {
        try
        {
            if (this.contains(key, 99))
            {
                return ((NBTNumber) this.tagMap.get(key)).asLong();
            }
        }
        catch (ClassCastException var3)
        {
        }

        return 0L;
    }

    public float getFloat(String key)
    {
        try
        {
            if (this.contains(key, 99))
            {
                return ((NBTNumber) this.tagMap.get(key)).asFloat();
            }
        }
        catch (ClassCastException var3)
        {
        }

        return 0.0F;
    }

    public double getDouble(String key)
    {
        try
        {
            if (this.contains(key, 99))
            {
                return ((NBTNumber) this.tagMap.get(key)).asDouble();
            }
        }
        catch (ClassCastException var3)
        {
        }

        return 0.0D;
    }

    public String getString(String key)
    {
        try
        {
            if (this.contains(key, 8))
            {
                return ((INBTBase) this.tagMap.get(key)).asString();
            }
        }
        catch (ClassCastException var3)
        {
        }

        return "";
    }

    public byte[] getByteArray(String key)
    {
        try
        {
            if (this.contains(key, 7))
            {
                return ((NBTTagByteArray) this.tagMap.get(key)).getByteArray();
            }
        }
        catch (ClassCastException var3)
        {
            var3.printStackTrace();
            //throw new ReportedException(this.createCrashReport(key, 7, var3));
        }

        return new byte[0];
    }

    public int[] getIntArray(String key)
    {
        try
        {
            if (this.contains(key, 11))
            {
                return ((NBTTagIntArray) this.tagMap.get(key)).getIntArray();
            }
        }
        catch (ClassCastException var3)
        {
            var3.printStackTrace();
            //throw new ReportedException(this.createCrashReport(key, 11, var3));
        }

        return new int[0];
    }

    public long[] getLongArray(String key)
    {
        try
        {
            if (this.contains(key, 12))
            {
                return ((NBTTagLongArray) this.tagMap.get(key)).getLongArray();
            }
        }
        catch (ClassCastException var3)
        {
            var3.printStackTrace();
            //throw new ReportedException(this.createCrashReport(key, 12, var3));
        }

        return new long[0];
    }

    public NBTTagCompound getCompound(String key)
    {
        try
        {
            if (this.contains(key, 10))
            {
                return (NBTTagCompound) this.tagMap.get(key);
            }
        }
        catch (ClassCastException var3)
        {
            var3.printStackTrace();
            //throw new ReportedException(this.createCrashReport(key, 10, var3));
        }

        return new NBTTagCompound();
    }

    public NBTTagList getList(String key, int type)
    {
        try
        {
            if (this.getTagId(key) == 9)
            {
                NBTTagList nbttaglist = (NBTTagList) this.tagMap.get(key);
                if (!nbttaglist.isEmpty() && nbttaglist.getTagType() != type)
                {
                    return new NBTTagList();
                }

                return nbttaglist;
            }
        }
        catch (ClassCastException var4)
        {
            var4.printStackTrace();
            //throw new ReportedException(this.createCrashReport(key, 9, var4));
        }

        return new NBTTagList();
    }

    public boolean getBoolean(String key)
    {
        return this.getByte(key) != 0;
    }

    public void removeTag(String key)
    {
        this.tagMap.remove(key);
    }

    public String toString()
    {
        StringBuilder stringbuilder = new StringBuilder("{");
        Collection collection = this.tagMap.keySet();

        String s;
        for (Iterator var5 = ((Collection) collection).iterator(); var5.hasNext(); stringbuilder.append(handleEscape(s)).append(':').append(this.tagMap.get(s)))
        {
            s = (String) var5.next();
            if (stringbuilder.length() != 1)
            {
                stringbuilder.append(',');
            }
        }

        return stringbuilder.append('}').toString();
    }

    public boolean isEmpty()
    {
        return this.tagMap.isEmpty();
    }

    /*
    private CrashReport createCrashReport(String key, int expectedType, ClassCastException ex)
    {
        CrashReport crashreport = CrashReport.makeCrashReport(ex, "Reading NBT data");
        CrashReportCategory crashreportcategory = crashreport.makeCategoryDepth("Corrupt NBT tag", 1);
        crashreportcategory.addDetail("Tag type found", () ->
        {
            return NBT_TYPES[((INBTBase) this.tagMap.get(key)).getTypeId()];
        });
        crashreportcategory.addDetail("Tag type expected", () ->
        {
            return NBT_TYPES[expectedType];
        });
        crashreportcategory.addDetail("Tag name", key);
        return crashreport;
    }
    */

    public NBTTagCompound copy()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        Iterator var2 = this.tagMap.keySet().iterator();

        while (var2.hasNext())
        {
            String s = (String) var2.next();
            nbttagcompound.setTag(s, ((INBTBase) this.tagMap.get(s)).copy());
        }

        return nbttagcompound;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else
        {
            return p_equals_1_ instanceof NBTTagCompound && Objects.equals(this.tagMap, ((NBTTagCompound) p_equals_1_).tagMap);
        }
    }

    public int hashCode()
    {
        return this.tagMap.hashCode();
    }

    private static void writeEntry(String name, INBTBase data, DataOutput output) throws IOException
    {
        output.writeByte(data.getTypeId());
        if (data.getTypeId() != 0)
        {
            output.writeUTF(name);
            data.write(output);
        }

    }

    private static byte readType(DataInput input, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(8L);
        return input.readByte();
    }

    private static String readKey(DataInput input, NBTSizeTracker sizeTracker) throws IOException
    {
        return input.readUTF();
    }

    static INBTBase readNBT(byte id, String key, DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(32L);
        INBTBase inbtbase = INBTBase.create(id);

        try
        {
            inbtbase.read(input, depth, sizeTracker);
            return inbtbase;
        }
        catch (IOException var9)
        {
            /*
            CrashReport crashreport = CrashReport.makeCrashReport(var9, "Loading NBT data");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("NBT Tag");
            crashreportcategory.addDetail("Tag name", key);
            crashreportcategory.addDetail("Tag type", id);
            throw new ReportedException(crashreport);
            */
            var9.printStackTrace();
        }

        return null;
    }

    public NBTTagCompound merge(NBTTagCompound other)
    {
        for (String s : other.tagMap.keySet())
        {
            INBTBase inbtbase = (INBTBase) other.tagMap.get(s);
            if (inbtbase.getTypeId() == 10)
            {
                if (this.contains(s, 10))
                {
                    NBTTagCompound nbttagcompound = this.getCompound(s);
                    nbttagcompound.merge((NBTTagCompound) inbtbase);
                }
                else
                {
                    this.setTag(s, inbtbase.copy());
                }
            }
            else
            {
                this.setTag(s, inbtbase.copy());
            }
        }

        return this;
    }

    protected static String handleEscape(String p_193582_0_)
    {
        return SIMPLE_VALUE.matcher(p_193582_0_).matches() ? p_193582_0_ : NBTTagString.quoteAndEscape(p_193582_0_, true);
    }
}
