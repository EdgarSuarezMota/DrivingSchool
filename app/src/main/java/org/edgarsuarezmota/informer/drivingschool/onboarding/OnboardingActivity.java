package org.edgarsuarezmota.informer.drivingschool.onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;


import org.edgarsuarezmota.informer.drivingschool.R;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout indicatorLayout;
    private int numPages; // Número total de páginas del ViewPager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        // Obtén una referencia al ViewPager desde el layout
        viewPager = findViewById(R.id.viewPager);
        // Obtén una referencia al LinearLayout del indicador desde el layout
        indicatorLayout = findViewById(R.id.indicatorLayout);

        // Crea un adaptador de actividades personalizado
        OnboardingPagerAdapter adapter = new OnboardingPagerAdapter(getSupportFragmentManager());
        numPages = adapter.getCount(); // Obtiene el número total de páginas

        // Establece el adaptador en el ViewPager
        viewPager.setAdapter(adapter);

        // Crea un arreglo de ImageView para almacenar los puntos del indicador
        ImageView[] indicators = new ImageView[numPages];

        // Itera sobre el número de páginas y crea un ImageView para cada punto
        for (int i = 0; i < numPages; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageResource(R.drawable.b); // Establece la imagen por defecto

            // Ajusta los márgenes de los puntos para separarlos un poco
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(8, 0, 8, 0); // Ajusta los márgenes según tu preferencia
            indicators[i].setLayoutParams(layoutParams);

            // Agrega el punto al LinearLayout del indicador
            indicatorLayout.addView(indicators[i]);
        }

        // Marca la primera opción como seleccionada al iniciar la actividad
        indicators[0].setImageResource(R.drawable.a);

        // Agrega un escuchador al ViewPager para actualizar los puntos del indicador al cambiar de página
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // Itera sobre los puntos del indicador y actualiza su apariencia
                for (int i = 0; i < numPages; i++) {
                    if (i == position) {
                        indicators[i].setImageResource(R.drawable.a); // Cambia el punto seleccionado
                    } else {
                        indicators[i].setImageResource(R.drawable.b); // Restaura los demás puntos
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
