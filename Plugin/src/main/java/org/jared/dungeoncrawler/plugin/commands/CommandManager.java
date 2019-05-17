package org.jared.dungeoncrawler.plugin.commands;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

/**
 * Created by JPaul on 8/18/2016.
 */
public class CommandManager implements CommandExecutor
{
    private Map<String, Method> commands = Maps.newHashMap();
    private Map<String, Method> serverCommands = Maps.newHashMap();
    private Map<Method, Object> instances = Maps.newHashMap();

    protected SimpleInjector injector;

    public CommandManager()
    {
    }

    public void setInjector(SimpleInjector injector)
    {
        this.injector = injector;
    }

    public void register(Class<? extends CommandClass> clazz)
    {
        Object object = injector != null ? injector.getInstance(clazz) : null;
        register(clazz, object);
    }

    public void register(Class<? extends CommandClass> clazz, Object object)
    {
        for (Method method : clazz.getMethods())
        {
            if (method.isAnnotationPresent(Command.class))
            {
                Command command = method.getAnnotation(Command.class);

                for (String alias : command.aliases())
                {
                    commands.put(alias + " " + command.initializer(), method);

                    if (method.isAnnotationPresent(ConsoleCommand.class))
                    {
                        serverCommands.put(alias + " " + command.initializer(), method);
                    }

                    if (!commands.containsKey(alias + " help"))
                    {
                        commands.put(alias + " help", null);
                    }
                }

                boolean isStatic = Modifier.isStatic(method.getModifiers());

                if (!isStatic)
                {
                    if (object == null)
                        continue;

                    instances.put(method, object);
                }
            }
        }
    }

    private boolean executeSafe(CommandSender sender, CommandInfo commandInfo)
    {
        try
        {
            execute(sender, commandInfo);
        } catch (CommandException e)
        {
            e.printStackTrace();
        }

        return true;
    }

    private void execute(CommandSender sender, CommandInfo commandInfo) throws CommandException
    {
        List<String> args = Lists.newArrayList(commandInfo.args);
        args.remove(commandInfo.initializer);

        String[] newArgs = args.toArray(new String[args.size()]);

        Object[] newMethodArgs = new Object[commandInfo.methodArgs.length + 1];

        newMethodArgs[0] = null;
        newMethodArgs[1] = sender;

        commandInfo.args = newArgs;
        commandInfo.methodArgs = newMethodArgs;

        executeMethod(sender, commandInfo);
    }

    private void executeMethod(CommandSender sender, CommandInfo commandInfo) throws CommandException
    {
        String commandName = commandInfo.root;
        String modifier = commandInfo.initializer;
        boolean help = modifier.equalsIgnoreCase("help");

        String[] args = commandInfo.args;

        Object[] methodArgs = commandInfo.methodArgs;

        Method method = commands.get(commandName + " " + modifier);
        if (method == null && !help)
        {
            method = commands.get(commandName + " *");
        }
        if (method == null && help)
        {
            executeHelp(sender, commandInfo.root, modifier);
            return;
        }

        if (method == null)
        {
            //TODO throw new UnhandledCommandException
            executeHelp(sender, commandInfo.root, modifier);

            return;
        }

        if (sender instanceof ConsoleCommandSender)
        {
            //TODO throw new ServerCommandException
        }

        //TODO if haspermission

        Command command = method.getAnnotation(Command.class);

        CommandContext context = new CommandContext(sender, args);

        if (context.argsLength() < command.minArgs())
        {
            //TODO throw new CommandUsageException
            sender.sendMessage(command.usage());

            return;
        }
        if (command.maxArgs() != -1 && context.argsLength() > command.maxArgs())
        {
            //TODO throw new CommandUsageException
            sender.sendMessage(command.usage());

            return;
        }


        if (!command.flags().contains("*"))
        {
            for (char flag : context.getFlags())
            {
                if (!command.flags().contains(String.valueOf(flag)))
                {
                    //TODO throw new CommandUsageException
                    return;
                }
            }
        }

        methodArgs[0] = context;

        Object instance = instances.get(method);

        try
        {
            method.invoke(instance, methodArgs);
        } catch (InvocationTargetException e)
        {
            if (e.getCause() instanceof CommandException)
            {
                throw (CommandException) e.getCause();
            }
            e.printStackTrace();

            //TODO throw new WrappedCommandException
        } catch (IllegalAccessException | IllegalArgumentException e)
        {
            e.printStackTrace();
        }
    }

    private void executeHelp(CommandSender sender, String root, String modifier)
    {
        Command command = getCommand(root, modifier);

        if (command == null)
        {
            //TODO throw new CommandException(Command Missing + root + initializer)
            return;
        }

        sender.sendMessage(command.help());
    }

    public Command getCommand(String root, String modifier)
    {
        String joined = Joiner.on(' ').join(root, modifier);
        for (Map.Entry<String, Method> entry : commands.entrySet())
        {
            String key = entry.getKey();
            Method value = entry.getValue();

            if (!key.equalsIgnoreCase(joined) || value == null)
            {
                continue;
            }

            return value.getAnnotation(Command.class);
        }

        return null;
    }

    public boolean hasCommand(CommandInfo commandInfo)
    {
        String command = commandInfo.root + " " + commandInfo.initializer.toLowerCase();
        return commands.containsKey(command) || commands.containsKey(command + " *");
    }

