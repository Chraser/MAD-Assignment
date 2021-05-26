package edu.curtin.mad_assignment;

public class SettingsSchema
{
    public static class SettingsTable
    {
        public static final String NAME = "settings";
        public static class Cols
        {
            public static final String ID = "id";
            public static final String MAPWIDTH = "mapWidth";
            public static final String MAPHEIGHT = "mapHeight";
            public static final String INITIALMONEY = "initialMoney";
        }
    }
}
