package org.jared.dungeoncrawler.api.generation.delaunay;

/**
 * this class represents a simple circle. <br />
 * it is used by the Delaunay Triangulation class. <br />
 * <br />
 * note that this class is immutable.
 *
 * @see DelaunayTriangulation
 */
public class DT_Circle
{

    private DT_Point c;
    private double r;

    /**
     * Constructor. <br />
     * Constructs a new Circle_dt.
     *
     * @param c Center of the circle.
     * @param r Radius of the circle.
     */
    public DT_Circle(DT_Point c, double r)
    {
        this.c = c;
        this.r = r;
    }

    /**
     * Copy Constructor. <br />
     * Creates a new Circle with same properties of <code>circ</code>.
     *
     * @param circ Circle to clone.
     */
    public DT_Circle(DT_Circle circ)
    {
        this.c = circ.c;
        this.r = circ.r;
    }

    public String toString()
    {
        return (new String(" Circle[" + c.toString() + "|" + r + "|"
                + (int) Math.round(Math.sqrt(r)) + "]"));
    }

    /**
     * Gets the center of the circle.
     *
     * @return the center of the circle.
     */
    public DT_Point Center()
    {
        return this.c;
    }

    /**
     * Gets the radius of the circle.
     *
     * @return the radius of the circle.
     */
    public double Radius()
    {
        return this.r;
    }
}