package org.edgarsuarezmota.informer.drivingschool;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.edgarsuarezmota.informer.drivingschool.databinding.ActivityMainBinding;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private static final String BASE_URL = "https://firebasestorage.googleapis.com/v0/b/testscoche-90c7b.appspot.com/o/";
    private static final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.top_bar_questions);
        setSupportActionBar(toolbar);
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
                if (menuItem.getItemId() == R.id.preguntas_generales) {

                } else if (menuItem.getItemId() == R.id.preguntas_temas) {
                    // Acción para la opción 2
                } else if (menuItem.getItemId() == R.id.errores) {
                    // Acción para la opción 3
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

}
