package org.jared.dungeoncrawler.plugin.commands;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by JPaul on 8/20/2016.
 */
public class SimpleInjector implements Injector
{
    @Override
    public Object getInstance(Class<?> clazz)
    {
        try
        {
            Constructor<?> constructor = clazz.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        } catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
