package com.example.fountainandgo.FRAGMENTS_NOACC;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fountainandgo.DATABASE.Fuentes;
import com.example.fountainandgo.R;

public class Favoritos_Noacc_Fragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public Favoritos_Noacc_Fragment() {}

    public static Favoritos_Noacc_Fragment newInstance(String param1, String param2) {
        Favoritos_Noacc_Fragment fragment = new Favoritos_Noacc_Fragment();
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

    public void showElements(View view) {
        SQLiteDatabase dbf = new Fuentes(view.getContext()).getReadableDatabase();

        // Asignar el resultado de findViewById a una variable
        LinearLayout fillContentShow = view.findViewById(R.id.fillContentShow);

        Cursor fuentes = dbf.rawQuery("SELECT * FROM Fuentes", null);

        if (fuentes.moveToFirst()) {
            StringBuilder dataText = new StringBuilder();
            do {
                int id = fuentes.getInt(0);

                String Latitud = fuentes.getString(1);
                String Longitud = fuentes.getString(2);
                String NombreFuente = fuentes.getString(3);
                String DirFuente = fuentes.getString(4);
                String cpFuente = fuentes.getString(5);
                String ciudadFuente = fuentes.getString(6);

                dataText.append("Latitud: ").append(Latitud).append("\n")
                        .append("Longitud: ").append(Longitud).append("\n")
                        .append("Nombre Fuente:").append(NombreFuente).append("\n")
                        .append("Dirección : ").append(DirFuente).append("\n")
                        .append("Codigo Postal: ").append(cpFuente).append("\n")
                        .append("Ciudad: ").append(ciudadFuente).append("\n");

            } while (fuentes.moveToNext());

            TextView dataTextView_nacc_fav = view.findViewById(R.id.dataTextView_nacc_fav);
            dataTextView_nacc_fav.setText(dataText.toString());

            // Hacer algo con fillContentShow si es necesario
            // fillContentShow.setVisibility(View.VISIBLE); por ejemplo
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoritos_noacc, container, false);

        Button showButton = view.findViewById(R.id.showbutton);
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showElements(view);
            }
        });
        return view;
    }

}