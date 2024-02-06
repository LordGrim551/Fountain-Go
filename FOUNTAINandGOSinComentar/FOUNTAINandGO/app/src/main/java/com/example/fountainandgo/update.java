package com.example.fountainandgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fountainandgo.DATABASE.Datos;
import com.example.fountainandgo.FRAGMENTS_ADMIN.Perfil_Admin_Fragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class update extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);
    }

    public void changeToMain(View view) {
        Intent nIntent = new Intent(update.this, Perfil_Admin_Fragment.class);
        startActivity(nIntent);
    }

    public void modifyValues(View view){
        TextView updateName = findViewById(R.id.updateName);
        TextView updateSurname = findViewById(R.id.updateSurname);
        TextView updateUser = findViewById(R.id.updateUser);
        TextView updatePass = findViewById(R.id.updatePass);
        TextView updateEmail = findViewById(R.id.updateEmail);
        TextView newName = findViewById(R.id.editNewName);
        TextView newSurname = findViewById(R.id.editNewSurname);
        TextView newUser = findViewById(R.id.editNewUser);
        TextView newPass = findViewById(R.id.editNewPass);
        TextView newEmail = findViewById(R.id.editNewEmail);

        String nameString = updateName.getText().toString();
        String surnameString = updateSurname.getText().toString();
        String userString = updateUser.getText().toString();
        String passString = updatePass.getText().toString();
        String emailString = updateEmail.getText().toString();
        String newNameString = newName.getText().toString();
        String newSurnameString = newSurname.getText().toString();
        String newUserString = newUser.getText().toString();
        String newPassString = newPass.getText().toString();
        String newEmailString = newEmail.getText().toString();

        SQLiteDatabase db = new Datos(this).getWritableDatabase();

        if(db != null && !nameString.trim().isEmpty() && !surnameString.trim().isEmpty() && !userString.trim().isEmpty() && !passString.trim().isEmpty()
                && !emailString.trim().isEmpty() && !newNameString.trim().isEmpty() && !newSurnameString.trim().isEmpty() && !newUserString.trim().isEmpty()
                && !newPassString.trim().isEmpty() && !newEmailString.trim().isEmpty()) {
            ContentValues values = new ContentValues();
            values.put("nombre", newNameString);
            values.put("apellidos", newSurnameString);
            values.put("usuario", newUserString);
            values.put("contrasena", newPassString);
            values.put("confcontrasena", newPassString);
            values.put("email", newEmailString);
            String [] si = {nameString, surnameString, userString, passString, emailString};
            long mod = db.update("users", values, "nombre = ? AND apellidos = ? AND usuario = ? AND contrasena = ? AND email = ?", si);

            if(mod > 0) {
                Toast.makeText(this, "Usuario correctamente modificado", Toast.LENGTH_LONG).show();
                newName.setText("");
                newSurname.setText("");
                newUser.setText("");
                newPass.setText("");
                newEmail.setText("");
                updateName.setText("");
                updateSurname.setText("");
                updateUser.setText("");
                updatePass.setText("");
                updateEmail.setText("");
                db.close();
            } else {
                Toast.makeText(this, "No se pudo modificar el usuario indicado", Toast.LENGTH_LONG).show();
            }

            FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();
            Map<String, String> users = new HashMap<>();
            users.put("nombre", newNameString);
            users.put("apellidos", newSurnameString);
            users.put("usuario", newUserString);
            users.put("contrasena", newPassString);
            users.put("confcontrasena", newPassString);
            users.put("email", newEmailString);

            String newDocumentName = newNameString;

            firestoreDb.collection("users").document(newDocumentName)
                    .set(users)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("DEBUG", "De super puta madre socio");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ERROR", "Inutil");
                        }
                    });

            firestoreDb.collection("users").document(nameString).delete();
        } else {
            Toast.makeText(this, "Debe rellenar todos los datos de un usuario existente para continuar", Toast.LENGTH_LONG).show();
        }
    }
}