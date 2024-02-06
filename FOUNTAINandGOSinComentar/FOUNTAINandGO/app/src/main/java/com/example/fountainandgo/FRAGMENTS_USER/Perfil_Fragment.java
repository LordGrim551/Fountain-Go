package com.example.fountainandgo.FRAGMENTS_USER;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.fountainandgo.R;
import com.example.fountainandgo.SCREENS.LoginScreen;
import com.google.android.material.textview.MaterialTextView;

public class Perfil_Fragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public Perfil_Fragment() {}

    public static Perfil_Fragment newInstance(String param1, String param2) {
        Perfil_Fragment fragment = new Perfil_Fragment();
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

    public void changeToEdit(View view) {
        EditarInfo_Fragment editFrag = new EditarInfo_Fragment();

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, editFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil_, container, false);

        /*SharedPreferences pref = requireContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        int id = pref.getInt("id", getId());
        String name = pref.getString("nombre", "");
        String surname = pref.getString("apellidos", "");
        String user = pref.getString("usuario", "");
        String pass = pref.getString("contrasena", "");
        String pass2 = pref.getString("confcontrasena", "");
        String email = pref.getString("email", "");*/

        MaterialTextView nombreTextView = view.findViewById(R.id.nameUserDisplay);
        MaterialTextView userTextView = view.findViewById(R.id.nameUserCoolDisplay);
        MaterialTextView emailTextView = view.findViewById(R.id.input_email);
        MaterialTextView surnameTextView = view.findViewById(R.id.input_surname);

        //loadUserProfileData();

        /*nombreTextView.setText(name);
        userTextView.setText(user);
        emailTextView.setText(email);
        surnameTextView.setText(surname);*/

        View yourButton = view.findViewById(R.id.editButton);
        yourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToEdit(view);
            }
        });

        View newButton = view.findViewById(R.id.close_button);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSession(view);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadUserProfileData();
    }

    private void loadUserProfileData() {
        SharedPreferences pref1 = requireContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        int id = pref1.getInt("id", getId());
        String name = pref1.getString("nombre", "");
        String surname = pref1.getString("apellidos", "");
        String user = pref1.getString("usuario", "");
        String pass = pref1.getString("contrasena", "");
        String pass2 = pref1.getString("confcontrasena", "");
        String email = pref1.getString("email", "");

        MaterialTextView nombreTextView = getView().findViewById(R.id.nameUserDisplay);
        MaterialTextView userTextView = getView().findViewById(R.id.nameUserCoolDisplay);
        MaterialTextView emailTextView = getView().findViewById(R.id.input_email);
        MaterialTextView surnameTextView = getView().findViewById(R.id.input_surname);

        if (getView() != null) {
            nombreTextView.setText(name);
            userTextView.setText(user);
            emailTextView.setText(email);
            surnameTextView.setText(surname);
        }
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