package com.example.clabuyakchai.brushtrainingsimulator.database;

/**
 * Created by Clabuyakchai on 04.05.2018.
 */

public class DBShema {
    public static final class Table {
        public static final String NAME = "statistics";

        public static final class Cols{
            public static final String id = "id";
            public static final String counter = "counter";
            public static final String data = "data";
            public static final String username = "username";
        }
    }
}
