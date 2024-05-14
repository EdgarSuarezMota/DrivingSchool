package org.edgarsuarezmota.informer.drivingschool.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.edgarsuarezmota.informer.drivingschool.R;
import org.edgarsuarezmota.informer.drivingschool.category.CategoryActivity;
import org.edgarsuarezmota.informer.drivingschool.databinding.ActivityMainBinding;
import org.edgarsuarezmota.informer.drivingschool.result.ResultActivity;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private static final String PREFS_FILE_NAME = "MyPrefsFile";
    private static final String PREF_UUID = "UUID";
    private SharedPreferences sharedPreferences;
    private String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.top_bar_questions);
        setSupportActionBar(toolbar);

        // Inicializa SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);

        // Lee el UUID guardado en SharedPreferences
        uuid = sharedPreferences.getString(PREF_UUID, null);

        // Si no hay un UUID guardado, genera uno nuevo y guárdalo
        if (uuid == null) {
            uuid = generateUUID();
            saveUUID(uuid);
        }

        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí se crea un Intent para iniciar una nueva actividad
                Intent intent = new Intent(MainActivity.this, QuestionsActivity.class);
                startActivity(intent);
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.preguntas_generales);// Marcar la primera opción como seleccionada
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // Manejar las selecciones de menú aquí

                if (menuItem.getItemId() == R.id.preguntas_temas) {
                    Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                    startActivity(intent);
                    finish();
                } else if (menuItem.getItemId() == R.id.aciertos) {
                    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                    intent.putExtra("resultado", "aciertos");
                    startActivity(intent);
                    finish();
                } else if (menuItem.getItemId() == R.id.errores) {
                    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
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

    // Generar un UUID único
    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    // Guardar el UUID en SharedPreferences
    private void saveUUID(String uuid) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_UUID, uuid);
        editor.apply();
    }
}
