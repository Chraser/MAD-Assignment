package edu.curtin.mad_assignment;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import edu.curtin.mad_assignment.SettingsSchema.SettingsTable;

public class Settings
{
    public static final int MAPWIDTH = 50;
    public static final int MAPHEIGHT = 10;
    public static final int INITIALMONEY = 1000;
    public static final int FAMILYSIZE = 4;
    public static final int SHOPSIZE = 6;
    public static final int SALARY = 10;
    public static final double TAXRATE = 0.3;
    public static final int SERVICECOST = 2;
    public static final int HOUSEBUILDINGCOST = 100;
    public static final int COMMBUILDINGCOST = 500;
    public static final int ROADBUILDINGCOST = 20;

    private final int id = 1;

    private int mapWidth;
    private int mapHeight;
    private int initialMoney;
    private int familySize;
    private int shopSize;
    private int salary;
    private double taxRate;
    private int serviceCost;
    private int houseBuildingCost;
    private int commBuildingCost;
    private int roadBuildingCost;
    private SQLiteDatabase db;

    private static Settings instance = null;

    public static Settings get()
    {
        if(instance == null)
        {
            instance = new Settings();
        }
        return instance;
    }

    protected Settings()
    {
        mapWidth = MAPWIDTH;
        mapHeight = MAPHEIGHT;
        initialMoney = INITIALMONEY;
        familySize = FAMILYSIZE;
        shopSize = SHOPSIZE;
        salary = SALARY;
        taxRate = TAXRATE;
        serviceCost = SERVICECOST;
        houseBuildingCost = HOUSEBUILDINGCOST;
        commBuildingCost = COMMBUILDINGCOST;
        roadBuildingCost = ROADBUILDINGCOST;
    }

    /*sets the database reference in the Settings object, load the values from the database or if
     no entry exist for settings saved in the database, save the values within settings object into database
     */
    public void load(SQLiteDatabase db)
    {
        this.db = db;
        SettingsCursor cursor = new SettingsCursor(db.query(SettingsTable.NAME, null, null, null, null, null, null));
        try
        {
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                cursor.loadSettings(this);
            }
            else
            {
                saveToDb();
            }
        }
        finally
        {
            cursor.close();
        }
    }

    //save the values within settings object to database
    private void saveToDb()
    {
        ContentValues cv = new ContentValues();
        cv.put(SettingsTable.Cols.ID, id);
        cv.put(SettingsTable.Cols.MAPHEIGHT,mapHeight);
        cv.put(SettingsTable.Cols.MAPWIDTH,mapWidth);
        cv.put(SettingsTable.Cols.INITIALMONEY,initialMoney);
        db.insert(SettingsTable.NAME, null, cv);
    }

    //update the database with the values within settings object
    public void updateDb()
    {
        ContentValues cv = new ContentValues();
        cv.put(SettingsTable.Cols.ID, id);
        cv.put(SettingsTable.Cols.MAPHEIGHT, mapHeight);
        cv.put(SettingsTable.Cols.MAPWIDTH, mapWidth);
        cv.put(SettingsTable.Cols.INITIALMONEY, initialMoney);
        String[] whereValue = { String.valueOf(id) };
        db.update(SettingsTable.NAME, cv, SettingsTable.Cols.ID + " = ?" , whereValue);
    }

    //set classfields to default values
    public void resetToDefault()
    {
        mapWidth = MAPWIDTH;
        mapHeight = MAPHEIGHT;
        initialMoney = INITIALMONEY;
        updateDb();
    }

    public int getMapWidth()
    {
        return mapWidth;
    }

    public int getMapHeight()
    {
        return mapHeight;
    }

    public void setMapWidth(int mapWidth)
    {
        this.mapWidth = mapWidth;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public int getInitialMoney() {
        return initialMoney;
    }

    public void setInitialMoney(int initialMoney) {
        this.initialMoney = initialMoney;
    }

    public int getFamilySize() {
        return familySize;
    }

    public void setFamilySize(int familySize) {
        this.familySize = familySize;
    }

    public int getShopSize() {
        return shopSize;
    }

    public void setShopSize(int shopSize) {
        this.shopSize = shopSize;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public int getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(int serviceCost) {
        this.serviceCost = serviceCost;
    }

    public int getHouseBuildingCost() {
        return houseBuildingCost;
    }

    public void setHouseBuildingCost(int houseBuildingCost) {
        this.houseBuildingCost = houseBuildingCost;
    }

    public int getCommBuildingCost() {
        return commBuildingCost;
    }

    public void setCommBuildingCost(int commBuildingCost) {
        this.commBuildingCost = commBuildingCost;
    }

    public int getRoadBuildingCost() {
        return roadBuildingCost;
    }

    public void setRoadBuildingCost(int roadBuildingCost) {
        this.roadBuildingCost = roadBuildingCost;
    }
}
