package edu.curtin.mad_assignment;

import java.io.Serializable;

/**
 * Represents a possible structure to be placed on the map. A structure simply contains a drawable
 * int reference, and a string label to be shown in the selector.
 */
public abstract class Structure implements Serializable
{
    private final int drawableId;

    public Structure(int drawableId)
    {
        this.drawableId = drawableId;
    }

    public int getDrawableId()
    {
        return drawableId;
    }

    public abstract String getLabel();
}
