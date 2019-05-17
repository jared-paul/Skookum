package org.jared.dungeoncrawler.v1_13_R2;

/*
 * Decompiled with CFR 0.143.
 */

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.PeekingIterator;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.DynamicOps;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.jared.dungeoncrawler.api.structures.nbt.*;
import org.jared.dungeoncrawler.api.structures.nbt.Tag;

public class DynamicOpsNBTea implements DynamicOps<Tag>
{
    public static final DynamicOpsNBTea a = new DynamicOpsNBTea();

    protected DynamicOpsNBTea()
    {
    }

    @Override
    public Tag empty()
    {
        return new EndTag();
    }

    @Override
    public Type<?> getType(Tag var0)
    {
        switch (var0.getTypeId())
        {
            case 0:
            {
                return DSL.nilType();
            }
            case 1:
            {
                return DSL.byteType();
            }
            case 2:
            {
                return DSL.shortType();
            }
            case 3:
            {
                return DSL.intType();
            }
            case 4:
            {
                return DSL.longType();
            }
            case 5:
            {
                return DSL.floatType();
            }
            case 6:
            {
                return DSL.doubleType();
            }
            case 7:
            {
                return DSL.list(DSL.byteType());
            }
            case 8:
            {
                return DSL.string();
            }
            case 9:
            {
                return DSL.list(DSL.remainderType());
            }
            case 10:
            {
                return DSL.compoundList(DSL.remainderType(), DSL.remainderType());
            }
            case 11:
            {
                return DSL.list(DSL.intType());
            }
            case 12:
            {
                return DSL.list(DSL.longType());
            }
        }
        return DSL.remainderType();
    }

    @Override
    public Optional<Number> getNumberValue(Tag var0)
    {
        if (var0.getValue() instanceof Number)
        {
            return Optional.of((Number) var0.getValue());
        }

        return Optional.empty();
    }

    @Override
    public Tag createNumeric(Number var0)
    {
        return new DoubleTag(var0.doubleValue());
    }

    @Override
    public Tag createByte(byte var0)
    {
        return new ByteTag(var0);
    }

    @Override
    public Tag createShort(short var0)
    {
        return new ShortTag(var0);
    }

    @Override
    public Tag createInt(int var0)
    {
        return new IntTag(var0);
    }

    @Override
    public Tag createLong(long var0)
    {
        return new LongTag(var0);
    }

    @Override
    public Tag createFloat(float var0)
    {
        return new FloatTag(var0);
    }

    @Override
    public Tag createDouble(double var0)
    {
        return new DoubleTag(var0);
    }

    @Override
    public Optional<String> getStringValue(Tag var0)
    {
        if (var0 instanceof StringTag)
        {
            return Optional.of(((StringTag) var0).getValue());
        }
        return Optional.empty();
    }

    @Override
    public Tag createString(String var0)
    {
        return new StringTag(var0);
    }

    @Override
    public Tag mergeInto(Tag var0, Tag var1)
    {
        if (var1 instanceof EndTag)
        {
            return var0;
        }
        if (var0 instanceof CompoundTag)
        {
            if (var1 instanceof CompoundTag)
            {
                CompoundTag var3 = new CompoundTag();
                CompoundTag var4 = (CompoundTag) var0;

                for (String var6 : var4.getValue().keySet())
                {
                    var3.set(var6, var4.getValue().get(var6));
                }
                CompoundTag var5 = (CompoundTag) var1;
                for (String var7 : var5.getValue().keySet())
                {
                    var3.set(var7, var5.getValue().get(var7));
                }
                return var3;
            }
            return var0;
        }
        if (var0 instanceof EndTag)
        {
            throw new IllegalArgumentException("mergeInto called with a null input.");
        }
        if (!(var0 instanceof ListTag))
        {
            return var0;
        }
        ListTag var2 = new ListTag();
        ListTag var3 = (ListTag) var0;
        var2.getValue().addAll(var3.getValue());
        var2.getValue().add(var1);
        return var2;
    }

    @Override
    public Tag mergeInto(Tag var0, Tag var1, Tag var22)
    {
        CompoundTag var3;
        if (var0 instanceof EndTag)
        {
            var3 = new CompoundTag();
        }
        else if (var0 instanceof CompoundTag)
        {
            CompoundTag var4 = (CompoundTag) var0;
            var3 = new CompoundTag();
            var4.getValue().keySet().forEach(var2 -> var3.set(var2, var4.getValue().get((String) var2)));
        }
        else
        {
            return var0;
        }

        var3.set(var1.asString(), var22);
        return var3;
    }

