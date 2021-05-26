package edu.curtin.mad_assignment;

import android.database.Cursor;
import android.database.CursorWrapper;
import edu.curtin.mad_assignment.GameDataSchema.GameStatsTable;

public class GameStatsCursor extends CursorWrapper
{
    public GameStatsCursor(Cursor cursor)
    {
        super(cursor);
    }

    public void loadGameStats(GameData gameData)
    {
        int nResidential = getInt(getColumnIndex(GameStatsTable.Cols.NRESIDENTIAL));
        int nCommercial = getInt(getColumnIndex(GameStatsTable.Cols.NCOMMERCIAL));
        int currentMoney = getInt(getColumnIndex(GameStatsTable.Cols.CURRENTMONEY));
        int gameTime = getInt(getColumnIndex(GameStatsTable.Cols.GAMETIME));
        int lastIncome = getInt(getColumnIndex(GameStatsTable.Cols.LASTINCOME));
        gameData.loadGameStats(nResidential, nCommercial, currentMoney, gameTime, lastIncome);
    }
}
