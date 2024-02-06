package com.example.fountainandgo.FRAGMENTS_USER;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fountainandgo.DATABASE.Datos;
import com.example.fountainandgo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditarInfo_Fragment extends Fragment {

    private TextInputLayout nameTextInputLayout;
    private TextInputLayout surnameTextInputLayout;
    private TextInputLayout userTextInputLayout;
    private TextInputLayout passTextInputLayout;
    private TextInputLayout pass2TextInputLayout;
    private TextInputLayout emailTextInputLayout;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public EditarInfo_Fragment() {}

    public static EditarInfo_Fragment newInstance(String param1, String param2) {
        EditarInfo_Fragment fragment = new EditarInfo_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void BackToUser(View view) {
        Perfil_Fragment perfilFrag = new Perfil_Fragment();

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_edit, perfilFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void modifyUser(View view) {

        SharedPreferences pref = requireContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        int id = pref.getInt("id", getId());
        String name = pref.getString("nombre", "");
        String surname = pref.getString("apellidos", "");
        String user = pref.getString("usuario", "");
        String pass = pref.getString("contrasena", "");
        String pass2 = pref.getString("confcontrasena", "");
        String email = pref.getString("email", "");

        TextInputEditText nameEditText = (TextInputEditText) nameTextInputLayout.getEditText();
        TextInputEditText surnameEditText = (TextInputEditText) surnameTextInputLayout.getEditText();
        TextInputEditText userEditText = (TextInputEditText) userTextInputLayout.getEditText();
        TextInputEditText passEditText = (TextInputEditText) passTextInputLayout.getEditText();
        TextInputEditText pass2EditText = (TextInputEditText) pass2TextInputLayout.getEditText();
        TextInputEditText emailEditText = (TextInputEditText) emailTextInputLayout.getEditText();

        String updateName = Objects.requireNonNull(nameEditText.getText()).toString();
        String updateSurname = Objects.requireNonNull(surnameEditText.getText()).toString();
        String updateUser = Objects.requireNonNull(userEditText.getText()).toString();
        String updatePass = Objects.requireNonNull(passEditText.getText()).toString();
        String updatePass2 = Objects.requireNonNull(pass2EditText.getText()).toString();
        String updateEmail = Objects.requireNonNull(emailEditText.getText()).toString();

        SQLiteDatabase db = new Datos(requireContext()).getWritableDatabase();

        if (db != null && !updateName.trim().isEmpty() && !updateSurname.trim().isEmpty() && !updateUser.trim().isEmpty() && !updatePass.trim().isEmpty() && !updatePass2.trim().isEmpty() && !updateEmail.trim().isEmpty() && updatePass.equals(updatePass2)) {
            ContentValues values = new ContentValues();
            values.put("nombre", updateName);
            values.put("apellidos", updateSurname);
            values.put("usuario", updateUser);
            values.put("contrasena", updatePass);
            values.put("confcontrasena", updatePass2);
            values.put("email", updateEmail);
            String [] lastValues = {name, surname, user, pass, pass2, email};
            long mod = db.update("users", values, "nombre = ? AND apellidos = ? AND usuario = ? AND contrasena = ? AND confcontrasena = ? AND email = ?", lastValues);

            if (mod > 0) {
                Toast.makeText(requireContext(), "Usuario correctamente modificado", Toast.LENGTH_LONG).show();
                nameEditText.setText("");
                surnameEditText.setText("");
                userEditText.setText("");
                passEditText.setText("");
                pass2EditText.setText("");
                emailEditText.setText("");
                db.close();
            } else {
                Toast.makeText(requireContext(), "No se pudo modificar el usuario indicado", Toast.LENGTH_LONG).show();
            }

            FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();
            Map<String, String> users = new HashMap<>();
            users.put("nombre", updateName);
            users.put("apellidos", updateSurname);
            users.put("usuario", updateUser);
            users.put("contrasena", updatePass);
            users.put("confcontrasena", updatePass2);
            users.put("email", updateEmail);

            String newDocumentName = updateName;

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

            firestoreDb.collection("users").document(name).delete();

            SharedPreferences pref1 = requireContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("id", getId());
            editor.putString("nombre", updateName);
            editor.putString("apellidos", updateSurname);
            editor.putString("usuario", updateUser);
            editor.putString("contrasena", updatePass);
            editor.putString("confcontrasena", updatePass2);
            editor.putString("email", updateEmail);
            editor.apply();

        }  else {
            Toast.makeText(requireContext(), "Debe rellenar todos los datos de un usuario existente para continuar", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editar_info_, container, false);

        nameTextInputLayout = view.findViewById(R.id.changeName);
        surnameTextInputLayout = view.findViewById(R.id.changeSurname);
        userTextInputLayout = view.findViewById(R.id.changeUser);
        passTextInputLayout = view.findViewById(R.id.changePass);
        pass2TextInputLayout = view.findViewById(R.id.changePass2);
        emailTextInputLayout = view.findViewById(R.id.changeEmail);

        View yourButton = view.findViewById(R.id.backUser);
        yourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackToUser(view);
            }
        });

        View yourButton2 = view.findViewById(R.id.applyChanges);
        yourButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyUser(view);
            }
        });

        return view;
    }
}