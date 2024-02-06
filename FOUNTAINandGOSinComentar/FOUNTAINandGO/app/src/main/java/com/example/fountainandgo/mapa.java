package com.example.fountainandgo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.fountainandgo.DATABASE.Datos;
import com.example.fountainandgo.FRAGMENTS_USER.Agregar_Fragment;
import com.example.fountainandgo.FRAGMENTS_USER.Correo_Fragment;
import com.example.fountainandgo.FRAGMENTS_USER.Favoritos_Fragment;
import com.example.fountainandgo.FRAGMENTS_USER.Mapa_Fragment;
import com.example.fountainandgo.FRAGMENTS_USER.Perfil_Fragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class mapa extends AppCompatActivity implements OnMyLocationButtonClickListener, OnMyLocationClickListener, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    BottomNavigationView navigationView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mapa_);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        navigationView = findViewById(R.id.bottomNavigationView);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                if (item.getItemId() == R.id.mapa_buttom) {
                    fragment = new Mapa_Fragment();
                } else if (item.getItemId() == R.id.mail_buttom) {
                    fragment = new Correo_Fragment();
                } else if (item.getItemId() == R.id.agregar_buttom) {
                    fragment = new Agregar_Fragment();
                } else if (item.getItemId() == R.id.favorito_buttom) {
                    fragment = new Favoritos_Fragment();
                } else if (item.getItemId() == R.id.perfil_buttom) {
                    fragment = new Perfil_Fragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.mapa_screen, fragment).addToBackStack(null).commit();
                return true;
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setOnMyLocationButtonClickListener(this);
        map.setOnMyLocationClickListener(this);
        enableMyLocation(this);
    }

    @SuppressLint("MissingPermission")
    private void enableMyLocation(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            Datos datos = new Datos(context);
            ArrayList<Datos.Fuente> listaFuentes = datos.obtenerFuentes();
            for (Datos.Fuente fuente : listaFuentes) {
                LatLng ubicacion = new LatLng(fuente.getLatitud(), fuente.getLongitud());
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(ubicacion)
                        .title(fuente.getNombre())
                        .snippet(String.valueOf(fuente.getId()));

                // Añadir el marcador al mapa
                map.addMarker(markerOptions);
            }

            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    // Mostrar el panel deslizante con la información del marcador
                    String snippet = marker.getSnippet();
                    int idFuente = Integer.parseInt(snippet);

                    Log.d("DEBUG", "ID de la fuente: " + idFuente);

                    double latitud = marker.getPosition().latitude;
                    double longitud = marker.getPosition().longitude;

                    BottomSheetFragment bottomSheetFragment = BottomSheetFragment.newInstance(latitud, longitud, idFuente);
                    bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                    return true;
                }
            });
        } else {
            requestLocationPermissions((Activity) context, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION) ||
                    isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                enableMyLocation(this);
            } else {
                permissionDenied = true;
            }
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        // Muestra un mensaje de error al usuario
    }

    public static void requestLocationPermissions(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                requestCode);
    }

    public static boolean isPermissionGranted(String[] permissions, int[] grantResults, String permission) {
        for (int i = 0; i < permissions.length; i++) {
            if (permission.equals(permissions[i])) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
        }
        return false;
    }
}
