package edu.curtin.mad_assignment;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import edu.curtin.mad_assignment.GameDataSchema.GameStatsTable;
import edu.curtin.mad_assignment.GameDataSchema.MapTable;

/**
 * Represents the overall map, and contains a map of MapElement objects (accessible using the
 * get(row, col) method). The two static constants width and HEIGHT indicate the size of the map.
 *
 * There is a static get() method to be used to obtain an instance (rather than calling the
 * constructor directly).
 *
 * There is also a regenerate() method. The map is randomly-generated, and this method will invoke
 * the algorithm again to replace all the map data with a new randomly-generated map.
 */
public class GameData
{
    public static final int BUILD_MODE = 0;
    public static final int DEMOLISH_MODE = 1;
    public static final int DETAILS_MODE = 2;

    private static final int[] GRASS = {R.drawable.ic_grass1, R.drawable.ic_grass2,
            R.drawable.ic_grass3, R.drawable.ic_grass4};

    private final int id = 1;

    private MapElement[][] map;
    private int money;
    private int lastIncome;
    private int gameTime;
    private Settings settings;
    private int nResidential;
    private int nCommercial;
    private SQLiteDatabase db;
    private int mode; //0 for build mode, 1 for demolish mode and 2 for details mode

    private static GameData instance = null;

    public static GameData get()
    {
        if(instance == null)
        {
            instance = new GameData();
        }
        return instance;
    }

    //make a new map that is grass everywhere
    private void generateMap()
    {
        map = new MapElement[settings.getMapHeight()][settings.getMapWidth()];
        int id;
        for(int i = 0; i < settings.getMapHeight(); i++)
        {
            for(int j = 0; j < settings.getMapWidth(); j++)
            {
                map[i][j] = new MapElement(i, j, GRASS[0], GRASS[1], GRASS[2], GRASS[3], null, null, "No owner");
            }
        }
    }

    public GameData()
    {
        setToDefault();
    }

    //set classfields to default valuesl
    private void setToDefault()
    {
        gameTime = 0;
        nResidential = 0;
        nCommercial = 0;
        settings = Settings.get();
        money = settings.getInitialMoney();
        lastIncome = 0;
        mode = 0;
        generateMap();
    }

    //resets the values within the gamedata object, reset the map, and set the database reference again
    public void reset()
    {
        setToDefault();
        setMapDb();
        saveToDb();
    }

    //sets the databse reference for all of the MapElement objects in the map array
    public void setMapDb()
    {
        for(int i = 0; i < settings.getMapHeight(); i++)
        {
            for(int j = 0; j < settings.getMapWidth(); j++)
            {
                map[i][j].setDb(db);
            }
        }
    }

    public MapElement get(int i, int j)
    {
        return map[i][j];
    }

    public int getMoney()
    {
        return money;
    }

    //sets the database reference and saves the gamedata object values if no entry in the game stats table exists, otherwise load the values from the database into the gamedata object
    public void load(SQLiteDatabase db)
    {
        this.db = db;
        GameStatsCursor gameStatsCursor = new GameStatsCursor((db.query(GameStatsTable.NAME, null, null, null, null, null, null)));
        try
        {
            /*if there is no entry in GameStatsTable, it means there is also no entry in MapTable
            so no need check to see if MapTable has entries since both are recreated together.
             */
            if(gameStatsCursor.getCount() > 0) {
                gameStatsCursor.moveToFirst();
                gameStatsCursor.loadGameStats(this);
                loadMap();
            }
        }
        finally {
            gameStatsCursor.close();
        }
    }

    //save the map to database by calling the MapElement's method to save to database
    public void saveMap()
    {
        for(int i = 0; i < settings.getMapHeight(); i++)
        {
            for(int j = 0; j < settings.getMapWidth(); j++)
            {
                map[i][j].saveToDb();
            }
        }
    }

    //loads the map from the database into the gamedata object
    public void loadMap()
    {
        MapCursor mapCursor = new MapCursor((db.query(MapTable.NAME, null, null, null, null, null, null)));
        try
        {
            if(mapCursor.getCount() > 0)
            {
                mapCursor.moveToFirst();
                mapCursor.loadMap(this);
                setMapDb();
            }
        }
        finally {
            mapCursor.close();
        }
    }

    //creates a new entry in the table and save the values within GameData object into the database
    public void saveToDb()
    {
        ContentValues cv = new ContentValues();
        cv.put(GameStatsTable.Cols.ID, id);
        cv.put(GameStatsTable.Cols.NRESIDENTIAL, nResidential);
        cv.put(GameStatsTable.Cols.NCOMMERCIAL, nCommercial);
        cv.put(GameStatsTable.Cols.GAMETIME, gameTime);
        cv.put(GameStatsTable.Cols.CURRENTMONEY, money);
        cv.put(GameStatsTable.Cols.LASTINCOME, lastIncome);
        db.insert(GameStatsTable.NAME, null, cv);
        saveMap();
    }

