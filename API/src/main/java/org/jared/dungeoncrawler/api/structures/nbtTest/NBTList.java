package org.jared.dungeoncrawler.api.structures.nbtTest;

import java.util.AbstractList;

public abstract class NBTList<T extends INBTBase> extends AbstractList<T> implements INBTBase
{
    public NBTList()
    {
    }

    public abstract int size();

    @Override
    public T get(final int var0) {
        return this.getTag(var0);
    }

    @Override
    public T set(final int var0, final T var1) {
        final T var2 = this.get(var0);
        this.setTag(var0, var1);
        return var2;
    }

    public abstract T getTag(final int index);

    public abstract void setTag(final int index, final INBTBase value);

    public abstract void removeTag(final int index);
}
