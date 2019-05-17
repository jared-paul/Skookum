package org.jared.dungeoncrawler.api.generation.delaunay;

import java.util.Comparator;

public class Compare implements Comparator<DT_Point>
{
    private int _flag;

    public Compare(int i)
    {
        _flag = i;
    }

    /**
     * compare between two points.
     */
    @Override
    public int compare(DT_Point o1, DT_Point o2)
    {
        int ans = 0;
        if (o1 != null && o2 != null)
        {
            DT_Point d1 = (DT_Point) o1;
            DT_Point d2 = (DT_Point) o2;
            if (_flag == 0)
            {
                if (d1.x > d2.x) return 1;
                if (d1.x < d2.x) return -1;
                // maxX == x2
                if (d1.y > d2.y) return 1;
                if (d1.y < d2.y) return -1;
            }
            else if (_flag == 1)
            {
                if (d1.x > d2.x) return -1;
                if (d1.x < d2.x) return 1;
                // maxX == x2
                if (d1.y > d2.y) return -1;
                if (d1.y < d2.y) return 1;
            }
            else if (_flag == 2)
            {
                if (d1.y > d2.y) return 1;
                if (d1.y < d2.y) return -1;
                // maxY == y2
                if (d1.x > d2.x) return 1;
                if (d1.x < d2.x) return -1;

            }
            else if (_flag == 3)
            {
                if (d1.y > d2.y) return -1;
                if (d1.y < d2.y) return 1;
                // maxY == y2
                if (d1.x > d2.x) return -1;
                if (d1.x < d2.x) return 1;
            }
        }
        else
        {
            if (o1 == null && o2 == null) return 0;
            if (o1 == null && o2 != null) return 1;
            if (o1 != null && o2 == null) return -1;
        }
        return ans;
    }

    public boolean equals(Object ob)
    {
        return false;
    }
}