package org.jared.dungeoncrawler.api.generation.cell;

import com.google.common.collect.Lists;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.generation.decorations.IDecoration;
import org.jared.dungeoncrawler.api.structures.AABB;
import org.jbox2d.dynamics.Body;

import java.util.List;

public abstract class AbstractRoom implements IRoom
{
    protected Vector center;
    protected int width;
    protected int depth;

    protected Body physicsBody;
    protected AABB aabb;

    protected List<Vector> doorways = Lists.newArrayList();
    protected CellType type;

    protected List<IDecoration> decorations = Lists.newArrayList();

    protected AbstractRoom()
    {
        this.type = CellType.NONE;
    }

    @Override
    public CellType getType()
    {
        return type;
    }

    @Override
    public void setType(CellType type)
    {
        this.type = type;
    }

    @Override
    public List<Vector> getDoorways()
    {
        return doorways;
    }

    @Override
    public Body getPhysicsBody()
    {
        return physicsBody;
    }

    @Override
    public void setPhysicsBody(Body physicsBody)
    {
        this.physicsBody = physicsBody;
    }

    @Override
    public int getWidth()
    {
        return this.width;
    }

    @Override
    public void setWidth(int width)
    {
        this.width = width;
    }

    @Override
    public int getDepth()
    {
        return this.depth;
    }

    @Override
    public void setDepth(int depth)
    {
        this.depth = depth;
    }

    @Override
    public AABB getAABB()
    {
        return aabb;
    }

    @Override
    public void setAABB(AABB aabb)
    {
        this.aabb = aabb;
    }

    @Override
    public Vector getCenter()
    {
        return aabb.getCenter();
    }

    @Override
    public void setCenter(Vector center)
    {
        aabb.setCenter(center.getBlockX(), 0, center.getBlockZ());
    }

    @Override
    public int getArea()
    {
        return aabb.getDepth() * aabb.getWidth();
    }

    @Override
    public List<IDecoration> getDecorations()
    {
        return decorations;
    }
}
