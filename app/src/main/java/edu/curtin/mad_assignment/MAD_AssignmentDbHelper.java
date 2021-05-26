package edu.curtin.mad_assignment;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import edu.curtin.mad_assignment.SettingsSchema.SettingsTable;
import edu.curtin.mad_assignment.GameDataSchema.GameStatsTable;
import edu.curtin.mad_assignment.GameDataSchema.MapTable;

public class MAD_AssignmentDbHelper extends SQLiteOpenHelper
{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "mad_assignment.db";

    public MAD_AssignmentDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        createSettingsTable(db);
        createGameStatsTable(db);
        createMapTable(db);
    }

    public void resetGameDataTables(SQLiteDatabase db)
    {
        resetGameStatsTable(db);
        resetMapTable(db);
    }

    private void createSettingsTable(SQLiteDatabase db)
    {
        db.execSQL("create table " + SettingsTable.NAME + "("
                + SettingsTable.Cols.ID + " INTEGER, "
                + SettingsTable.Cols.MAPWIDTH + " INTEGER, "
                + SettingsTable.Cols.MAPHEIGHT + " INTEGER, "
                + SettingsTable.Cols.INITIALMONEY + " INTEGER)"
        );
    }

    //drop the table if exist and recreate the table to ensure an empty table
    private void resetSettingsTable(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS " + SettingsTable.NAME);
        createSettingsTable(db);
    }

    private void createGameStatsTable(SQLiteDatabase db)
    {
        db.execSQL("create table " + GameStatsTable.NAME + "("
                + GameStatsTable.Cols.ID + " INTEGER, "
                + GameStatsTable.Cols.NRESIDENTIAL + " INTEGER, "
                + GameStatsTable.Cols.NCOMMERCIAL + " INTEGER, "
                + GameStatsTable.Cols.GAMETIME + " INTEGER, "
                + GameStatsTable.Cols.CURRENTMONEY + " INTEGER, "
                + GameStatsTable.Cols.LASTINCOME + " INTEGER)"
        );
    }

    //drop the table if exist and recreate the table to ensure an empty table
    private void resetGameStatsTable(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS " + GameStatsTable.NAME);
        createGameStatsTable(db);
    }

    private void createMapTable(SQLiteDatabase db)
    {
        db.execSQL("create table " + MapTable.NAME + "("
                + MapTable.Cols.ROW + " INTEGER, "
                + MapTable.Cols.COL + " INTEGER, "
                + MapTable.Cols.NORTHEAST + " INTEGER, "
                + MapTable.Cols.NORTHWEST + " INTEGER, "
                + MapTable.Cols.SOUTHEAST + " INTEGER, "
                + MapTable.Cols.SOUTHWEST + " INTEGER, "
                + MapTable.Cols.STRUCTURE_IMAGE + " INTEGER, "
                + MapTable.Cols.OWNERNAME + " String, "
                + MapTable.Cols.STRUCTURETYPE + " String)"
        );
    }

    //drop the table if exist and recreate the table to ensure an empty table
    private void resetMapTable(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS " + MapTable.NAME);
        createMapTable(db);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int v1, int v2) {}
}
