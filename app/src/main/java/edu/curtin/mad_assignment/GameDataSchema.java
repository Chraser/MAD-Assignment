package edu.curtin.mad_assignment;

public class GameDataSchema
{
    public static class GameStatsTable
    {
        public static final String NAME = "gameStats";
        public static class Cols
        {
            public static final String ID = "id";
            public static final String NRESIDENTIAL = "nResidential";
            public static final String NCOMMERCIAL = "nCommercial";
            public static final String CURRENTMONEY = "currentMoney";
            public static final String GAMETIME = "gameTime";
            public static final String LASTINCOME = "lastIncome";
        }
    }

    public static class MapTable
    {
        public static final String NAME = "map";
        public static class Cols
        {
            public static final String ROW = "rowNum";
            public static final String COL = "colNum";
            public static final String NORTHEAST = "northEast";
            public static final String NORTHWEST = "northWest";
            public static final String SOUTHEAST = "southEAST";
            public static final String SOUTHWEST = "southWest";
            public static final String STRUCTURE_IMAGE = "structure_image";
            public static final String OWNERNAME = "ownerName";
            public static final String STRUCTURETYPE = "structureType";
        }
    }
}
