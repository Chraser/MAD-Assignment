package edu.curtin.mad_assignment;

import android.database.Cursor;
import android.database.CursorWrapper;
import edu.curtin.mad_assignment.GameDataSchema.MapTable;

public class MapCursor extends CursorWrapper
{
    public MapCursor(Cursor cursor)
    {
        super(cursor);
    }

    public void loadMap(GameData gameData)
    {
        int row, col, northEast, northWest, southEast, southWest, structureImage;
        String ownerName, structureType;
        Structure structure;
        MapElement[][] mapArray = new MapElement[gameData.getMapHeight()][gameData.getMapWidth()];
        for(int i = 0; i < gameData.getMapHeight(); i++)
        {
            for(int j = 0; j < gameData.getMapWidth(); j++)
            {
                row = getInt(getColumnIndex(MapTable.Cols.ROW));
                col = getInt(getColumnIndex(MapTable.Cols.COL));
                northEast = getInt(getColumnIndex(MapTable.Cols.NORTHEAST));
                northWest = getInt(getColumnIndex(MapTable.Cols.NORTHWEST));
                southEast = getInt(getColumnIndex(MapTable.Cols.SOUTHEAST));
                southWest = getInt(getColumnIndex(MapTable.Cols.SOUTHWEST));
                structureImage = getInt(getColumnIndex(MapTable.Cols.STRUCTURE_IMAGE));
                ownerName = getString(getColumnIndex(MapTable.Cols.OWNERNAME));
                structureType = getString(getColumnIndex(MapTable.Cols.STRUCTURETYPE));
                if(structureType.equals(StructureData.STRUCTURETYPES[0]))
                {
                    structure = new Residential(structureImage);
                }
                else if(structureType.equals(StructureData.STRUCTURETYPES[1]))
                {
                    structure = new Commercial(structureImage);
                }
                else if(structureType.equals(StructureData.STRUCTURETYPES[2]))
                {
                    structure = new Road(structureImage);
                }
                else
                {
                    structure = null;
                }
                mapArray[row][col] = new MapElement(row, col, northEast, northWest, southEast, southWest, structure, null, ownerName);
                moveToNext();
            }
        }
        gameData.setMap(mapArray);
    }
}
