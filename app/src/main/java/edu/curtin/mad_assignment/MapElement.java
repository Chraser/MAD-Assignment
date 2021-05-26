package edu.curtin.mad_assignment;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import edu.curtin.mad_assignment.GameDataSchema.MapTable;

import java.io.Serializable;

/**
 * Represents a single grid square in the map. Each map element has both terrain and an optional
 * structure.
 *
 * The terrain comes in four pieces, as if each grid square was further divided into its own tiny
 * 2x2 grid (north-west, north-east, south-west and south-east). Each piece of the terrain is
 * represented as an int, which is actually a drawable reference. That is, if you have both an
 * ImageView and a MapElement, you can do this:
 *
 * ImageView iv = ...;
 * MapElement me = ...;
 * iv.setImageResource(me.getNorthWest());
 *
 * This will cause the ImageView to display the grid square's north-western terrain image,
 * whatever it is.
 *
 * (The terrain is broken up like this because there are a lot of possible combinations of terrain
 * images for each grid square. If we had a single terrain image for each square, we'd need to
 * manually combine all the possible combinations of images, and we'd get a small explosion of
 * image files.)
 *
 * Meanwhile, the structure is something we want to display over the top of the terrain. Each
 * MapElement has either zero or one Structure} objects. For each grid square, we can also change
 * which structure is built on it.
 */
public class MapElement implements Serializable
{
    public static final String NO_OWNER = "No owner";

    private final int row;
    private final int col;
    private final int terrainNorthWest;
    private final int terrainSouthWest;
    private final int terrainNorthEast;
    private final int terrainSouthEast;
    private Structure structure;
    private Bitmap image;
    private String ownerName;
    private transient SQLiteDatabase db;

    public MapElement(int row, int col, int northWest, int northEast,
                      int southWest, int southEast, Structure structure, Bitmap image, String ownerName)
    {
        this.row = row;
        this.col = col;
        this.terrainNorthWest = northWest;
        this.terrainNorthEast = northEast;
        this.terrainSouthWest = southWest;
        this.terrainSouthEast = southEast;
        this.structure = structure;
        this.image = image;
        this.ownerName = ownerName;
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    public int getNorthWest()
    {
        return terrainNorthWest;
    }

    public int getSouthWest()
    {
        return terrainSouthWest;
    }

    public int getNorthEast()
    {
        return terrainNorthEast;
    }

    public int getSouthEast()
    {
        return terrainSouthEast;
    }

    /**
     * Retrieves the structure built on this map element.
     * @return The structure, or null if one is not present.
     */
    public Structure getStructure()
    {
        return structure;
    }

    public void setStructure(Structure structure)
    {
        this.structure = structure;
        if(ownerName.equals("No owner"))
        {
            if(structure instanceof  Residential)
            {
                this.ownerName = StructureData.STRUCTURETYPES[0];
            }
            else if(structure instanceof  Commercial)
            {
                this.ownerName = StructureData.STRUCTURETYPES[1];
            }
            else
            {
                this.ownerName = StructureData.STRUCTURETYPES[2];
            }
        }
        updateDb();
    }

    /*
    removes the current structure in place and sets the owner name to no one.
    updates the database to reflect current data
    */
    public void removeStructure()
    {
        structure = null;
        this.ownerName = "No owner";
        updateDb();
    }

    public Bitmap getImage()
    {
        return image;
    }

    public void setImage(Bitmap image)
    {
        this.image = image;
    }

    public String getOwnerName()
    {
        return ownerName;
    }

    public void setOwnerName(String ownerName)
    {
        this.ownerName = ownerName;
        updateDb();
    }

    public void setDb(SQLiteDatabase db)
    {
        this.db = db;
    }

    //inserts its data into the database
    public void saveToDb()
    {
        ContentValues cv = new ContentValues();
        cv.put(MapTable.Cols.ROW, row);
        cv.put(MapTable.Cols.COL, col);
        cv.put(MapTable.Cols.NORTHEAST, terrainNorthEast);
        cv.put(MapTable.Cols.NORTHWEST, terrainNorthWest);
        cv.put(MapTable.Cols.SOUTHEAST, terrainSouthEast);
        cv.put(MapTable.Cols.SOUTHWEST, terrainSouthWest);
        if(structure != null)
        {
            cv.put(MapTable.Cols.STRUCTURE_IMAGE, structure.getDrawableId());
        }
        else
        {
            cv.put(MapTable.Cols.STRUCTURE_IMAGE, 1);
        }
        cv.put(MapTable.Cols.OWNERNAME, ownerName);
        if(structure instanceof  Residential)
        {
            cv.put(MapTable.Cols.STRUCTURETYPE, "Residential");
        }
        else if(structure instanceof  Commercial)
        {
            cv.put(MapTable.Cols.STRUCTURETYPE, "Commercial");
        }
        else if(structure instanceof  Road)
        {
            cv.put(MapTable.Cols.STRUCTURETYPE, "Road");
        }
        else
        {
            cv.put(MapTable.Cols.STRUCTURETYPE, "Nothing");
        }
        db.insert(MapTable.NAME, null, cv);
    }

    //updates the GameStatsTable in the database with the values within GameData object
    public void updateDb()
    {
        ContentValues cv = new ContentValues();
        cv.put(MapTable.Cols.ROW, row);
        cv.put(MapTable.Cols.COL, col);
        cv.put(MapTable.Cols.NORTHEAST, terrainNorthEast);
        cv.put(MapTable.Cols.NORTHWEST, terrainNorthWest);
        cv.put(MapTable.Cols.SOUTHEAST, terrainSouthEast);
        cv.put(MapTable.Cols.SOUTHWEST, terrainSouthWest);
        if(structure != null)
        {
            System.out.println("Road saved");
            cv.put(MapTable.Cols.STRUCTURE_IMAGE, structure.getDrawableId());
        }
        else
        {
            cv.put(MapTable.Cols.STRUCTURE_IMAGE, 1);
        }
        cv.put(MapTable.Cols.OWNERNAME, ownerName);
        if(structure instanceof  Residential)
        {
            cv.put(MapTable.Cols.STRUCTURETYPE, "Residential");
        }
        else if(structure instanceof  Commercial)
        {
            cv.put(MapTable.Cols.STRUCTURETYPE, "Commercial");
        }
        else if(structure instanceof  Road)
        {
            cv.put(MapTable.Cols.STRUCTURETYPE, "Road");
        }
        else
        {
            cv.put(MapTable.Cols.STRUCTURETYPE, "Nothing");
        }
        String[] whereValue = { String.valueOf(row), String.valueOf(col) };
        db.update(MapTable.NAME, cv, MapTable.Cols.ROW + " = ? AND " + MapTable.Cols.COL + " = ?", whereValue);
    }
}
