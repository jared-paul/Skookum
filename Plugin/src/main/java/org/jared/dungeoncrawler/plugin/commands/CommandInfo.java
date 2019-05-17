package org.jared.dungeoncrawler.plugin.commands;

/**
 * Created by JPaul on 7/11/2016.
 */
public class CommandInfo
{
    String root;
    String initializer;
    String[] args;
    Object[] methodArgs;
    boolean nested = false;

    public CommandInfo(String root, String initializer, String[] args, Object... methodArgs)
    {
        this.root = root;
        this.initializer = initializer;
        this.args = args;
        this.methodArgs = methodArgs;
    }
}
