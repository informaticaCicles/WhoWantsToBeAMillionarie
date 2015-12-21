package com.example.evanmartnez.whowantstobeamillionarie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScoresSQLiteHelper extends SQLiteOpenHelper{

    String sqlCreate = "CREATE TABLE Scores (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                            "name TEXT NOT NULL, " +
                                            "score INTEGER NOT NULL, " +
                                            "longitude FLOAT, " +
                                            "latitude FLOAT)";

    public ScoresSQLiteHelper(Context contexto, String nombre,
                                 SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS Scores");

        db.execSQL(sqlCreate);
    }

}
