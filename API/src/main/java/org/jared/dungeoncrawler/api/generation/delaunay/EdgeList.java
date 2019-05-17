package org.jared.dungeoncrawler.api.generation.delaunay;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class EdgeList
{
    private List<Edge> list = Lists.newArrayList();

    public EdgeList(DelaunayTriangulation delaunayTriangulation, DisjointSetForest<DT_Point> forest)
    {
        HashMap<DT_Point, DisjointSetForest<DT_Point>.Node> nodeMap = Maps.newHashMap();
        DT_Point point;

        Iterator<DT_Point> dtPointIterator = delaunayTriangulation.verticesIterator();
        while (dtPointIterator.hasNext())
        {
            point = dtPointIterator.next();
            nodeMap.put(point, forest.makeSet(point));
        }

        Edge edge;
        DisjointSetForest<DT_Point>.Node node1, node2, node3;
        DT_Triangle triangle;

        Iterator<DT_Triangle> triangleIterator = delaunayTriangulation.trianglesIterator();
        while (triangleIterator.hasNext())
        {
            triangle = triangleIterator.next();
            node1 = nodeMap.get(triangle.p1());
            node2 = nodeMap.get(triangle.p2());
            edge = new Edge(node1, node2);
            if (!list.contains(edge))
            {
                list.add(edge);
            }

            if (!triangle.isHalfplane())
            {
                node3 = nodeMap.get(triangle.p3());
                edge = new Edge(node2, node3);
                if (!list.contains(edge))
                {
                    list.add(edge);
                }
                edge = new Edge(node3, node1);
                if (!list.contains(edge))
                {
                    list.add(edge);
                }
            }
        }

        Collections.sort(list);
    }

    public List<Edge> getList()
    {
        return list;
    }

    public class Edge implements Comparable<Edge>
    {
        private final DisjointSetForest<DT_Point>.Node node1;
        private final DisjointSetForest<DT_Point>.Node node2;
        private final int distanceSquared;

        public Edge(DisjointSetForest<DT_Point>.Node node1, DisjointSetForest<DT_Point>.Node node2)
        {
            this.node1 = node1;
            this.node2 = node2;
            int dx = (int) (node2.reference.x() - node1.reference.x());
            int dy = (int) (node2.reference.y() - node1.reference.y());
            distanceSquared = dx * dx + dy * dy;
        }

        public int getDistanceSquared()
        {
            return distanceSquared;
        }

        public DT_Point getP1()
        {
            return node1.reference;
        }

        public DT_Point getP2()
        {
            return node2.reference;
        }

        public DisjointSetForest<DT_Point>.Node getNode1()
        {
            return node1;
        }

        public DisjointSetForest<DT_Point>.Node getNode2()
        {
            return node2;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((node1 == null) ? 0 : node1.hashCode());
            result = prime * result + ((node2 == null) ? 0 : node2.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
            {
                return true;
            }
            if (obj == null)
            {
                return false;
            }
            if (getClass() != obj.getClass())
            {
                return false;
            }
            Edge other = (Edge) obj;
            if (!node1.equals(other.node1))
            {
                if (!(node1.equals(other.node2) && node2.equals(other.node1)))
                {
                    return false;
                }
            }
            if (!node2.equals(other.node2))
            {
                if (!(node1.equals(other.node2) && node2.equals(other.node1)))
                {
                    return false;
                }
            }

            return true;
        }

        @Override
        public int compareTo(Edge otherEdge)
        {
            if (distanceSquared < otherEdge.distanceSquared)
            {
                return 1;
            }
            else if (distanceSquared > otherEdge.distanceSquared)
            {
                return -1;
            }

            return 0;
        }
    }
}
