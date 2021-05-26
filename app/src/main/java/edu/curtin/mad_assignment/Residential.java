package edu.curtin.mad_assignment;

import java.io.Serializable;

public class Residential extends Structure implements Serializable
{
    public Residential(int drawableId)
    {
        super(drawableId);
    }

    public String getLabel()
    {
        return "Residential";
    }
}
