package org.jared.dungeoncrawler.v1_13_R2;

import net.minecraft.server.v1_13_R2.DefinedStructure;
import net.minecraft.server.v1_13_R2.DefinedStructureManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.jared.dungeoncrawler.api.structures.IStructure;
import org.jared.dungeoncrawler.api.structures.IStructureUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

public class StructureUtil implements IStructureUtil
{
    /*
    @Override
    public IStructure createStructure(Location[] corners, String author)
    {
        if (corners.length != 2)
            throw new IllegalArgumentException("An area needs to be set up by exactly 2 opposite edges!");
        Location[] normalized = normalizeEdges(corners[0], corners[1]); // find this method at the end of the tutorial
        // ^^ This is juggling the coordinates, so the first is the Corner with lowest x, y, z and the second has the highest x, y, z.
        WorldServer world = ((CraftWorld) normalized[0].getWorld()).getHandle();
        int[] dimensions = getDimensions(normalized); // find this method at the end of the tutorial
        if (dimensions[0] > 32 || dimensions[1] > 32 || dimensions[2] > 32)
            throw new IllegalArgumentException("A single structure can only be 32x32x32!");
        DefinedStructure structure = new DefinedStructure();
        structure.a(world, new BlockPosition(normalized[0].getBlockX(), normalized[0].getBlockY(), normalized[0].getBlockZ()), new BlockPosition(dimensions[0], dimensions[1], dimensions[2]), true, Blocks.STRUCTURE_BLOCK);
        structure.a(author); // may not be saved to file anymore since 1.13
        return new Structure(structure);
    }

    @Override
    public void saveStructure(IStructure definedStructure, File destination) throws IOException
    {
        NBTTagCompound fileTag = new NBTTagCompound();
        fileTag = ((Structure) definedStructure).getDefinedStructure().a(fileTag);
        NBTCompressedStreamTools.a(fileTag, new FileOutputStream(new File(destination + ".nbt")));
    }
    */

    /*
    @Override
    public IStructure loadStructure(File source) throws IOException
    {
        CustomDefinedStructure structure = new CustomDefinedStructure();
        NBTTagCompound tagCompound = NBTCompressedStreamTools.a(new FileInputStream(source));
        structure.b(tagCompound);

        IStructure iStructure = new Structure(structure);
        iStructure.setFile(source);
        String fileName = source.getName();
        iStructure.setName(fileName.substring(0, fileName.lastIndexOf('.')));
        return iStructure;
    }
    */

    @Override
    public IStructure loadStructure(File source) throws Exception
    {
        World world = Bukkit.getWorlds().get(0);
        net.minecraft.server.v1_13_R2.WorldServer craftWorld = ((CraftWorld) world).getHandle();

        Method convertMethod = DefinedStructureManager.class.getDeclaredMethod("a", InputStream.class);
        convertMethod.setAccessible(true);
        DefinedStructure structure = (DefinedStructure) convertMethod.invoke(craftWorld.D(), new FileInputStream(source));

        IStructure iStructure = new Structure(structure);
        iStructure.setFile(source);
        String fileName = source.getName();
        iStructure.setName(fileName.substring(0, fileName.lastIndexOf('.')));
        return iStructure;
    }


    /*
    @Override
    public void pasteStructure(IStructure definedStructure, Location startEdge, Object enumBlockRotation)
    {
        WorldServer world = ((CraftWorld) startEdge.getWorld()).getHandle();
        DefinedStructureInfo structInfo = new DefinedStructureInfo().a(EnumBlockMirror.NONE).a((EnumBlockRotation) enumBlockRotation).a(false).a((ChunkCoordIntPair) null).a((Block) null).a(false).a(1.0f).a(new Random());
        // false sets ignore entities to false (so it does NOT ignore them)
        // 1.0f sets the amout of to be pasted blocks to 100%
        // mirror & rortation are self explaining
        // the block does the thing like the block does, which can be set in the ingame structure block GUI
        // no idea at the moment, what the coord pair, the random and the second false does
        // If you want to find out more about it, see net.minecraft.server.v1_13_R1.TileEntityStructure.c(boolean), there mojang calls the method and compare it with the ingame GUI
        ((Structure) definedStructure).getDefinedStructure().a(world, new BlockPosition(startEdge.getBlockX(), startEdge.getBlockY(), startEdge.getBlockZ()), structInfo);
    }
    */

    @Override
    public int[] getDimensions(Location[] corners)
    {
        if (corners.length != 2)
            throw new IllegalArgumentException("An area needs to be set up by exactly 2 opposite edges!");

        return new int[]{corners[1].getBlockX() - corners[0].getBlockX() + 1, corners[1].getBlockY() - corners[0].getBlockY() + 1, corners[1].getBlockZ() - corners[0].getBlockZ() + 1};
    }

    @Override
    public Location[] normalizeEdges(Location startBlock, Location endBlock)
    {
        int xMin, xMax, yMin, yMax, zMin, zMax;
        if (startBlock.getBlockX() <= endBlock.getBlockX())
        {
            xMin = startBlock.getBlockX();
            xMax = endBlock.getBlockX();
        }
        else
        {
            xMin = endBlock.getBlockX();
            xMax = startBlock.getBlockX();
        }
        if (startBlock.getBlockY() <= endBlock.getBlockY())
        {
            yMin = startBlock.getBlockY();
            yMax = endBlock.getBlockY();
        }
        else
        {
            yMin = endBlock.getBlockY();
            yMax = startBlock.getBlockY();
        }
        if (startBlock.getBlockZ() <= endBlock.getBlockZ())
        {
            zMin = startBlock.getBlockZ();
            zMax = endBlock.getBlockZ();
        }
        else
        {
            zMin = endBlock.getBlockZ();
            zMax = startBlock.getBlockZ();
        }

        return new Location[]{new Location(startBlock.getWorld(), xMin, yMin, zMin), new Location(startBlock.getWorld(), xMax, yMax, zMax)};
    }
}
