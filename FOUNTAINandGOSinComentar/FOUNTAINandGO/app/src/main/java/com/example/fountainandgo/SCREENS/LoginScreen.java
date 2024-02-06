package com.example.fountainandgo.SCREENS;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.fountainandgo.DATABASE.Datos;
import com.example.fountainandgo.FRAGMENTS_USER.Mapa_Fragment;
import com.example.fountainandgo.FRAGMENTS_USER.Perfil_Fragment;
import com.example.fountainandgo.R;
import com.example.fountainandgo.mapa;
import com.example.fountainandgo.mapa_admin;
import com.example.fountainandgo.mapa_noacc;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginScreen extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();

        MaterialButton accessNowButton = findViewById(R.id.accessNow);
        accessNowButton.setOnClickListener(this::existValues);
    }

    public void existValues(View view) {
        //Obtener la referencia del loginemail y del loginpass a través de su id
        TextInputLayout existEmailLayout = findViewById(R.id.loginEmail);
        TextInputLayout existPassLayout = findViewById(R.id.loginPass);

        // Obtener datos de entrada de texto dentro de los elementos
        TextInputEditText existEmailEditText = (TextInputEditText) existEmailLayout.getEditText();
        TextInputEditText existPassEditText = (TextInputEditText) existPassLayout.getEditText();

        // Verificar haber si no existen si no lo estan obtener el gmail y el la contraseña en laq base de datos
        //if (existEmailEditText != null && existPassEditText != null) {
        String existEmailString = existEmailEditText.getText().toString();
        String existPassString = existPassEditText.getText().toString();

        SQLiteDatabase db = new Datos(this).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email ='" + existEmailString + "' AND contrasena='" + existPassString + "'", null);

        boolean entro = false;
        boolean admin = false;

        if (db != null && !existEmailString.trim().isEmpty() && !existPassString.trim().isEmpty() && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String surname = cursor.getString(2);
                String user = cursor.getString(3);
                String pass = cursor.getString(4);
                String pass2 = cursor.getString(5);
                String email = cursor.getString(6);

                if ((existEmailString.equals("lukinda551@gmail.com") && existPassString.equals("12345")) || (existEmailString.equals("seryiuwu@gmail.com") && existPassString.equals("12345"))) {
                    editor.putInt("id", id);
                    editor.putString("nombre", name);
                    editor.putString("apellidos", surname);
                    editor.putString("usuario", user);
                    editor.putString("confcontrasena", pass2);
                    editor.putString("email", existEmailString);
                    editor.putString("contrasena", existPassString);
                    editor.commit();
                    Toast.makeText(this, "Bienvenido admin " + existEmailString, Toast.LENGTH_LONG).show();
                    Intent nIntent = new Intent(LoginScreen.this, mapa_admin.class);
                    startActivity(nIntent);
                    admin = true;
                    entro = true;
                } else if (existEmailString.equals(email) && existPassString.equals(pass)) {
                    editor.putInt("id", id);
                    editor.putString("nombre", name);
                    editor.putString("apellidos", surname);
                    editor.putString("usuario", user);
                    editor.putString("confcontrasena", pass2);
                    editor.putString("email", existEmailString);
                    editor.putString("contrasena", existPassString);
                    editor.commit();
                    Toast.makeText(this, "Bienvenido " + existEmailString, Toast.LENGTH_LONG).show();
                    Intent nIntent = new Intent(LoginScreen.this, mapa.class);
                    startActivity(nIntent);
                    entro = true;
                } else {
                    entro = false;
                    admin = false;
                }
            } while (cursor.moveToNext());
            cursor.close();

            if (entro == false) {
                Toast.makeText(this, "El usuario " + existEmailString + " no existe", Toast.LENGTH_LONG).show();
            }

            if (admin) {
                Toast.makeText(this, "Recuerda hacer un uso responsable de tus funciones de admin, " + existEmailString, Toast.LENGTH_LONG).show();
            }

            // Limpiar los TextInputLayout
            existEmailLayout.getEditText().setText("");
            existPassLayout.getEditText().setText("");
        } else {
            Toast.makeText(this, "Todos los datos deben ser completados", Toast.LENGTH_LONG).show();
            existEmailLayout.getEditText().setText("");
            existPassLayout.getEditText().setText("");
        }
    }

    public void changeToRegister(View view) {
        Intent nIntent = new Intent(LoginScreen.this, RegisterScreen.class);
        startActivity(nIntent);
    }

    public void noAccount(View view) {
        Intent nIntent = new Intent(LoginScreen.this, mapa_noacc.class);
        startActivity(nIntent);
    }
}