    public boolean hasRoot(CommandInfo command)
    {
        String subRoot = command.initializer.toLowerCase();
        boolean hasRoot = false;
        for (String root : commands.keySet())
        {
            String realRoot = root.split(" ")[0];

            if (realRoot.equals(command.root))
            {
                hasRoot = true;
            }
        }

        return hasRoot;
    }

    private boolean suggestClosestModifier(CommandSender sender, CommandInfo commandInfo)
    {
        String closest = getClosestCommandModifier(commandInfo);
        if (!closest.isEmpty())
        {
            sender.sendMessage(ChatColor.RED + "Unknown command");
            sender.sendMessage(ChatColor.RED + "Did you mean " + "\"" + ChatColor.YELLOW + "/" + commandInfo.root + " " + closest + ChatColor.RED + "\"" + " ?");
            return true;
        }
        return false;
    }

    public String getClosestCommandModifier(CommandInfo commandInfo)
    {
        int minDist = Integer.MAX_VALUE;
        String command = (commandInfo.root).toLowerCase();
        String closest = "";
        for (String cmd : commands.keySet())
        {
            String[] split = cmd.split(" ");

            if (!(split[0]).equals(command) || split.length <= 1)
            {
                continue;
            }

            if (commandInfo.nested)
            {
                try
                {
                    String lookingFor = split[1];

                    int distance = getLevenshteinDistance(commandInfo.initializer, lookingFor);
                    if (minDist > distance)
                    {
                        minDist = distance;
                        closest = split[1];
                    }
                } catch (IndexOutOfBoundsException exception)
                {
                    int distance = getLevenshteinDistance(commandInfo.initializer, split[1]);
                    if (minDist > distance)
                    {
                        minDist = distance;
                        closest = split[1];
                    }
                }
            }
            else
            {
                int distance = getLevenshteinDistance(commandInfo.initializer, split[1]);
                if (minDist > distance)
                {
                    minDist = distance;
                    closest = split[1];
                }
            }
        }

        return closest;
    }

    private boolean suggestClosestRoot(CommandSender sender, CommandInfo command)
    {
        String closest = getClosestCommandRoot(command);
        if (!closest.isEmpty())
        {
            sender.sendMessage(ChatColor.RED + "Unknown command");
            sender.sendMessage(ChatColor.RED + "Did you mean " + "\"" + ChatColor.YELLOW + "/" + command.root + " " + closest + ChatColor.RED + "\"" + " ?");
            return true;
        }
        return false;
    }

    public String getClosestCommandRoot(CommandInfo command)
    {
        int minDist = Integer.MAX_VALUE;
        String subRoot = command.initializer;
        String closest = "";
        for (String cmd : commands.keySet())
        {
            String[] split = cmd.split(" ");

            int distance = getLevenshteinDistance(subRoot, split[1]);
            if (minDist > distance)
            {
                minDist = distance;
                closest = split[1];
            }
        }

        return closest;
    }

    private static int getLevenshteinDistance(String s, String t)
    {
        if (s == null || t == null)
        {
            throw new IllegalArgumentException("Strings must not be null");
        }

        int n = s.length(); // length of s
        int m = t.length(); // length of t

        if (n == 0)
        {
            return m;
        }
        else if (m == 0)
        {
            return n;
        }

        int p[] = new int[n + 1]; // 'previous' cost array, horizontally
        int d[] = new int[n + 1]; // cost array, horizontally
        int _d[]; // placeholder to assist in swapping p and d

        // indexes into strings s and t
        int i; // iterates through s
        int j; // iterates through t

        char t_j; // jth character of t

        int cost; // cost

        for (i = 0; i <= n; i++)
            p[i] = i;

        for (j = 1; j <= m; j++)
        {
            t_j = t.charAt(j - 1);
            d[0] = j;

            for (i = 1; i <= n; i++)
            {
                cost = s.charAt(i - 1) == t_j ? 0 : 1;
                // minimum of cell to the left+1, to the top+1, diagonally left
                // and up +cost
                d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
            }

            // copy current distance counts to 'previous row' distance counts
            _d = p;
            p = d;
            d = _d;
        }

        // our last action in the above loop was to switch d and p, so p now
        // actually has the most recent cost counts
        return p[n];
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String cmdName, String[] args)
    {
        String root = command.getName();
        String initializer = args.length > 0 ? args[0] : "";
        String nestedInitializer = args.length > 1 ? args[1] : "";

        Object[] methodArgs = {sender};

        CommandInfo nestedCommandInfo = new CommandInfo(root, initializer + " " + nestedInitializer, args, methodArgs);
        nestedCommandInfo.nested = true;


        List<String> newArgs = Lists.newArrayList(args);
        newArgs.remove(initializer);
        newArgs.remove(nestedInitializer);

        nestedCommandInfo.args = newArgs.toArray(new String[newArgs.size()]);

        if (!hasRoot(nestedCommandInfo))
        {
            return suggestClosestRoot(sender, nestedCommandInfo);
        }
        else if (hasCommand(nestedCommandInfo))
        {
            return executeSafe(sender, nestedCommandInfo);
        }
        else if (!hasCommand(nestedCommandInfo))
        {
            CommandInfo commandInfo = new CommandInfo(root, initializer, args, methodArgs);

            if (!hasCommand(commandInfo))
            {
                return suggestClosestModifier(sender, nestedCommandInfo);
            }
            else
            {
                return executeSafe(sender, commandInfo);
            }
        }
        else
        {
            return executeSafe(sender, nestedCommandInfo);
        }
        // TODO: change the args supplied to a context style system for
        // flexibility (ie. adding more context in the future without
        // changing everything)
    }

}
