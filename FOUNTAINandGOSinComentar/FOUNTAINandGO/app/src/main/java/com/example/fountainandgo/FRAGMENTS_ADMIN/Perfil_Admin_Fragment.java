package com.example.fountainandgo.FRAGMENTS_ADMIN;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.fountainandgo.R;
import com.example.fountainandgo.SCREENS.LoginScreen;
import com.google.android.material.textview.MaterialTextView;

public class Perfil_Admin_Fragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public Perfil_Admin_Fragment() {}

    public static Perfil_Admin_Fragment newInstance(String param1, String param2) {
        Perfil_Admin_Fragment fragment = new Perfil_Admin_Fragment();
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil_admin, container, false);

        SharedPreferences pref = requireContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        int id = pref.getInt("id", getId());
        String name = pref.getString("nombre", "");
        String surname = pref.getString("apellidos", "");
        String user = pref.getString("usuario", "");
        String pass = pref.getString("contrasena", "");
        String pass2 = pref.getString("confcontrasena", "");
        String email = pref.getString("email", "");

        MaterialTextView nombreTextView = view.findViewById(R.id.nameAdminDisplay);
        MaterialTextView userTextView = view.findViewById(R.id.nameAdminCoolDisplay);
        MaterialTextView emailTextView = view.findViewById(R.id.input_email_admin);
        MaterialTextView surnameTextView = view.findViewById(R.id.input_surname_admin);

        nombreTextView.setText(name);
        userTextView.setText(user);
        emailTextView.setText(email);
        surnameTextView.setText(surname);

        View newButton = view.findViewById(R.id.close_button_admin);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSession(v);
            }
        });

        return view;
    }

    public void closeSession(View view) {
        SharedPreferences pref = requireContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear(); // quitar los datos
        editor.apply(); // aplicar

        Intent intent = new Intent(requireContext(), LoginScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // vaciar todas las actividades abiertas y abrir una nueva instancia de LoginScreen sin datos por detrás
        startActivity(intent);
        requireActivity().finish();
    }
}