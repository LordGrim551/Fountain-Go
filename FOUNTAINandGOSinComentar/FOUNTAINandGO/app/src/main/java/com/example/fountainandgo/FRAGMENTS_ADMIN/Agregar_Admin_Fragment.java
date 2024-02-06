package com.example.fountainandgo.FRAGMENTS_ADMIN;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Agregar_Admin_Fragment extends Fragment {

    private TextInputLayout fontNameTextInputLayout;
    private TextInputLayout fontDirTextInputLayout;
    private TextInputLayout fontCpTextInputLayout;
    private TextInputLayout fontCityTextInputLayout;
    private TextInputLayout latitudTextInputLayout;
    private TextInputLayout longitudTextInputLayout;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public Agregar_Admin_Fragment() {}

    public static Agregar_Admin_Fragment newInstance(String param1, String param2) {
        Agregar_Admin_Fragment fragment = new Agregar_Admin_Fragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_admin, container, false);

        fontNameTextInputLayout = view.findViewById(R.id.addFontName);
        fontDirTextInputLayout = view.findViewById(R.id.addFontDir);
        fontCpTextInputLayout = view.findViewById(R.id.addFontCP);
        fontCityTextInputLayout = view.findViewById(R.id.addFontCity);
        latitudTextInputLayout = view.findViewById(R.id.addFontLatitud);
        longitudTextInputLayout = view.findViewById(R.id.addFontLongitud);

        View yourButton = view.findViewById(R.id.addFontButton);
        yourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarFuente(view);
            }
        });

        return view;
    }

    public void agregarFuente(View view) {
        TextInputEditText fontNameEditText = (TextInputEditText) fontNameTextInputLayout.getEditText();
        TextInputEditText fontDirEditText = (TextInputEditText) fontDirTextInputLayout.getEditText();
        TextInputEditText fontCpEditText = (TextInputEditText) fontCpTextInputLayout.getEditText();
        TextInputEditText fontCityEditText = (TextInputEditText) fontCityTextInputLayout.getEditText();
        TextInputEditText latitudEditText = (TextInputEditText) latitudTextInputLayout.getEditText();
        TextInputEditText longitudEditText = (TextInputEditText) longitudTextInputLayout.getEditText();

        String newFontName = Objects.requireNonNull(fontNameEditText.getText().toString());
        String newFontDir = Objects.requireNonNull(fontDirEditText.getText().toString());
        double newFontCp = Double.parseDouble(Objects.requireNonNull(fontCpEditText.getText().toString()));
        String newFontCity = Objects.requireNonNull(fontCityEditText.getText().toString());
        double newFontLatitud = Double.parseDouble(Objects.requireNonNull(latitudEditText.getText().toString()));
        double newFontLongitud = Double.parseDouble(Objects.requireNonNull(longitudEditText.getText().toString()));

        SQLiteDatabase db = new Datos(requireContext()).getWritableDatabase();

        if (db != null && !newFontName.trim().isEmpty() && !newFontDir.trim().isEmpty() && !newFontCity.trim().isEmpty()) {

            try {
                double latitud = newFontLatitud;
                double longitud = newFontLongitud;
                double codigoPostal = newFontCp;

                ContentValues valuesf = new ContentValues();
                valuesf.put("latitud", latitud);
                valuesf.put("longitud", longitud);
                valuesf.put("nombre", newFontName);
                valuesf.put("direccion", newFontDir);
                valuesf.put("codigopostal", codigoPostal);
                valuesf.put("provincia", newFontCity);

                long res = db.insert("fuentes", null, valuesf);
                if (res >= 0) {
                    Cursor cursor = db.rawQuery("SELECT MAX(id) FROM fuentes", null);
                    if (cursor != null && cursor.moveToFirst()) {
                        long lastInsertedId = cursor.getLong(0); // Obtener el último ID insertado
                        cursor.close();
                        Toast.makeText(requireContext(), "Fuente correctamente agregada con ID: " + lastInsertedId, Toast.LENGTH_SHORT).show(); }
                    fontNameEditText.setText("");
                    fontDirEditText.setText("");
                    fontCpEditText.setText("");
                    fontCityEditText.setText("");
                    latitudEditText.setText("");
                    longitudEditText.setText("");
                } else {
                    Toast.makeText(requireContext(), "Error: la fuente no pudo crearse", Toast.LENGTH_SHORT).show();
                }

                FirebaseFirestore firestoreDbf = FirebaseFirestore.getInstance();
                Map<String, Object> fuentes = new HashMap<>();
                fuentes.put("id", getId());
                fuentes.put("latitud", newFontLatitud);
                fuentes.put("longitud", newFontLongitud);
                fuentes.put("nombre", newFontName);
                fuentes.put("direccion", newFontDir);
                fuentes.put("codigopostal", newFontCp);
                fuentes.put("provincia", newFontCity);

                firestoreDbf.collection("fuentes").document(newFontName)
                        .set(fuentes)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("DEBUG", "Bien, creaste fuente");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("ERROR", "Mal, no se creo la fuente");
                            }
                        });
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Los valores de latitud, longitud o código postal no son válidos", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(requireContext(), "Todos los campos deben ser completados", Toast.LENGTH_LONG).show();
        }
    }
}