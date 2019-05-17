package org.jared.dungeoncrawler.api.structures.nbtTest;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.PeekingIterator;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.DynamicOps;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class NBTDynamicOpsTest implements DynamicOps<INBTBase>
{
    public static final NBTDynamicOpsTest a = new NBTDynamicOpsTest();

    protected NBTDynamicOpsTest() {
    }

    public INBTBase empty() {
        return new NBTTagEnd();
    }

    public Type<?> getType(INBTBase var0) {
        switch(var0.getTypeId()) {
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

    public Optional<Number> getNumberValue(INBTBase var0) {
        return var0 instanceof NBTNumber ? Optional.of(((NBTNumber)var0).getAsNumber()) : Optional.empty();
    }

    public INBTBase createNumeric(Number var0) {
        return new NBTTagDouble(var0.doubleValue());
    }

    public INBTBase createByte(byte var0) {
        return new NBTTagByte(var0);
    }

    public INBTBase createShort(short var0) {
        return new NBTTagShort(var0);
    }

    public INBTBase createInt(int var0) {
        return new NBTTagInt(var0);
    }

    public INBTBase createLong(long var0) {
        return new NBTTagLong(var0);
    }

    public INBTBase createFloat(float var0) {
        return new NBTTagFloat(var0);
    }

    public INBTBase createDouble(double var0) {
        return new NBTTagDouble(var0);
    }

    public Optional<String> getStringValue(INBTBase var0) {
        return var0 instanceof NBTTagString ? Optional.of(var0.asString()) : Optional.empty();
    }

    public INBTBase createString(String var0) {
        return new NBTTagString(var0);
    }

    public INBTBase mergeInto(INBTBase var0, INBTBase var1) {
        if (var1 instanceof NBTTagEnd) {
            return var0;
        } else if (!(var0 instanceof NBTTagCompound)) {
            if (var0 instanceof NBTTagEnd) {
                throw new IllegalArgumentException("mergeInto called with a null input.");
            } else if (var0 instanceof NBTList) {
                NBTList<INBTBase> var2 = new NBTTagList();
                NBTList<?> var3 = (NBTList)var0;
                var2.addAll(var3);
                var2.add(var1);
                return var2;
            } else {
                return var0;
            }
        } else if (!(var1 instanceof NBTTagCompound)) {
            return var0;
        } else {
            NBTTagCompound var3 = new NBTTagCompound();
            NBTTagCompound var4 = (NBTTagCompound)var0;
            Iterator var6 = var4.keySet().iterator();

            while(var6.hasNext()) {
                String var06 = (String)var6.next();
                var3.setTag(var06, var4.getTag(var06));
            }

            NBTTagCompound var5 = (NBTTagCompound)var1;
            Iterator var11 = var5.keySet().iterator();

            while(var11.hasNext()) {
                String var7 = (String)var11.next();
                var3.setTag(var7, var5.getTag(var7));
            }

            return var3;
        }
    }

    public INBTBase mergeInto(INBTBase var0, INBTBase var1, INBTBase var2) {
        NBTTagCompound var3;
        if (var0 instanceof NBTTagEnd) {
            var3 = new NBTTagCompound();
        } else {
            if (!(var0 instanceof NBTTagCompound)) {
                return var0;
            }

            NBTTagCompound var4 = (NBTTagCompound)var0;
            var3 = new NBTTagCompound();
            var4.keySet().forEach((var2x) -> {
                var3.setTag(var2x, var4.getTag(var2x));
            });
        }

        var3.setTag(var1.asString(), var2);
        return var3;
    }

    public INBTBase merge(INBTBase var0, INBTBase var1) {
        if (var0 instanceof NBTTagEnd) {
            return var1;
        } else if (var1 instanceof NBTTagEnd) {
            return var0;
        } else {
            if (var0 instanceof NBTTagCompound && var1 instanceof NBTTagCompound) {
                NBTTagCompound var2 = (NBTTagCompound)var0;
                NBTTagCompound var3 = (NBTTagCompound)var1;
                NBTTagCompound var4 = new NBTTagCompound();
                var2.keySet().forEach((var2x) -> {
                    var4.setTag(var2x, var2.getTag(var2x));
                });
                var3.keySet().forEach((var2x) -> {
                    var4.setTag(var2x, var3.getTag(var2x));
                });
            }

            if (var0 instanceof NBTList && var1 instanceof NBTList) {
                NBTTagList var2 = new NBTTagList();
                var2.addAll((NBTList)var0);
                var2.addAll((NBTList)var1);
                return var2;
            } else {
                throw new IllegalArgumentException("Could not merge " + var0 + " and " + var1);
            }
        }
    }

    public Optional<Map<INBTBase, INBTBase>> getMapValues(INBTBase var0) {
        if (var0 instanceof NBTTagCompound) {
            NBTTagCompound var1 = (NBTTagCompound)var0;
            return Optional.of(var1.keySet().stream().map((var1x) -> {
                return Pair.of(this.createString(var1x), var1.getTag(var1x));
            }).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)));
        } else {
            return Optional.empty();
        }
    }

    public INBTBase createMap(Map<INBTBase, INBTBase> var0) {
        NBTTagCompound var1 = new NBTTagCompound();
        Iterator var3 = var0.entrySet().iterator();

        while(var3.hasNext()) {
            Entry<INBTBase, INBTBase> var03 = (Entry)var3.next();
            var1.setTag(((INBTBase)var03.getKey()).asString(), (INBTBase)var03.getValue());
        }

        return var1;
    }

    public Optional<Stream<INBTBase>> getStream(INBTBase var0) {
        return var0 instanceof NBTList ? Optional.of(((NBTList)var0).stream().map((var0x) -> {
            return var0x;
        })) : Optional.empty();
    }

    public Optional<ByteBuffer> getByteBuffer(INBTBase var0) {
        return var0 instanceof NBTTagByteArray ? Optional.of(ByteBuffer.wrap(((NBTTagByteArray)var0).getByteArray())) : getByteBuffer(var0);
    }

    public INBTBase createByteList(ByteBuffer var0) {
        return new NBTTagByteArray(DataFixUtils.toArray(var0));
    }

    public Optional<IntStream> getIntStream(INBTBase var0) {
        return var0 instanceof NBTTagIntArray ? Optional.of(Arrays.stream(((NBTTagIntArray)var0).getIntArray())) : getIntStream(var0);
    }

    public INBTBase createIntList(IntStream var0) {
        return new NBTTagIntArray(var0.toArray());
    }

    public Optional<LongStream> getLongStream(INBTBase var0) {
        return var0 instanceof NBTTagLongArray ? Optional.of(Arrays.stream(((NBTTagLongArray)var0).getAsLongArray())) : getLongStream(var0);
    }

    public INBTBase createLongList(LongStream var0) {
        return new NBTTagLongArray(var0.toArray());
    }

    public INBTBase createList(Stream<INBTBase> var0) {
        PeekingIterator<INBTBase> var1 = Iterators.peekingIterator(var0.iterator());
        if (!var1.hasNext()) {
            return new NBTTagList();
        } else {
            INBTBase var2 = (INBTBase)var1.peek();
            ArrayList var3;
            if (var2 instanceof NBTTagByte) {
                var3 = Lists.newArrayList(Iterators.transform(var1, (var0x) -> {
                    return ((NBTTagByte)var0x).asByte();
                }));
                return new NBTTagByteArray(var3);
            } else if (var2 instanceof NBTTagInt) {
                var3 = Lists.newArrayList(Iterators.transform(var1, (var0x) -> {
                    return ((NBTTagInt)var0x).asInt();
                }));
                return new NBTTagIntArray(var3);
            } else if (var2 instanceof NBTTagLong) {
                var3 = Lists.newArrayList(Iterators.transform(var1, (var0x) -> {
                    return ((NBTTagLong)var0x).asLong();
                }));
                return new NBTTagLongArray(var3);
            } else {
                NBTTagList var04 = new NBTTagList();

                while(var1.hasNext()) {
                    INBTBase var4 = (INBTBase)var1.next();
                    if (!(var4 instanceof NBTTagEnd)) {
                        var04.add(var4);
                    }
                }

                return var04;
            }
        }
    }

    public INBTBase remove(INBTBase var0, String var1) {
        if (var0 instanceof NBTTagCompound) {
            NBTTagCompound var2 = (NBTTagCompound)var0;
            NBTTagCompound var3 = new NBTTagCompound();
            var2.keySet().stream().filter((var1x) -> {
                return !Objects.equals(var1x, var1);
            }).forEach((var2x) -> {
                var3.setTag(var2x, var2.getTag(var2x));
            });
            return var3;
        } else {
            return var0;
        }
    }

    public String toString() {
        return "NBT";
    }
}
