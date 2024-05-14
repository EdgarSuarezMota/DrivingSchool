package org.edgarsuarezmota.informer.drivingschool.result;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.edgarsuarezmota.informer.drivingschool.NetworkUtils;
import org.edgarsuarezmota.informer.drivingschool.main.MainActivity;
import org.edgarsuarezmota.informer.drivingschool.R;
import org.edgarsuarezmota.informer.drivingschool.category.CategoryActivity;
import org.edgarsuarezmota.informer.drivingschool.databinding.ActivityResultBinding;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActivityResultBinding binding;
    private RecyclerView recyclerView;
    private ResultAdapter adapter;
    private List<ResultItem> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.top_bar_results);
        setSupportActionBar(toolbar);
        recyclerView = binding.rvResults;
        resultList = new ArrayList<>();
        adapter = new ResultAdapter(this, resultList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        binding.cpiResults.setVisibility(View.VISIBLE);
        binding.rvResults.setVisibility(View.INVISIBLE);
        String result = getIntent().getStringExtra("resultado");
        if (result.equals("aciertos")) {
            binding.topBarResults.setTitle("Aciertos");
            if (NetworkUtils.isNetworkAvailable(this)) {
                obtenerDatosFirebaseAciertos();
            } else {
                mostrarAlertaSinWifi("Error", "No hay conexion a internet");
            }

        }else {
            binding.topBarResults.setTitle("Fallos");
            if (NetworkUtils.isNetworkAvailable(this)) {
                obtenerDatosFirebaseFallos();
            } else {
                mostrarAlertaSinWifi("Error", "No hay conexion a internet");
            }

        }
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.navigation_view);
        if (result.equals("aciertos")) {
            navigationView.setCheckedItem(R.id.aciertos);
        }else {
            navigationView.setCheckedItem(R.id.errores);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // Manejar las selecciones de menú aquí
                if (menuItem.getItemId() == R.id.preguntas_generales) {
                    Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (menuItem.getItemId() == R.id.preguntas_temas) {
                    Intent intent = new Intent(ResultActivity.this, CategoryActivity.class);
                    startActivity(intent);
                    finish();
                } else if (menuItem.getItemId() == R.id.aciertos) {
                    Intent intent = new Intent(ResultActivity.this, ResultActivity.class);
                    intent.putExtra("resultado", "aciertos");
                    startActivity(intent);
                    finish();
                } else if (menuItem.getItemId() == R.id.errores) {
                    Intent intent = new Intent(ResultActivity.this, ResultActivity.class);
                    intent.putExtra("resultado", "fallos");
                    startActivity(intent);
                    finish();
                }

                // Cerrar el Drawer después de seleccionar una opción
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void obtenerDatosFirebaseAciertos() {
        // Obtener el ID de usuario guardado en SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("UUID", ""); // Asegúrate de que "UUID" sea la clave correcta

        // Verificar si se ha guardado un ID válido en SharedPreferences
        if (!userId.isEmpty()) {
            // Obtener una referencia a la base de datos de Firebase para los fallos del usuario
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Aciertos").child(userId);

            // Realizar una consulta para obtener los datos
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    resultList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Aquí puedes procesar los datos de Firebase para los fallos del usuario
                        String texto = snapshot.child("texto").getValue(String.class);
                        int numero = snapshot.child("numero").getValue(Integer.class);
                        ResultItem resultItem = new ResultItem(texto, numero, "Aciertos:");
                        resultList.add(resultItem);
                    }
                    adapter.notifyDataSetChanged();
                    binding.cpiResults.setVisibility(View.INVISIBLE);
                    binding.rvResults.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("ResultActivity", "Error al obtener los datos de Firebase: " + databaseError.getMessage());
                }
            });
        } else {
            // El ID guardado en SharedPreferences es inválido o está vacío
            Log.e("ResultActivity", "No se encontró un ID válido en SharedPreferences");
        }
    }
    private void obtenerDatosFirebaseFallos() {
        // Obtener el ID de usuario guardado en SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("UUID", ""); // Asegúrate de que "UUID" sea la clave correcta

        // Verificar si se ha guardado un ID válido en SharedPreferences
        if (!userId.isEmpty()) {
            // Obtener una referencia a la base de datos de Firebase para los fallos del usuario
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Fallos").child(userId);

            // Realizar una consulta para obtener los datos
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    resultList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Aquí puedes procesar los datos de Firebase para los fallos del usuario
                        String texto = snapshot.child("texto").getValue(String.class);
                        int numero = snapshot.child("numero").getValue(Integer.class);
                        ResultItem resultItem = new ResultItem(texto, numero, "Fallos:");
                        resultList.add(resultItem);
                    }
                    adapter.notifyDataSetChanged();
                    binding.cpiResults.setVisibility(View.INVISIBLE);
                    binding.rvResults.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("ResultActivity", "Error al obtener los datos de Firebase: " + databaseError.getMessage());
                }
            });
        } else {
            // El ID guardado en SharedPreferences es inválido o está vacío
            Log.e("ResultActivity", "No se encontró un ID válido en SharedPreferences");
        }
    }
    private void mostrarAlertaSinWifi(String titulo, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Html.fromHtml("<font color='#000000'>" + titulo + "</font>"))
                .setMessage(Html.fromHtml("<font color='#000000'>" + mensaje + "</font>"))
                .setPositiveButton(Html.fromHtml("<font color='#990000'>Aceptar</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Cierra la alerta
                        dialog.dismiss();
                        // Cierra la aplicación por completo
                        finishAffinity();
                    }
                });

        // Establecer el fondo blanco para el diálogo
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.white);

        alertDialog.show();
    }

}
