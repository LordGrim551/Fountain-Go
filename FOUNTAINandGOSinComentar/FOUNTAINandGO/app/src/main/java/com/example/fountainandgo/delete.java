package com.example.fountainandgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class delete extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
    }

    public void changeToMain(View view) {
        Intent nIntent = new Intent(delete.this, Perfil_Admin_Fragment.class);
        startActivity(nIntent);
    }

    public void deleteElements(View view){
        TextView nameView = findViewById(R.id.deleteName); //el deleteName es el nombre del TextView
        TextView surnameView = findViewById(R.id.deleteSurname);
        TextView userView = findViewById(R.id.deleteUser);
        TextView passView = findViewById(R.id.deletePass);
        TextView pass2View = findViewById(R.id.deletePass2);
        TextView emailView = findViewById(R.id.deleteEmail);

        String nameString = nameView.getText().toString();
        String surnameString = surnameView.getText().toString();
        String userString = userView.getText().toString();
        String passString = passView.getText().toString();
        String pass2String = pass2View.getText().toString();
        String emailString = emailView.getText().toString();

        SQLiteDatabase db = new Datos(this).getWritableDatabase();

        if(db != null && !nameString.trim().isEmpty() && !surnameString.trim().isEmpty() && !userString.trim().isEmpty()
                && !passString.trim().isEmpty() && !pass2String.trim().isEmpty() && !emailString.trim().isEmpty()) {
            long del = db.delete("users", "nombre = '" + nameString + "'and apellidos = '" + surnameString +
                    "'and usuario = '" + userString + "'and contrasena = '" + passString + "'and confcontrasena = '" + pass2String +
                    "'and email = '" + emailString + "'", null );

            if(del > 0) {
                Toast.makeText(this, "Usuario correctamente eliminado", Toast.LENGTH_LONG).show();
                nameView.setText("");
                surnameView.setText("");
                userView.setText("");
                passView.setText("");
                pass2View.setText("");
                emailView.setText("");
                db.close();
            } else {
                Toast.makeText(this, "No se pudo eliminar el usuario indicado", Toast.LENGTH_LONG).show();
            }

            FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();
            Map <String, String> users = new HashMap<>();
            users.remove("nombre", nameString);
            users.remove("apellidos", surnameString);
            users.remove("usuario", userString);
            users.remove("contrasena", passString);
            users.remove("confcontrasena", pass2String);
            users.remove("email", emailString);


            firestoreDb.collection("users").document(nameString)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("DEBUG", "De lokisimos mano"); }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ERROR", "Vaya tontito");
                        }
                    });
        } else {
            Toast.makeText(this, "Debe rellenar todos los datos de un usuario existente para continuar", Toast.LENGTH_LONG).show();
        }
    }
}