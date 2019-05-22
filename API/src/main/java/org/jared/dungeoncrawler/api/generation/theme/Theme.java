package org.jared.dungeoncrawler.api.generation.theme;

import org.jared.dungeoncrawler.api.material.XMaterial;

import java.util.Map;

public class Theme
{
    private String name;
    private Map<XMaterial, Integer> floor, wall, misc;

    public Theme(String name, Map<XMaterial, Integer> floor, Map<XMaterial, Integer> wall, Map<XMaterial, Integer> misc)
    {
        this.name = name;
        this.floor = floor;
        this.wall = wall;
        this.misc = misc;
    }

    public String getName()
    {
        return name;
    }

    public Map<XMaterial, Integer> getFloor()
    {
        return floor;
    }

    public void setFloor(Map<XMaterial, Integer> floor)
    {
        this.floor = floor;
    }

    public Map<XMaterial, Integer> getWalls()
    {
        return wall;
    }

    public void setWall(Map<XMaterial, Integer> wall)
    {
        this.wall = wall;
    }

    public Map<XMaterial, Integer> getMisc()
    {
        return misc;
    }

    public void setMisc(Map<XMaterial, Integer> misc)
    {
        this.misc = misc;
    }
}
