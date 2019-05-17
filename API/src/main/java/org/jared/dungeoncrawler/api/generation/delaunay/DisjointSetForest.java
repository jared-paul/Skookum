package org.jared.dungeoncrawler.api.generation.delaunay;

public class DisjointSetForest<T>
{
    public Node makeSet(T x)
    {
        return new Node(x);
    }

    public void union(Node x, Node y)
    {
        link(findSet(x), findSet(y));
    }

    public Node findSet(Node node)
    {
        if (node.parent != null)
        {
            return findSet(node.parent);
        }
        return node;
    }

    private void link(Node x, Node y)
    {
        if (x.rank > y.rank)
        {
            y.parent = x;
        }
        else
        {
            x.parent = y;
            if (x.rank == y.rank)
            {
                y.rank++;
            }
        }
    }

    public class Node
    {
        public T reference;
        public Node parent;
        public int rank;

        public Node(T x)
        {
            reference = x;
            parent = null;
            rank = 0;
        }

        @Override
        public String toString()
        {
            return reference.toString() + "[p=" + parent.reference.toString() + ", rank=" + rank + "]";
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((parent == null) ? 0 : parent.hashCode());
            result = prime * result + rank;
            result = prime * result + ((reference == null) ? 0 : reference.hashCode());
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
            @SuppressWarnings("unchecked")
            Node other = (Node) obj;
            if (reference == null)
            {
                if (other.reference != null)
                {
                    return false;
                }
            }
            else if (!reference.equals(other.reference))
            {
                return false;
            }
            return true;
        }
    }
}
