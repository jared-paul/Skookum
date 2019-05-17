package org.jared.dungeoncrawler.plugin;

import de.slikey.effectlib.EffectManager;
import net.minecraft.server.v1_12_R1.Blocks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.generation.block.IBlockUtil;
import org.jared.dungeoncrawler.api.generation.decorations.registry.IDecorationRegistry;
import org.jared.dungeoncrawler.api.generation.theme.Theme;
import org.jared.dungeoncrawler.api.maps.IMapUtil;
import org.jared.dungeoncrawler.api.maps.ListenerTest;
import org.jared.dungeoncrawler.api.maps.MapRegistry;
import org.jared.dungeoncrawler.api.player.registry.IDungeonPlayerRegistry;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.api.plugin.IDungeonCrawlerMain;
import org.jared.dungeoncrawler.api.settings.DungeonSetting;
import org.jared.dungeoncrawler.api.settings.DungeonSettings;
import org.jared.dungeoncrawler.api.settings.IDungeonSettings;
import org.jared.dungeoncrawler.api.structures.IStructureUtil;
import org.jared.dungeoncrawler.api.structures.nbt.CompoundTag;
import org.jared.dungeoncrawler.api.structures.nbt.ListTag;
import org.jared.dungeoncrawler.api.structures.nbt.NBTInputStream;
import org.jared.dungeoncrawler.api.util.ITitleUtil;
import org.jared.dungeoncrawler.game.listeners.NPCClick;
import org.jared.dungeoncrawler.game.listeners.PlayerJoin;
import org.jared.dungeoncrawler.game.listeners.PlayerMove;
import org.jared.dungeoncrawler.generation.decorations.DecorationRegistry;
import org.jared.dungeoncrawler.generation.listeners.LeafDecay;
import org.jared.dungeoncrawler.plugin.commands.CommandManager;
import org.jared.dungeoncrawler.plugin.commands.Commands;
import org.jared.dungeoncrawler.plugin.commands.SimpleInjector;
import org.jared.dungeoncrawler.plugin.registry.DungeonPlayerRegistry;
import org.jared.dungeoncrawler.v1_13_R2.testloader.Test;

import java.io.FileInputStream;

public class DungeonCrawlerMain extends JavaPlugin implements Listener, IDungeonCrawlerMain
{
    private static final CommandManager COMMANDS = new CommandManager();

    private IDecorationRegistry decorationRegistry;
    private IDungeonPlayerRegistry dungeonPlayerRegistry;
    private IDungeonSettings dungeonSettings;
    private IStructureUtil structureUtil;
    private IBlockUtil blockUtil;
    private ITitleUtil titleUtil;
    private EffectManager effectManager;
    private MapRegistry mapRegistry;
    private IMapUtil mapUtil;

    public void onEnable()
    {
        DungeonCrawler.setPlugin(this);


        DungeonCrawler.LOG.console("FUCK YEA HOLY SHIT !!!!!!");

        String packageName = this.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf(".") + 1);

        try
        {
            DungeonCrawler.LOG.warning(version);

            final Class<?> mapSenderClazz = Class.forName("org.jared.dungeoncrawler." + version + ".maps.MapUtil");
            if (IMapUtil.class.isAssignableFrom(mapSenderClazz))
                mapUtil = (IMapUtil) mapSenderClazz.getConstructor().newInstance();

            DungeonCrawler.LOG.warning(mapUtil == null);

            final Class<?> decorationClazz = Class.forName("org.jared.dungeoncrawler." + version + ".StructureUtil");
            if (IStructureUtil.class.isAssignableFrom(decorationClazz))
                structureUtil = (IStructureUtil) decorationClazz.getConstructor().newInstance();

            final Class<?> placingClazz = Class.forName("org.jared.dungeoncrawler." + version + ".block.BlockUtil");
            if (IBlockUtil.class.isAssignableFrom(placingClazz))
                blockUtil = ((IBlockUtil) placingClazz.getConstructor().newInstance());

            final Class<?> titleClazz = Class.forName("org.jared.dungeoncrawler." + version + ".util.TitleUtil");
            if (ITitleUtil.class.isAssignableFrom(titleClazz))
                titleUtil = (ITitleUtil) titleClazz.getConstructor().newInstance();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            decorationRegistry = new DecorationRegistry("Decorations");
            decorationRegistry.loadDecorations();

            dungeonPlayerRegistry = new DungeonPlayerRegistry();

            effectManager = new EffectManager(this);

            mapRegistry = new MapRegistry();

            //new MapColourPalette();

            dungeonSettings = new DungeonSettings();

            Theme theme = (Theme) dungeonSettings.getDungeonSettings("dungeon1").get(DungeonSetting.THEME).getValue();

            DungeonCrawler.LOG.severe(theme.getFloor());


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        COMMANDS.setInjector(new SimpleInjector());
        initCommands();

        registerListeners();
    }

    public void onDisable()
    {
        effectManager.dispose();
    }

    private void registerListeners()
    {
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerMove(), this);
        getServer().getPluginManager().registerEvents(new NPCClick(), this);
        getServer().getPluginManager().registerEvents(new LeafDecay(), this);
        getServer().getPluginManager().registerEvents(new ListenerTest(), this);
    }

    private void initCommands()
    {
        COMMANDS.register(Commands.class);

        getCommand("dungeon").setExecutor(COMMANDS);
    }

    @Override
    public IDecorationRegistry getDecorationRegistry()
    {
        return decorationRegistry;
    }

    @Override
    public IDungeonPlayerRegistry getDungeonPlayerRegistry()
    {
        return dungeonPlayerRegistry;
    }

    @Override
    public IDungeonSettings getDungeonSettings()
    {
        return dungeonSettings;
    }

    @Override
    public IStructureUtil getStructureUtil()
    {
        return structureUtil;
    }

    @Override
    public IBlockUtil getBlockUtil()
    {
        return blockUtil;
    }

    @Override
    public ITitleUtil getTitleUtil()
    {
        return titleUtil;
    }

    @Override
    public EffectManager getEffectManager()
    {
        return effectManager;
    }

    @Override
    public MapRegistry getMapRegistry()
    {
        return mapRegistry;
    }

    @Override
    public IMapUtil getMapUtil()
    {
        return mapUtil;
    }

    @EventHandler
    public void on(PlayerJoinEvent joinEvent)
    {
        joinEvent.getPlayer().setFlySpeed(.6F);
    }
}
