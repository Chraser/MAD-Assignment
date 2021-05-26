package edu.curtin.mad_assignment;

import android.database.Cursor;
import android.database.CursorWrapper;
import edu.curtin.mad_assignment.SettingsSchema.SettingsTable;

public class SettingsCursor extends CursorWrapper
{
    public SettingsCursor(Cursor cursor)
    {
        super(cursor);
    }

    public void loadSettings(Settings settings)
    {
        int mapWidth = getInt(getColumnIndex(SettingsTable.Cols.MAPWIDTH));
        int mapHeight = getInt(getColumnIndex(SettingsTable.Cols.MAPHEIGHT));
        int initialMoney = getInt(getColumnIndex(SettingsTable.Cols.INITIALMONEY));
        settings.setMapWidth(mapWidth);
        settings.setMapHeight(mapHeight);
        settings.setInitialMoney(initialMoney);
    }
}
