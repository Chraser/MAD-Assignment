package edu.curtin.mad_assignment;

import java.io.Serializable;

public class Road extends Structure implements Serializable
{
    public Road(int drawableId)
    {
        super(drawableId);
    }

    public String getLabel()
    {
        return "Road";
    }
}