    @Override
    public Tag merge(Tag var0, Tag var1)
    {
        if (var0 instanceof EndTag)
        {
            return var1;
        }
        if (var1 instanceof EndTag)
        {
            return var0;
        }
        if (var0 instanceof CompoundTag && var1 instanceof CompoundTag)
        {
            CompoundTag var3 = (CompoundTag) var1;
            CompoundTag var4 = new CompoundTag();
            ((CompoundTag) var0).getValue().keySet().forEach(arg_0 -> DynamicOpsNBTea.c(var4, (CompoundTag) var0, arg_0));
            var3.getValue().keySet().forEach(var2 -> var4.set(var2, var3.getValue().get((String) var2)));
        }
        if (var0 instanceof ListTag && var1 instanceof ListTag)
        {
            ListTag var22 = new ListTag();

            var22.getValue().addAll(((ListTag) var0).getValue());
            var22.getValue().addAll(((ListTag) var1).getValue());
            return var22;
        }
        throw new IllegalArgumentException("Could not merge " + var0 + " and " + var1);
    }

    @Override
    public Optional<Map<Tag, Tag>> getMapValues(Tag var0)
    {
        if (var0 instanceof CompoundTag)
        {
            CompoundTag var12 = (CompoundTag) var0;
            return Optional.of(var12.getValue().keySet().stream().map(var1 ->
                    Pair.of(this.createString((String) var1), var12.getValue().get((String) var1))).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)));
        }
        return Optional.empty();
    }

    @Override
    public Tag createMap(Map<Tag, Tag> var0)
    {
        CompoundTag var1 = new CompoundTag();
        for (Map.Entry<Tag, Tag> var3 : var0.entrySet())
        {
            var1.set(var3.getKey().asString(), var3.getValue());
        }
        return var1;
    }

    @Override
    public Optional<Stream<Tag>> getStream(Tag var02)
    {
        if (var02 instanceof ListTag)
        {
            return Optional.of(((ListTag) var02).getValue().stream().map(var0 -> var0));
        }
        return Optional.empty();
    }

    @Override
    public Optional<ByteBuffer> getByteBuffer(Tag var0)
    {
        if (var0 instanceof ByteArrayTag)
        {
            return Optional.of(ByteBuffer.wrap(((ByteArrayTag) var0).getValue()));
        }
        return DynamicOps.super.getByteBuffer(var0);
    }

    @Override
    public Tag createByteList(ByteBuffer var0)
    {
        return new ByteArrayTag(DataFixUtils.toArray(var0));
    }

    @Override
    public Optional<IntStream> getIntStream(Tag var0)
    {
        if (var0 instanceof IntArrayTag)
        {
            return Optional.of(Arrays.stream(((IntArrayTag) var0).getValue()));
        }
        return DynamicOps.super.getIntStream(var0);
    }

    @Override
    public Tag createIntList(IntStream var0)
    {
        return new IntArrayTag(var0.toArray());
    }

    @Override
    public Optional<LongStream> getLongStream(Tag var0)
    {
        if (var0 instanceof LongArrayTag)
        {
            return Optional.of(Arrays.stream(((LongArrayTag) var0).getValue()));
        }
        return DynamicOps.super.getLongStream(var0);
    }

    @Override
    public Tag createLongList(LongStream var0)
    {
        return new LongArrayTag(var0.toArray());
    }

    @Override
    public Tag createList(Stream<Tag> var02)
    {
        PeekingIterator var1 = Iterators.peekingIterator(var02.iterator());
        if (!var1.hasNext())
        {
            return new ListTag();
        }
        Tag var2 = (Tag) var1.peek();
        if (var2 instanceof ByteTag)
        {
            List<Byte> var3 = Lists.newArrayList(Iterators.transform(var1, var0 -> ((ByteTag) var0).getValue()));
            return new ByteArrayTag(var3);
        }
        if (var2 instanceof IntTag)
        {
            ArrayList<Integer> var3 = Lists.newArrayList(Iterators.transform(var1, var0 -> ((IntTag) var0).getValue()));
            return new IntArrayTag(var3);
        }
        if (var2 instanceof LongTag)
        {
            ArrayList<Long> var3 = Lists.newArrayList(Iterators.transform(var1, var0 -> ((LongTag) var0).getValue()));
            return new LongArrayTag(var3);
        }
        ListTag var3 = new ListTag();
        while (var1.hasNext())
        {
            Tag var4 = (Tag) var1.next();
            if (var4 instanceof EndTag) continue;
            var3.getValue().add(var4);
        }
        return var3;
    }

    @Override
    public Tag remove(Tag var0, String var12)
    {
        if (var0 instanceof CompoundTag)
        {
            CompoundTag var22 = (CompoundTag) var0;
            CompoundTag var3 = new CompoundTag();
            var22.getValue().keySet().stream().filter(var1 -> !Objects.equals(var1, var12)).forEach(var2 -> var3.set((String) var2, var22.getValue().get((String) var2)));
            return var3;
        }
        return var0;
    }

    public String toString()
    {
        return "NBT";
    }

    private static /* synthetic */ void c(CompoundTag var0, CompoundTag var1, String var2)
    {
        var0.set(var2, var1.getValue().get(var2));
    }
}


