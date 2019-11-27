package com.example.sqlitebasic.database;

import android.provider.BaseColumns;

public class GreatManContract  {

    private GreatManContract(){}

    public  static  final  class  GreatManEntry implements BaseColumns{

        public static final String TABLE_NAME = "greatman";
        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_CONTENT = "content";

        public static final String COLUMN_NATIONALITY = "nationality";

        public static final String COLUMN_BIRTHYEAR = "birthYear";
        public static final String COLUMN_MOT = "mot";
        public static final String COLUMN_NAME_IMAGE = "image";


        public static final String COLUMN_DATE = "date";
    }


}
