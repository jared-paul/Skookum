package org.jared.dungeoncrawler.v1_13_R2.testloader;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;

import java.lang.reflect.Method;

public class Test
{
    public void test() throws Exception
    {
        ClassPool classPool = ClassPool.getDefault();
        Loader loader = new Loader(classPool);

        Class clazz = loader.loadClass("net.minecraft.server.v1_13_R2.NBTTagCompound");
        Object object = clazz.newInstance();
        Method method = clazz.getDeclaredMethod("getTypeId");
        method.invoke(object);

        CtClass ctClass = classPool.get("net.minecraft.server.v1_13_R2.NBTTagCompound");
        ctClass.defrost();
        CtMethod ctMethod = ctClass.getDeclaredMethod("getTypeId");
        ctMethod.insertBefore("{ System.out.println(\"TEST\"); }");
        ctClass.writeFile(".");
        ctClass.toClass();

        NBTTagCompound nbtTagCompound = new NBTTagCompound();

        DungeonCrawler.LOG.debug(nbtTagCompound.getTypeId());
    }
}
