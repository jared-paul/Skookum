package org.jared.dungeoncrawler.generation.decorations;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.concurrency.Callback;
import org.jared.dungeoncrawler.api.generation.block.IBlockPlacer;
import org.jared.dungeoncrawler.api.generation.decorations.IDecoration;
import org.jared.dungeoncrawler.api.structures.IStructure;

public class Decoration implements IDecoration
{
    private int rotation;
    private Vector position;
    private Type type;
    private Size size;
    private boolean placed = false;
    private IStructure structure;

    public Decoration(IStructure structure, Size size, Type type)
    {
        this.structure = structure;
        this.size = size;
        this.type = type;
    }

    @Override
    public String getName()
    {
        return structure.getName();
    }

    @Override
    public int getRotation()
    {
        return rotation;
    }

    @Override
    public void setRotation(int rotation)
    {
        this.rotation = rotation;
    }

    @Override
    public Vector getPosition()
    {
        return position;
    }

    @Override
    public void setPosition(Vector position)
    {
        this.position = position;
    }

    @Override
    public IStructure getStructure()
    {
        return structure;
    }

    @Override
    public Size getSize()
    {
        return size;
    }

    @Override
    public Type getType()
    {
        return type;
    }

    @Override
    public boolean hasBeenPlaced()
    {
        return placed;
    }

    @Override
    public void setPlaced(boolean placed)
    {
        this.placed = placed;
    }

    @Override
    public void place(Location base, IBlockPlacer blockPlacer, Callback<Boolean> callback)
    {
        structure.rotate(rotation);
        structure.place(base.clone().add(position), blockPlacer, callback);
    }

    @Override
    public Decoration clone()
    {
        try
        {
            Decoration decoration = (Decoration) super.clone();
            decoration.structure = decoration.structure.clone();
            return decoration;
        } catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
