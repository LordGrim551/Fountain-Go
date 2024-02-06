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
import com.example.fountainandgo.FRAGMENTS_ADMIN.Agregar_Admin_Fragment;
import com.example.fountainandgo.FRAGMENTS_ADMIN.Correos_Admin_Fragment;
import com.example.fountainandgo.FRAGMENTS_ADMIN.Mapa_Admin_Fragment;
import com.example.fountainandgo.FRAGMENTS_ADMIN.Perfil_Admin_Fragment;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class mapa_admin extends AppCompatActivity implements OnMyLocationButtonClickListener, OnMyLocationClickListener, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    BottomNavigationView navigationView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mapa_admin);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_admin);
        mapFragment.getMapAsync(this);

        navigationView = findViewById(R.id.bottomNavigationViewAdmin);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                if (item.getItemId() == R.id.mapa_buttom_admin) {
                    fragment = new Mapa_Admin_Fragment();
                }else if (item.getItemId() == R.id.mail_buttom_admin) {
                    fragment = new Correos_Admin_Fragment();
                } else if (item.getItemId() == R.id.agregar_buttom_admin) {
                    fragment = new Agregar_Admin_Fragment();
                } else if (item.getItemId() == R.id.perfil_buttom_admin) {
                    fragment = new Perfil_Admin_Fragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.map_admin, fragment).addToBackStack(null).commit();
                return true;
            }
        });
    }
    //MAPA , HABILITAR LOCALIZACION , Y BUSQUEDA DE LOCALIZACION DEL USUARIO

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

    //MENSAJE CUANDO SE PULSA SU POSICION EN EL MAPA
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    //MOSTRAR LAS COORDENADAS DE SU POSICIÓN ACTUAL
    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    //COMPROBAR DE QUE LOS PERMISOS DE LOCALIZACIÓN ESTÉN GARARANTIZADOS
    //UNA VEZ QUE LOS PERMISOS ESTÉN GARANTIZADOS SE MOSTRARÁ SU UBICACIÓN EN EL MAPA
    //DE LO CONTRARIO NO SE VA A MOSTRAR EN EL MAPA
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
        //VERIFICA SI LOS PERMISOS SE HAN CONDECIDO EN EL CASO DE QUE NO SE HAYAN CONCEDIDO LOS PERMISOS SE MOSTRARÁ EL MENSAJE
        // QUE GENERARÁ showMissingPermissionError
        super.onResumeFragments();
        if (permissionDenied) {
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        // Muestra un mensaje de error al usuario
        Toast.makeText(this, "FALTAN LOS PERMISOS PORFAVOR CONCEDA LOS PERMISOS", Toast.LENGTH_SHORT).show();
    }

    public static void requestLocationPermissions(Activity activity, int requestCode) {
        //ENVIAR LA SOLICITUD PARA PEDIR AL USUARIO LOS PERMISOS DE LOCALIZACIÓN
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                requestCode);
    }

    public static boolean isPermissionGranted(String[] permissions, int[] grantResults, String permission) {
        //PETTICION DE PERMISOS
        for (int i = 0; i < permissions.length; i++) {
            if (permission.equals(permissions[i])) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
        }
        return false;
    }
}
