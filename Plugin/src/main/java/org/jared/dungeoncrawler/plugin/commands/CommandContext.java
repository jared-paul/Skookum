package org.jared.dungeoncrawler.plugin.commands;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Credit to Citizens and sk89q
 */
public class CommandContext
{
    protected String[] args;
    protected final Set<Character> flags = Sets.newHashSet();
    private Location location = null;
    private final CommandSender sender;
    protected final Map<String, String> valueFlags = Maps.newHashMap();

    public CommandContext(CommandSender sender, String[] args)
    {
        this.sender = sender;
        this.args = args;
    }

    public CommandContext(String[] args)
    {
        this(null, args);
    }

    public int argsLength()
    {
        return args.length;
    }

    public String getCommand()
    {
        return args[0];
    }

    public double getDouble(int index) throws NumberFormatException
    {
        return Double.parseDouble(args[index + 1]);
    }

    public double getDouble(int index, double def) throws NumberFormatException
    {
        return index + 1 < args.length ? Double.parseDouble(args[index + 1]) : def;
    }

    public String getFlag(String ch)
    {
        return valueFlags.get(ch);
    }

    public String getFlag(String ch, String def)
    {
        final String value = valueFlags.get(ch);
        if (value == null)
        {
            return def;
        }

        return value;
    }

    public double getFlagDouble(String ch) throws NumberFormatException
    {
        return Double.parseDouble(valueFlags.get(ch));
    }

    public double getFlagDouble(String ch, double def) throws NumberFormatException
    {
        final String value = valueFlags.get(ch);
        if (value == null)
        {
            return def;
        }

        return Double.parseDouble(value);
    }

    public int getFlagInteger(String ch) throws NumberFormatException
    {
        return Integer.parseInt(valueFlags.get(ch));
    }

    public int getFlagInteger(String ch, int def) throws NumberFormatException
    {
        final String value = valueFlags.get(ch);
        if (value == null)
        {
            return def;
        }

        return Integer.parseInt(value);
    }

    public Set<Character> getFlags()
    {
        return flags;
    }

    public int getInteger(int index) throws NumberFormatException
    {
        return Integer.parseInt(args[index + 1]);
    }

    public int getInteger(int index, int def) throws NumberFormatException
    {
        if (index + 1 < args.length)
        {
            try
            {
                return Integer.parseInt(args[index + 1]);
            } catch (NumberFormatException ignored)
            {
            }
        }
        return def;
    }

    public String getJoinedStrings(int initialIndex)
    {
        return getJoinedStrings(initialIndex, ' ');
    }

    public String getJoinedStrings(int initialIndex, char delimiter)
    {
        initialIndex = initialIndex + 1;
        StringBuilder buffer = new StringBuilder(args[initialIndex]);
        for (int i = initialIndex + 1; i < args.length; i++)
            buffer.append(delimiter).append(args[i]);
        return buffer.toString().trim();
    }

    public String[] getPaddedSlice(int index, int padding)
    {
        String[] slice = new String[args.length - index + padding];
        System.arraycopy(args, index, slice, padding, args.length - index);
        return slice;
    }

    public Location getSenderTargetBlockLocation()
    {
        if (sender == null)
        {
            return location;
        }
        if (sender instanceof Player)
        {
            location = ((Player) sender).getTargetBlock((Set<org.bukkit.Material>) null, 50).getLocation();
        }
        else if (sender instanceof BlockCommandSender)
        {
            location = ((BlockCommandSender) sender).getBlock().getLocation();
        }
        return location;
    }

    public String[] getSlice(int index)
    {
        String[] slice = new String[args.length - index];
        System.arraycopy(args, index, slice, 0, args.length - index);
        return slice;
    }

    public String getString(int index)
    {
        return args[index];
    }

    public String[] getArgs()
    {
        return args;
    }

    public String getString(int index, String def)
    {
        return index + 1 < args.length ? args[index + 1] : def;
    }

    public Map<String, String> getValueFlags()
    {
        return valueFlags;
    }

    public boolean hasFlag(char ch)
    {
        return flags.contains(ch);
    }

    public boolean hasValueFlag(String ch)
    {
        return valueFlags.containsKey(ch);
    }

    public int length()
    {
        return args.length;
    }

    public boolean matches(String command)
    {
        return args[0].equalsIgnoreCase(command);
    }

    private static final Pattern FLAG = Pattern.compile("^-[a-zA-Z]+$");
    private static final Splitter LOCATION_SPLITTER = Splitter.on(Pattern.compile("[,]|[:]")).omitEmptyStrings();
    private static final Pattern VALUE_FLAG = Pattern.compile("^--[a-zA-Z0-9-]+$");
}
