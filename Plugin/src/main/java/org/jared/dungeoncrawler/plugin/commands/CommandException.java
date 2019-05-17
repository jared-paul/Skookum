package org.jared.dungeoncrawler.plugin.commands;

/**
 * Created by JPaul on 7/8/2016.
 */
public class CommandException extends Exception
{
    public CommandException()
    {
        super();
    }

    public CommandException(String message)
    {
        super(message);
    }

    public CommandException(Throwable cause)
    {
        super(cause);
    }
}
