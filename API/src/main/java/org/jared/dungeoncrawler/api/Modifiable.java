package org.jared.dungeoncrawler.api;

public interface Modifiable<T extends Modifiable<T>> extends Cloneable
{
    T clone();
}
