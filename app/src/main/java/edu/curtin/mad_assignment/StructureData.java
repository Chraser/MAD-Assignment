package edu.curtin.mad_assignment;

import java.util.Arrays;
import java.util.List;

/**
 * Stores the list of possible structures. This has a static get() method for retrieving an
 * instance, rather than calling the constructor directly.
 *
 * The remaining methods -- get(int), size(), add(Structure) and remove(int) -- provide
 * minimalistic list functionality.
 *
 * There is a static int array called DRAWABLES, which stores all the drawable integer references,
 * some of which are not actually used (yet) in a Structure object.
 */
public class StructureData
{
    public static final String[] STRUCTURETYPES = {"Residential", "Commercial", "Road"};

    public static final int[] DRAWABLES = {
        0, // No structure
        R.drawable.ic_building1, R.drawable.ic_building2, R.drawable.ic_building3,
        R.drawable.ic_building4, R.drawable.ic_building5, R.drawable.ic_building6,
        R.drawable.ic_building7, R.drawable.ic_building8,
        R.drawable.ic_road_ns, R.drawable.ic_road_ew, R.drawable.ic_road_nsew,
        R.drawable.ic_road_ne, R.drawable.ic_road_nw, R.drawable.ic_road_se, R.drawable.ic_road_sw,
        R.drawable.ic_road_n, R.drawable.ic_road_e, R.drawable.ic_road_s, R.drawable.ic_road_w,
        R.drawable.ic_road_nse, R.drawable.ic_road_nsw, R.drawable.ic_road_new, R.drawable.ic_road_sew,};

    private List<Structure> structureList = Arrays.asList(new Structure[] {
        new Residential(R.drawable.ic_building1),
        new Residential(R.drawable.ic_building2),
        new Residential(R.drawable.ic_building3),
        new Residential(R.drawable.ic_building4),
        new Commercial(R.drawable.ic_building5),
        new Commercial(R.drawable.ic_building6),
        new Commercial(R.drawable.ic_building7),
        new Commercial(R.drawable.ic_building8),
        new Road(R.drawable.ic_road_ns),
        new Road(R.drawable.ic_road_ew),
        new Road(R.drawable.ic_road_nsew),
        new Road(R.drawable.ic_road_ne),
        new Road(R.drawable.ic_road_nw),
        new Road(R.drawable.ic_road_se),
        new Road(R.drawable.ic_road_sw),
        new Road(R.drawable.ic_road_n),
        new Road(R.drawable.ic_road_e),
        new Road(R.drawable.ic_road_s),
        new Road(R.drawable.ic_road_w),
        new Road(R.drawable.ic_road_nse),
        new Road(R.drawable.ic_road_nsw),
        new Road(R.drawable.ic_road_new),
        new Road(R.drawable.ic_road_sew),
    });

    private static StructureData instance = null;

    public static StructureData get()
    {
        if(instance == null)
        {
            instance = new StructureData();
        }
        return instance;
    }

    protected StructureData() {}

    public Structure get(int i)
    {
        return structureList.get(i);
    }

    public int size()
    {
        return structureList.size();
    }

    public void add(Structure s)
    {
        structureList.add(0, s);
    }

    public void remove(int i)
    {
        structureList.remove(i);
    }
}
