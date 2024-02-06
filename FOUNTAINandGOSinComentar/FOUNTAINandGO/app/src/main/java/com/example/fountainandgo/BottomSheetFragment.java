package com.example.fountainandgo;

import android.content.ContentValues;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.example.fountainandgo.DATABASE.Datos;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    private double latitud;
    private double longitud;
    private int idFuente;
    private String nameFont;
    private String dirFont;
    private String cpFont;
    private String cityFont;
    private TextView nombreTextView;

    public BottomSheetFragment() {}

    public static BottomSheetFragment newInstance(double latitud, double longitud, int idFuente) {
        BottomSheetFragment fragment = new BottomSheetFragment();
        Bundle args = new Bundle();
        args.putInt("idFuente", idFuente);
        args.putDouble("latitud", latitud);
        args.putDouble("longitud", longitud);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener las coordenadas desde los argumentos
        if (getArguments() != null) {
            latitud = getArguments().getDouble("latitud", 0);
            longitud = getArguments().getDouble("longitud", 0);
            idFuente = getArguments().getInt("idFuente", 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        //View love = view.findViewById(R.id.favButton); //busqueda del icono

        TextView latitudFont = view.findViewById(R.id.num_latitud);
        TextView longitudFont = view.findViewById(R.id.num_longitud);
        TextView nameFont = view.findViewById(R.id.nameFont);
        TextView dirFont = view.findViewById(R.id.dirFont);
        TextView cpFont = view.findViewById(R.id.cpFont);
        TextView cityFont = view.findViewById(R.id.cityFont);

        Datos datos = new Datos(requireContext());
        Log.d("DEBUG", "fuenteID: " + idFuente);

        boolean esValido = datos.esIdFuenteValido(idFuente);
        if (esValido) {
            Datos.Fuente fuente = datos.getFuenteFromDatabase(idFuente);
            if (fuente != null) {
                latitudFont.setText(String.valueOf(fuente.getLatitud()));
                longitudFont.setText(String.valueOf(fuente.getLongitud()));
                nameFont.setText(fuente.getNombre());
                dirFont.setText(fuente.getDireccion());
                cpFont.setText(String.valueOf(fuente.getCodigoPostal()));
                cityFont.setText(fuente.getProvincia());
            } else {
                Toast.makeText(requireContext(), "Esta fuente no existe", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "El ID de la fuente no es válido", Toast.LENGTH_SHORT).show();
        }

        /*love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarCoordenadas();

                Double DoubleLatitud = latitud;
                Double DoubleLongitud = longitud;
                String nombreFuente = nameFont.getText().toString();
                String dirFuente = dirFont.getText().toString();
                String PostalCode = cpFont.getText().toString();
                String FuenteCiudad = cityFont.getText().toString();
                Log.d("DEBUG", "DoubleLatitud: " + DoubleLatitud);
                Log.d("DEBUG", "DoubleLongitud: " + DoubleLongitud);
                Log.d("DEBUG", "nombreFuente: " + nombreFuente);

                SQLiteDatabase dbf = new Fuentes(requireContext()).getWritableDatabase();
                if (dbf != null && !DoubleLatitud.toString().isEmpty() && !DoubleLongitud.toString().isEmpty() && !nombreFuente.trim().isEmpty() && !dirFuente.trim().isEmpty() && !PostalCode.trim().isEmpty() && !FuenteCiudad.trim().isEmpty()) {
                    ContentValues fvalues = new ContentValues();
                    fvalues.put("latitud", DoubleLatitud);
                    fvalues.put("longitud", DoubleLongitud);
                    fvalues.put("nombre", String.valueOf(nombreFuente));
                    fvalues.put("direccion", String.valueOf(dirFuente));
                    fvalues.put("codigopostal", PostalCode);
                    fvalues.put("provincia", String.valueOf(FuenteCiudad));

                    long resF = dbf.insert("fuentes", null, fvalues);
                    if (resF >= 0) {
                        Toast.makeText(requireContext(), "Coordenadas agregadas correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Las coordenadas no han podido ser agregadas", Toast.LENGTH_SHORT).show();
                    }
                }

                FirebaseFirestore firestoreDbf = FirebaseFirestore.getInstance();
                Map<String, Object> fuentes = new HashMap<>();
                fuentes.put("latitud", latitud);
                fuentes.put("longitud", longitud);
                fuentes.put("nombre", nameFontString);
                fuentes.put("direccion", dirFontString);
                fuentes.put("codigopostal", Integer.parseInt(cpFontString));
                fuentes.put("provincia", cityFontString);
                firestoreDbf.collection("fuentes").document(nameFontString)
                        .set(fuentes)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("DEBUG", "Bien");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("ERROR", "Mal");
                            }
                        });
            }
        });*/

        return view;
    }

    /* void mostrarCoordenadas() {
        Toast.makeText(requireContext(), "Latitud : " + latitud + ", Longitud: " + longitud, Toast.LENGTH_SHORT).show();
        if (getActivity() instanceof mapa_noacc) {
            ((mapa_noacc) getActivity()).actualizarCoordenadas(latitud, longitud);
        }
    }*/

    public void setFuente(double latitud, double longitud, String nameFont, String dirFont, String cpFont, String cityFont) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.nameFont = nameFont;
        this.dirFont = dirFont;
        this.cpFont = cpFont;
        this.cityFont = cityFont;                                                   
    }
    public void setNombreFuente(String nombreFuente) {
        // Cambia la fuente para el TextView correspondiente
        nombreTextView.setText(nombreFuente);
        // Puedes considerar cambiar la fuente directamente en el TextView, por ejemplo:
        // nombreTextView.setTypeface(Typeface.create("tu_tipo_de_fuente", Typeface.NORMAL));
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        View bottomSheet = (View) view.getParent();
        bottomSheet.setBackgroundTintMode(PorterDuff.Mode.CLEAR);
        bottomSheet.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        bottomSheet.setBackgroundColor(Color.TRANSPARENT);

        //borde redondeado
        ConstraintLayout mainLayout = view.findViewById(R.id.bottomSheet);

        // fondo con esquinas redondeadas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mainLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corners));
        } else {
            mainLayout.setBackgroundResource(R.drawable.rounded_corners);
        }

        /*ImageView fondo = view.findViewById(R.id.circulines);
        ImageView imageViewFuente1 = view.findViewById(R.id.fuente1_imagen);
        MaterialTextView textViewNombre1 = view.findViewById(R.id.nameFont);
        MaterialTextView textViewDir1 = view.findViewById(R.id.dirFont);
        MaterialTextView textViewCity1 = view.findViewById(R.id.cityFont);
        ImageView imageViewCorazon = view.findViewById(R.id.love_ic_fuente1);
        ImageView imageViewWarning = view.findViewById(R.id.rep_fuente1_ic);

        textViewNombre1.getText();
        textViewDir1.getText();
        textViewCity1.getText();

        // Puedes ajustar las imágenes según sea necesario
        fondo.getDrawable();
        imageViewFuente1.getDrawable();
        imageViewCorazon.getDrawable();
        imageViewWarning.getDrawable();*/
    }
}