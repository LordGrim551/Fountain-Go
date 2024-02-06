package com.example.fountainandgo.DATABASE;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Datos extends SQLiteOpenHelper {

    private static final String DB_NAME = "Fountain&GO";
    private static final int DB_VERSION = 1;

    public Datos(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre VARCHAR(25) NOT NULL," +
                "apellidos VARCHAR(25) NOT NULL," +
                "usuario VARCHAR(25) NOT NULL," +
                "contrasena VARCHAR(25) NOT NULL," +
                "confcontrasena VARCHAR(25) NOT NULL," +
                "email VARCHAR(25) NOT NULL)");

        db.execSQL("CREATE TABLE fuentes " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "latitud DOUBLE NOT NULL," +
                "longitud DOUBLE NOT NULL, " +
                "nombre VARCHAR(25) NOT NULL, " +
                "direccion VARCHAR(50) NOT NULL, " +
                "codigopostal DOUBLE NOT NULL, " +
                "provincia VARCHAR(25) NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public class Fuente {
        private int id;
        private double latitud;
        private double longitud;
        private String nombre;
        private String direccion;
        private double codigoPostal;
        private String provincia;

        public Fuente (int id, double latitud, double longitud, String nombre, String direccion, double codigoPostal, String provincia) {
            this.id = id;
            this.latitud = latitud;
            this.longitud = longitud;
            this.nombre = nombre;
            this.direccion = direccion;
            this.codigoPostal = codigoPostal;
            this.provincia = provincia;
        }

        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }

        public double getLatitud() {
            return latitud;
        }
        public void setLatitud(double latitud) {
            this.latitud = latitud;
        }

        public double getLongitud() {
            return longitud;
        }
        public void setLongitud(double longitud) {
            this.longitud = longitud;
        }

        public String getNombre() {
            return nombre;
        }
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getDireccion() {
            return direccion;
        }
        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }

        public double getCodigoPostal() {
            return codigoPostal;
        }
        public void setCodigoPostal(double codigoPostal) {
            this.codigoPostal = codigoPostal;
        }

        public String getProvincia() {
            return provincia;
        }
        public void setProvincia(String provincia) {
            this.provincia = provincia;
        }
    }

    public ArrayList<Fuente> obtenerFuentes() {
        ArrayList<Fuente> listaFuentes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM fuentes", null);

        int columnIndexId = cursor.getColumnIndex("id");
        int columnIndexLatitud = cursor.getColumnIndex("latitud");
        int columnIndexLongitud = cursor.getColumnIndex("longitud");
        int columnIndexNombre = cursor.getColumnIndex("nombre");
        int columnIndexDireccion = cursor.getColumnIndex("direccion");
        int columnIndexCodigoPostal = cursor.getColumnIndex("codigopostal");
        int columnIndexProvincia = cursor.getColumnIndex("provincia");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(columnIndexId);
                double latitud = cursor.getDouble(columnIndexLatitud);
                double longitud = cursor.getDouble(columnIndexLongitud);
                String nombre = cursor.getString(columnIndexNombre);
                String direccion = cursor.getString(columnIndexDireccion);
                double codigoPostal = cursor.getDouble(columnIndexCodigoPostal);
                String provincia = cursor.getString(columnIndexProvincia);

                Fuente fuente = new Fuente(id, latitud, longitud, nombre, direccion, codigoPostal, provincia);
                listaFuentes.add(fuente);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return listaFuentes;
    }

    public Fuente getFuenteFromDatabase(int idFuente) {
        SQLiteDatabase db = this.getReadableDatabase();
        Fuente fuente = null;

        Cursor cursor = db.query(
                "fuentes",
                null,
                "id = ?",
                new String[]{String.valueOf(idFuente)},
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndexId = cursor.getColumnIndex("id");
                int columnIndexLatitud = cursor.getColumnIndex("latitud");
                int columnIndexLongitud = cursor.getColumnIndex("longitud");
                int columnIndexNombre = cursor.getColumnIndex("nombre");
                int columnIndexDireccion = cursor.getColumnIndex("direccion");
                int columnIndexCodigoPostal = cursor.getColumnIndex("codigopostal");
                int columnIndexProvincia = cursor.getColumnIndex("provincia");

                int id = cursor.getInt(columnIndexId);
                double latitud = cursor.getDouble(columnIndexLatitud);
                double longitud = cursor.getDouble(columnIndexLongitud);
                String nombre = cursor.getString(columnIndexNombre);
                String direccion = cursor.getString(columnIndexDireccion);
                double codigoPostal = cursor.getDouble(columnIndexCodigoPostal);
                String provincia = cursor.getString(columnIndexProvincia);

                fuente = new Fuente(id, latitud, longitud, nombre, direccion, codigoPostal, provincia);
            }
            cursor.close();
        }

        if (fuente == null) {
            Log.d("BottomSheetFragment", "La fuente obtenida de la base de datos es nula");
        } else {
            Log.d("BottomSheetFragment", "Fuente encontrada en la base de datos: " + fuente.toString());
        }

        return fuente;
    }

    public boolean esIdFuenteValido(int idFuente) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM fuentes WHERE id = ?", new String[]{String.valueOf(idFuente)});
        boolean existeId = cursor != null && cursor.moveToFirst();

        if (cursor != null) {
            cursor.close();
        }

        return existeId;
    }

}