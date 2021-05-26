package edu.curtin.mad_assignment;

import java.io.Serializable;

public class Commercial extends Structure implements Serializable
{
    public Commercial(int drawableId)
    {
        super(drawableId);
    }

    public String getLabel()
    {
        return "Commercial";
    }
}
