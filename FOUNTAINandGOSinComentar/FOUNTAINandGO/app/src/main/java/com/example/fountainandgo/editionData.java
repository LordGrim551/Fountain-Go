package com.example.fountainandgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fountainandgo.FRAGMENTS_USER.Perfil_Fragment;

public class editionData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edition_data);
    }
    public void applychanges(View view){
        Intent nIntent = new Intent(editionData.this, Perfil_Fragment.class);
        startActivity(nIntent);

    }
    public void changeToperfilSincambios(View view) {
        Intent nIntent = new Intent(editionData.this, Perfil_Fragment.class);
        startActivity(nIntent);
    }
}