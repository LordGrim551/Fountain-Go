package com.example.fountainandgo.SCREENS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fountainandgo.DATABASE.Datos;
import com.example.fountainandgo.R;
import com.example.fountainandgo.delete;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
    }

    public void showElements(View view) {
        SQLiteDatabase db = new Datos(this).getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM users", null);
        LinearLayout layout = findViewById(R.id.fillContentShow);

        if (cursor.moveToFirst()) {
            StringBuilder dataText = new StringBuilder();
            do {
                String email = cursor.getString(0);
                String name = cursor.getString(1);
                String surname = cursor.getString(2);
                String user = cursor.getString(3);


                dataText.append("Nombre: ").append(name).append("\n")
                        .append("Apellidos: ").append(surname).append("\n")
                        .append("Usuario: ").append(user).append("\n")
                        .append("Email: ").append(email).append("\n\n");
            } while (cursor.moveToNext());

            TextView dataTextView = findViewById(R.id.dataTextView);
            dataTextView.setText(dataText.toString());
        }
    }

    public void toDelete (View view) {
        Intent nIntent = new Intent(MainScreen.this, delete.class);
        startActivity(nIntent);
    }



    public void toLogin (View view) {
        Intent nIntent = new Intent(MainScreen.this, LoginScreen.class);
        startActivity(nIntent);
    }


}