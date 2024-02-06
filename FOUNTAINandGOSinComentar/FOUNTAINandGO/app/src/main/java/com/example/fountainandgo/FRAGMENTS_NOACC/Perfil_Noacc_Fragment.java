package com.example.fountainandgo.FRAGMENTS_NOACC;

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
import com.example.fountainandgo.SCREENS.RegisterScreen;

public class Perfil_Noacc_Fragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public Perfil_Noacc_Fragment() {}

    public static Perfil_Noacc_Fragment newInstance(String param1, String param2) {
        Perfil_Noacc_Fragment fragment = new Perfil_Noacc_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_perfil_noacc, container, false);

        View newButton = view.findViewById(R.id.close_button_noacc);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSession(v);
            }
        });

        View newButton2 = view.findViewById(R.id.logButton_logreg);
        newButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSession(v);
            }
        });

        View newButton3 = view.findViewById(R.id.regButton_logreg);
        newButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeToRegister(v);
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

    public void closeToRegister(View view) {
        SharedPreferences pref = requireContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear(); // quitar los datos
        editor.apply(); // aplicar

        Intent intent = new Intent(requireContext(), RegisterScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // vaciar todas las actividades abiertas y abrir una nueva instancia de LoginScreen sin datos por detrás
        startActivity(intent);
        requireActivity().finish();
    }
}