    //updates the GameStatsTable in the database with the values within GameData object
    public void updateDb()
    {
        ContentValues cv = new ContentValues();
        cv.put(GameStatsTable.Cols.ID, id);
        cv.put(GameStatsTable.Cols.NRESIDENTIAL, nResidential);
        cv.put(GameStatsTable.Cols.NCOMMERCIAL, nCommercial);
        cv.put(GameStatsTable.Cols.GAMETIME, gameTime);
        cv.put(GameStatsTable.Cols.CURRENTMONEY, money);
        cv.put(GameStatsTable.Cols.LASTINCOME, lastIncome);
        String[] whereValue = { String.valueOf(id) };
        db.update(GameStatsTable.NAME, cv, GameStatsTable.Cols.ID + " = ?" , whereValue);
    }

    //sets the values loaded from the database to the gameData object
    public void loadGameStats(int nResidential, int nCommercial, int money, int gameTime, int lastIncome)
    {
        this.nResidential = nResidential;
        this.nCommercial = nCommercial;
        this.gameTime = gameTime;
        this.money = money;
        this.lastIncome = lastIncome;
    }

    //performs 1 tick of the game running
    public void run()
    {
        gameTime++;
        int population = getPopulation();
        double employmentRate = getEmploymentRate();
        //calculation of lastIncome is done in doubles and the final value is truncated to an int
        lastIncome = (int)((double)population * (employmentRate * (double)settings.getSalary() * settings.getTaxRate() - (double)settings.getServiceCost()));
        money += lastIncome;
        updateDb();
    }

    //sets the owner name of the MapElement at the position of (row,col) in the map array
    public void setElementOwnerName(int row, int col, String ownerName)
    {
        map[row][col].setOwnerName(ownerName);
    }

    //sets the image of the MapElement at the position of (row,col) in the map array
    public void setElementImage(int row, int col, Bitmap thumbnail)
    {
        map[row][col].setImage(thumbnail);
    }

    public void setMap(MapElement[][] mapArray)
    {
        this.map = mapArray;
    }

    public int getLastIncome()
    {
        return lastIncome;
    }

    public int getCurrentMode()
    {
        return mode;
    }

    public void setBuildMode()
    {
        mode = BUILD_MODE;
    }

    public void setDemolishMode()
    {
        mode = DEMOLISH_MODE;
    }

    public void setDetailsMode()
    {
        mode = DETAILS_MODE;
    }

    public int getMapWidth()
    {
        return settings.getMapWidth();
    }

    public int getMapHeight()
    {
        return settings.getMapHeight();
    }

    public int getGameTime()
    {
        return gameTime;
    }

    //check if the MapElement at position (row,col) in the map is adjacent to a road.
    //returns true if there is an adjacent road and false otherwise
    public boolean isNextToRoad(int row, int col)
    {
        boolean nextToRoad = false;
        int up, down, left, right;
        up = row - 1;
        down = row + 1;
        left = col - 1;
        right = col + 1;
        if(up >= 0)
        {
            if(map[up][col].getStructure() instanceof Road)
            {
                nextToRoad = true;
            }
        }
        if(down < settings.getMapHeight())
        {
            if(map[down][col].getStructure() instanceof Road)
            {
                nextToRoad = true;
            }
        }
        if(left >= 0)
        {
            if(map[row][left].getStructure() instanceof Road)
            {
                nextToRoad = true;
            }
        }
        if(right < settings.getMapWidth())
        {
            if(map[row][right].getStructure() instanceof Road)
            {
                nextToRoad = true;
            }
        }
        return nextToRoad;
    }

    //reduces the money by the cost of commercial building and return a boolean saying if the commercial building is successfully paid for or not
    public boolean payForResidential()
    {
        boolean paid = false;
        if(money >= settings.getHouseBuildingCost())
        {
            paid = true;
            money -= settings.getHouseBuildingCost();
            nResidential++;
            updateDb();
        }
        return paid;
    }

    //reduces the money by the cost of commercial building and return a boolean saying if the commercial building is successfully paid for or not
    public boolean payForCommercial()
    {
        boolean paid = false;
        if(money >= settings.getCommBuildingCost())
        {
            paid = true;
            money -= settings.getCommBuildingCost();
            nCommercial++;
            updateDb();
        }
        return paid;
    }

    //reduces the money by the cost of road and return a boolean saying if the road is successfully paid for or not
    public boolean payForRoad()
    {
        boolean paid = false;
        if(money >= settings.getRoadBuildingCost())
        {
            paid = true;
            money -= settings.getRoadBuildingCost();
            updateDb();
        }
        return paid;
    }

    public void decrementNResidential()
    {
        nResidential--;
        updateDb();
    }

    public void setNCommercial(int nCommercialal)
    {
        this.nCommercial = nCommercial;
    }

    public void decrementNCommericial()
    {
        nCommercial--;
        updateDb();
    }

    public int getPopulation()
    {
        return settings.getFamilySize() * nResidential;
    }

    public double getEmploymentRate()
    {
        double employmentRate = 1;
        double temp = 2;
        int population = getPopulation();
        //check if population is zero to avoid division by zero error
        if(population != 0)
        {
            temp = (double)nCommercial * (double)settings.getShopSize() / (double)population;
        }
        //employmentRate uses the value that is the smaller one out of the 2
        if(temp < employmentRate)
        {
            employmentRate = temp;
        }
        return employmentRate;
    }
}
