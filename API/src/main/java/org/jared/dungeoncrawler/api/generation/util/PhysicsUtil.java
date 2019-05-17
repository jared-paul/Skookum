package org.jared.dungeoncrawler.api.generation.util;

import org.bukkit.util.Vector;

public class PhysicsUtil
{
    public static int round(double n, double m)
    {
        double test = Math.floor(((n + m - 1)/m)) * m;
        return (int) test;
    }

    public static Vector getRandomPointInCircle(int radius)
    {
        double t = 2 * Math.PI * Math.random();
        double u = Math.random() + Math.random();
        double r;

        if (u > 1)
        {
            r = 2 - u;
        }
        else
        {
            r = u;
        }

        return new Vector((int) (radius * r * Math.cos(t)), 0, (int) (radius * r * Math.sin(t)));
    }
}
