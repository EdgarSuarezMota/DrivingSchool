package org.edgarsuarezmota.informer.drivingschool.category;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.navigation.NavigationView;

import org.edgarsuarezmota.informer.drivingschool.NetworkUtils;
import org.edgarsuarezmota.informer.drivingschool.api.ApiService;
import org.edgarsuarezmota.informer.drivingschool.api.RetrofitClient;
import org.edgarsuarezmota.informer.drivingschool.main.MainActivity;
import org.edgarsuarezmota.informer.drivingschool.R;
import org.edgarsuarezmota.informer.drivingschool.result.ResultActivity;
import org.edgarsuarezmota.informer.drivingschool.databinding.ActivityCategoryBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActivityCategoryBinding binding;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.top_bar_questions);
        setSupportActionBar(toolbar);
        // Configuración del RecyclerView
        binding.rvCategory.setLayoutManager(new LinearLayoutManager(this));
        if (NetworkUtils.isNetworkAvailable(this)) {


        // Inicializar Retrofit
        ApiService serviceApi = RetrofitClient.getInstance().create(ApiService.class);

        // Hacer la llamada a la API
        Call<CategoryResponse> call = serviceApi.getCategoryResponse();
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful()) {
                    CategoryResponse categoryResponse = response.body();
                    if (categoryResponse != null && categoryResponse.getTemas() != null) {
                        List<String> categoryList = new ArrayList<>();
                        for (CategoryResponse.Tema tema : categoryResponse.getTemas()) {
                            categoryList.add(tema.getCategoria());
                        }
                        adapter = new CategoryAdapter(CategoryActivity.this, categoryList);
                        binding.rvCategory.setAdapter(adapter);
                        adapter.setOnCategoryClickListener(category -> {
                            Intent intent = new Intent(CategoryActivity.this, QuestionsTopicsActivity.class);
                            intent.putExtra("categoria", category);
                            startActivity(intent);
                        });
                        alternarVistas();
                    } else {
                        mostrarAlerta("Error", "No se pudo obtener las categorias");
                    }
                } else {

                    mostrarAlertaSinWifi("Error", "No se pudo conectar al servidor");                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Toast.makeText(CategoryActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        } else {
            mostrarAlertaSinWifi("Error", "No hay conexion a internet");
        }
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.preguntas_temas);// Marcar la primera opción como seleccionada
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // Manejar las selecciones de menú aquí

                if (menuItem.getItemId() == R.id.preguntas_generales) {
                    Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (menuItem.getItemId() == R.id.aciertos) {
                    Intent intent = new Intent(CategoryActivity.this, ResultActivity.class);
                    intent.putExtra("resultado", "aciertos");
                    startActivity(intent);
                    finish();
                } else if (menuItem.getItemId() == R.id.errores) {
                    Intent intent = new Intent(CategoryActivity.this, ResultActivity.class);
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
    private void alternarVistas() {

        // Oculta la vista para ocultar
        binding.cpiQuestions.setVisibility(View.GONE);
        // Muestra la vista para mostrar
        binding.rvCategory.setVisibility(View.VISIBLE);
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
    private void mostrarAlerta(String titulo, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Html.fromHtml("<font color='#000000'>" + titulo + "</font>"))
                .setMessage(Html.fromHtml("<font color='#000000'>" + mensaje + "</font>"))
                .setPositiveButton(Html.fromHtml("<font color='#990000'>Aceptar</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Cierra la alerta
                        dialog.dismiss();
                    }
                });

        // Establecer el fondo blanco para el diálogo
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.white);

        alertDialog.show();
    }
}
