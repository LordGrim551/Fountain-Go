package com.example.fountainandgo.DATABASE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Fuentes extends SQLiteOpenHelper {

    private static final String DB_NAME = "Fountain&GO";
    private static final int DB_VERSION = 1;

    public Fuentes(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase dbf) {
        dbf.execSQL("CREATE TABLE fuentes " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "latitud DOUBLE NOT NULL," +
                "longitud DOUBLE NOT NULL, " +
                "nombre VARCHAR(25) NOT NULL, " +
                "direccion VARCHAR(50) NOT NULL, " +
                "codigopostal DOUBLE NOT NULL, " +
                "provincia VARCHAR(25) NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase dbf, int oldVersion, int newVersion) {

    }
}