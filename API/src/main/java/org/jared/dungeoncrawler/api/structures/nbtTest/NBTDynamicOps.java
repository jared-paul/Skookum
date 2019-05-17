package org.jared.dungeoncrawler.api.structures.nbtTest;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.PeekingIterator;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.DynamicOps;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class NBTDynamicOps implements DynamicOps<INBTBase>
{
    public static final NBTDynamicOps INSTANCE = new NBTDynamicOps();

    protected NBTDynamicOps()
    {

    }

    @Override
    public INBTBase empty()
    {
        return new NBTTagEnd();
    }

    @Override
    public Type getType(INBTBase p_getType_1_)
    {
        switch (p_getType_1_.getTypeId())
        {
            case 0:
                return DSL.nilType();
            case 1:
                return DSL.byteType();
            case 2:
                return DSL.shortType();
            case 3:
                return DSL.intType();
            case 4:
                return DSL.longType();
            case 5:
                return DSL.floatType();
            case 6:
                return DSL.doubleType();
            case 7:
                return DSL.list(DSL.byteType());
            case 8:
                return DSL.string();
            case 9:
                return DSL.list(DSL.remainderType());
            case 10:
                return DSL.compoundList(DSL.remainderType(), DSL.remainderType());
            case 11:
                return DSL.list(DSL.intType());
            case 12:
                return DSL.list(DSL.longType());
            default:
                return DSL.remainderType();
        }
    }

    @Override
    public Optional<Number> getNumberValue(INBTBase p_getNumberValue_1_)
    {
        return p_getNumberValue_1_ instanceof NBTNumber ? Optional.of(((NBTNumber) p_getNumberValue_1_).getAsNumber()) : Optional.empty();
    }

    @Override
    public INBTBase createNumeric(Number p_createNumeric_1_)
    {
        return new NBTTagDouble(p_createNumeric_1_.doubleValue());
    }

    @Override
    public INBTBase createByte(byte p_createByte_1_)
    {
        return new NBTTagByte(p_createByte_1_);
    }

    @Override
    public INBTBase createShort(short p_createShort_1_)
    {
        return new NBTTagShort(p_createShort_1_);
    }

    @Override
    public INBTBase createInt(int p_createInt_1_)
    {
        return new NBTTagInt(p_createInt_1_);
    }

    @Override
    public INBTBase createLong(long p_createLong_1_)
    {
        return new NBTTagLong(p_createLong_1_);
    }

    @Override
    public INBTBase createFloat(float p_createFloat_1_)
    {
        return new NBTTagFloat(p_createFloat_1_);
    }

    @Override
    public INBTBase createDouble(double p_createDouble_1_)
    {
        return new NBTTagDouble(p_createDouble_1_);
    }

    @Override
    public Optional<String> getStringValue(INBTBase p_getStringValue_1_)
    {
        return p_getStringValue_1_ instanceof NBTTagString ? Optional.of(p_getStringValue_1_.asString()) : Optional.empty();
    }

    @Override
    public INBTBase createString(String p_createString_1_)
    {
        return new NBTTagString(p_createString_1_);
    }

    @Override
    public INBTBase mergeInto(final INBTBase base, final INBTBase otherBase)
    {
        if (otherBase instanceof NBTTagEnd)
        {
            return base;
        }

        if (base instanceof NBTTagCompound)
        {

            if (otherBase instanceof NBTTagCompound)
            {
                NBTTagCompound combined = new NBTTagCompound();

                NBTTagCompound baseCompound = (NBTTagCompound) base;
                for (String key : baseCompound.keySet())
                {
                    combined.setTag(key, baseCompound.getTag(key));
                }

                NBTTagCompound otherBaseCompound = (NBTTagCompound) otherBase;
                for (String key : otherBaseCompound.keySet())
                {
                    combined.setTag(key, otherBaseCompound.getTag(key));
                }

                return combined;
            }

            return base;
        }
        else
        {
            if (base instanceof NBTTagEnd)
            {
                throw new IllegalArgumentException("mergeInto called with a null input.");
            }
            if (base instanceof NBTList)
            {
                final NBTList<INBTBase> var7 = new NBTTagList();
                final NBTList<INBTBase> var8 = (NBTList<INBTBase>) base;
                var7.addAll(var8);
                var7.add(otherBase);
                return var7;
            }

            return base;
        }
    }

    @Override
    public INBTBase mergeInto(final INBTBase base1, final INBTBase base2, final INBTBase base3)
    {
        NBTTagCompound combined;

        if (base1 instanceof NBTTagEnd)
        {
            combined = new NBTTagCompound();
        }
        else
        {
            if (!(base1 instanceof NBTTagCompound))
            {
                return base1;
            }
            else
            {
                combined = new NBTTagCompound();

                NBTTagCompound base1Compound = (NBTTagCompound) base1;

                for (String key : base1Compound.keySet())
                {
                    combined.setTag(key, base1Compound.getTag(key));
                }
            }
        }

        combined.setTag(base2.asString(), base3);
        return combined;
    }

    @Override
    public INBTBase merge(final INBTBase base, final INBTBase otherBase)
    {
        if (base instanceof NBTTagEnd)
        {
            return otherBase;
        }
        if (otherBase instanceof NBTTagEnd)
        {
            return base;
        }

        if (base instanceof NBTTagCompound && otherBase instanceof NBTTagCompound)
        {
            NBTTagCompound baseCompound = (NBTTagCompound) base;
            NBTTagCompound otherBaseCompound = (NBTTagCompound) otherBase;
            NBTTagCompound combined = new NBTTagCompound();

            for (String key : baseCompound.keySet())
            {
                combined.setTag(key, baseCompound.getTag(key));
            }

            for (String key : otherBaseCompound.keySet())
            {
                combined.setTag(key, otherBaseCompound.getTag(key));
            }
        }
        if (base instanceof NBTList && otherBase instanceof NBTList)
        {
            NBTTagList combined = new NBTTagList();
            combined.addAll((NBTList) base);
            combined.addAll((NBTList) otherBase);

            return combined;
        }

        DungeonCrawler.LOG.warning("COULD NOT MERGE TAGS");
        return null;
    }

    @Override
    public Optional<Map<INBTBase, INBTBase>> getMapValues(INBTBase p_getMapValues_1_)
    {
        if (p_getMapValues_1_ instanceof NBTTagCompound)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound) p_getMapValues_1_;
            return Optional.of(nbttagcompound.keySet().stream().map(var1 ->
                    Pair.of(this.createString(var1), nbttagcompound.getTag(var1))).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)));
        }
        else
        {
            return Optional.empty();
        }
    }

    @Override
    public INBTBase createMap(Map p_createMap_1_)
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        Iterator var3 = p_createMap_1_.entrySet().iterator();

        while (var3.hasNext())
        {
            Entry entry = (Entry) var3.next();
            nbttagcompound.setTag(((INBTBase) entry.getKey()).asString(), (INBTBase) entry.getValue());
        }

        return nbttagcompound;
    }

    @Override
    public Optional<Stream<INBTBase>> getStream(INBTBase p_getStream_1_)
    {
        return p_getStream_1_ instanceof NBTList ? Optional.of(((NBTList) p_getStream_1_).stream().map((p_210817_0_) ->
        {
            return p_210817_0_;
        })) : Optional.empty();
    }

    @Override
    public Optional<ByteBuffer> getByteBuffer(INBTBase p_getByteBuffer_1_)
    {
        return p_getByteBuffer_1_ instanceof NBTTagByteArray ? Optional.of(ByteBuffer.wrap(((NBTTagByteArray) p_getByteBuffer_1_).getByteArray())) : getByteBuffer(p_getByteBuffer_1_);
    }

    @Override
    public INBTBase createByteList(ByteBuffer p_createByteList_1_)
    {
        return new NBTTagByteArray(DataFixUtils.toArray(p_createByteList_1_));
    }

    @Override
    public Optional<IntStream> getIntStream(INBTBase p_getIntStream_1_)
    {
        return p_getIntStream_1_ instanceof NBTTagIntArray ? Optional.of(Arrays.stream(((NBTTagIntArray) p_getIntStream_1_).getIntArray())) : getIntStream(p_getIntStream_1_);
    }

    @Override
    public INBTBase createIntList(IntStream p_createIntList_1_)
    {
        return new NBTTagIntArray(p_createIntList_1_.toArray());
    }

    @Override
    public Optional<LongStream> getLongStream(INBTBase p_getLongStream_1_)
    {
        return p_getLongStream_1_ instanceof NBTTagLongArray ? Optional.of(Arrays.stream(((NBTTagLongArray) p_getLongStream_1_).getAsLongArray())) : getLongStream(p_getLongStream_1_);
    }

    @Override
    public INBTBase createLongList(LongStream p_createLongList_1_)
    {
        return new NBTTagLongArray(p_createLongList_1_.toArray());
    }

    @Override
    public INBTBase createList(Stream<INBTBase> p_createList_1_)
    {
        PeekingIterator<INBTBase> peekingiterator = Iterators.peekingIterator(p_createList_1_.iterator());
        if (!peekingiterator.hasNext())
        {
            return new NBTTagList();
        }

        INBTBase inbtbase = peekingiterator.peek();
        ArrayList arraylist;
        if (inbtbase instanceof NBTTagByte)
        {
            arraylist = Lists.newArrayList(Iterators.transform(peekingiterator, (p_210815_0_) ->
            {
                return ((NBTTagByte) p_210815_0_).asByte();
            }));
            return new NBTTagByteArray(arraylist);
        }

        if (inbtbase instanceof NBTTagInt)
        {
            arraylist = Lists.newArrayList(Iterators.transform(peekingiterator, (p_210818_0_) ->
            {
                return ((NBTTagInt) p_210818_0_).asInt();
            }));
            return new NBTTagIntArray(arraylist);
        }

        if (inbtbase instanceof NBTTagLong)
        {
            arraylist = Lists.newArrayList(Iterators.transform(peekingiterator, (p_210816_0_) ->
            {
                return ((NBTTagLong) p_210816_0_).asLong();
            }));
            return new NBTTagLongArray(arraylist);
        }

        NBTTagList nbttaglist = new NBTTagList();

        while (peekingiterator.hasNext())
        {
            INBTBase inbtbase1 = peekingiterator.next();
            if (!(inbtbase1 instanceof NBTTagEnd))
            {
                nbttaglist.add(inbtbase1);
            }
        }

        return nbttaglist;
    }

    public INBTBase remove(INBTBase p_remove_1_, String p_remove_2_)
    {
        if (p_remove_1_ instanceof NBTTagCompound)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound) p_remove_1_;
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound.keySet().stream().filter((p_212019_1_) ->
            {
                return !Objects.equals(p_212019_1_, p_remove_2_);
            }).forEach((p_212010_2_) ->
            {
                nbttagcompound1.setTag((String) p_212010_2_, nbttagcompound.getTag((String) p_212010_2_));
            });
            return nbttagcompound1;
        }

        return p_remove_1_;
    }

    public String toString()
    {
        return "NBT";
    }
}
