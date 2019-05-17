package org.jared.dungeoncrawler.api.generation.cell;

import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.generation.decorations.IDecoration;
import org.jared.dungeoncrawler.api.structures.AABB;
import org.jbox2d.dynamics.Body;

import java.util.List;

public interface IRoom
{
    CellType getType();

    void setType(CellType type);

    List<Vector> getDoorways();

    Body getPhysicsBody();

    void setPhysicsBody(Body physicsBody);

    int getWidth();

    void setWidth(int width);

    int getDepth();

    void setDepth(int height);

    AABB getAABB();

    void setAABB(AABB aabb);

    Vector getCenter();

    void setCenter(Vector center);

    int getArea();

    List<IDecoration> getDecorations();
}
