package org.jared.dungeoncrawler.api.generation.decorations;

import com.google.common.collect.Maps;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.concurrency.Callback;
import org.jared.dungeoncrawler.api.generation.block.IBlockPlacer;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.api.structures.IStructure;

import java.io.File;
import java.util.Map;

public interface IDecoration extends Cloneable
{
    String getName();

    int getRotation();

    void setRotation(int rotation);

    Vector getPosition();

    void setPosition(Vector position);

    Type getType();

    Size getSize();

    boolean hasBeenPlaced();

    void setPlaced(boolean placed);

    void place(Location base, IBlockPlacer blockPlacer, Callback<Boolean> callback);

    IStructure getStructure();

    IDecoration clone();

    enum Size
    {
        SMALL(27),
        MEDIUM(125),
        BIG(343);

        int volume;

        Size(int volume)
        {
            this.volume = volume;
        }

        public int getVolume()
        {
            return volume;
        }

        public static Size fromVolume(int volume)
        {
            if (volume <= SMALL.getVolume())
            {
                return SMALL;
            }
            else if (volume >= BIG.getVolume())
            {
                return BIG;
            }
            else
            {
                return MEDIUM;
            }
        }
    }

    String decorationFolder = DungeonCrawler.getPlugin().getDataFolder() + "\\Decorations";

    enum Type
    {
        WALL(decorationFolder + "\\Wall"),
        CORNER(decorationFolder + "\\Corner"),
        CEILING(decorationFolder + "\\Ceiling"),
        CEILING_CORNER(CEILING.folder + "\\Corner"),
        CEILING_WALL(CEILING.folder + "\\Wall"),
        FLOOR(decorationFolder + "\\Floor"),
        FLOOR_CORNER(FLOOR.folder + "\\Corner"),
        FLOOR_WALL(FLOOR.folder + "\\Wall");

        File folder;

        private static Map<File, Type> typeMap = Maps.newHashMap();

        static
        {
            for (Type type : Type.values())
            {
                typeMap.put(type.folder, type);
            }

            DungeonCrawler.LOG.warning(typeMap.toString());
        }

        Type(String path)
        {
            this.folder = new File(path);
        }

        public static Type fromFile(File file)
        {
            DungeonCrawler.LOG.debug(file);
            return typeMap.get(file.getParentFile());
        }
    }

}
