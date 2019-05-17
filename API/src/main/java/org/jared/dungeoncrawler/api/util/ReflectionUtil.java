package org.jared.dungeoncrawler.api.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtil
{
    public static Method getPrivateMethod(Object obj, String methodName, Class<?>... params) throws Exception
    {
        Method method = obj.getClass().getDeclaredMethod(methodName, params);
        method.setAccessible(true);
        return method;

    }

    public static Object getPrivateField(Object obj, Class<?> clazz, String fieldName) throws Exception
    {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    public static void setPrivateField(Object obj, Class<?> clazz, String fieldName, Object newValue) throws Exception
    {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, newValue);
    }
}
