package com.example.fountainandgo.SCREENS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.fountainandgo.DATABASE.Datos;
import com.example.fountainandgo.R;
import com.example.fountainandgo.mapa_noacc;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void changeToLogin(View view) {
        Intent nIntent = new Intent(RegisterScreen.this, LoginScreen.class);
        startActivity(nIntent);
    }

    public void noAccount(View view) {
        Intent nIntent = new Intent(RegisterScreen.this, mapa_noacc.class);
        startActivity(nIntent);
    }

    public void insertValues(View v) {
        TextInputLayout nameTextInputLayout = findViewById(R.id.registerName);
        TextInputLayout surnameTextInputLayout = findViewById(R.id.registerSurname);
        TextInputLayout userTextInputLayout = findViewById(R.id.registerUser);
        TextInputLayout passTextInputLayout = findViewById(R.id.registerPass);
        TextInputLayout pass2TextInputLayout = findViewById(R.id.registerPass2);
        TextInputLayout emailTextInputLayout = findViewById(R.id.registerEmail);

        TextInputEditText nameEditText =  (TextInputEditText) nameTextInputLayout.getEditText();
        TextInputEditText surnameEditText = (TextInputEditText) surnameTextInputLayout.getEditText();
        TextInputEditText userEditText = (TextInputEditText) userTextInputLayout.getEditText();
        TextInputEditText PassEditText = (TextInputEditText) passTextInputLayout.getEditText();
        TextInputEditText pass2EditText = (TextInputEditText) pass2TextInputLayout.getEditText();
        TextInputEditText EmailEditText = (TextInputEditText) emailTextInputLayout.getEditText();

        String nameString = nameEditText.getText().toString();
        String surnameString = surnameEditText.getText().toString();
        String userString = userEditText.getText().toString();
        String passString = PassEditText.getText().toString();
        String pass2String = pass2EditText.getText().toString();
        String emailString = EmailEditText.getText().toString();

        Datos aux = new Datos(RegisterScreen.this);
        SQLiteDatabase db = aux.getWritableDatabase();

        if (db != null && !nameString.trim().isEmpty() && !surnameString.trim().isEmpty() && !userString.trim().isEmpty() && !passString.trim().isEmpty() && !pass2String.trim().isEmpty() && !emailString.trim().isEmpty()) {
            ContentValues values = new ContentValues();
            values.put("nombre", nameString);
            values.put("apellidos", surnameString);
            values.put("usuario", userString);
            values.put("contrasena", passString);
            values.put("confcontrasena", pass2String);
            values.put("email", emailString);

            long res = db.insert("users", null, values);
            if (res >= 0) {
                Toast.makeText(this, "Usuario correctamente creado", Toast.LENGTH_LONG).show();
                nameEditText.setText("");
                surnameEditText.setText("");
                userEditText.setText("");
                PassEditText.setText("");
                pass2EditText.setText("");
                EmailEditText.setText("");
                Intent nIntent = new Intent(RegisterScreen.this, LoginScreen.class);
                startActivity(nIntent);
            } else {
                Toast.makeText(this, "Error: el usuario no pudo crearse", Toast.LENGTH_LONG).show();
            }

            FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();
            Map<String, String> users = new HashMap<>();
            users.put("nombre", nameString);
            users.put("apellidos", surnameString);
            users.put("usuario", userString);
            users.put("contrasena", passString);
            users.put("confcontrasena", pass2String);
            users.put("email", emailString);

            firestoreDb.collection("users").document(nameString)
                    .set(users)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("DEBUG", "Bien");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ERROR", "Mal");
                        }
                    });
        }  else {
            Toast.makeText(this, "Todos los campos deben ser completados", Toast.LENGTH_LONG).show();
        }
    }
}
