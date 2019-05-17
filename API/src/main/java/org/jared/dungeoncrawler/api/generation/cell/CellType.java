package org.jared.dungeoncrawler.api.generation.cell;

public enum CellType
{
    OVERLAP(-1),
    NONE(0),
    FILL(1),
    ROOM(2),
    HIDDEN_ROOM(3);

    private int id;

    CellType(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }
}
