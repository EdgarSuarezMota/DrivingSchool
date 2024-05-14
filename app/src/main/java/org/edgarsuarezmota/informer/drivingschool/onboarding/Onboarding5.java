package org.edgarsuarezmota.informer.drivingschool.onboarding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import org.edgarsuarezmota.informer.drivingschool.main.MainActivity;
import org.edgarsuarezmota.informer.drivingschool.R;

public class Onboarding5 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View rootView = inflater.inflate(R.layout.activity_onboarding5, container, false);

        // Obtener una referencia al botón desde el diseño del fragmento
        Button button = rootView.findViewById(R.id.btn_exit_onboarding);

        // Configurar un OnClickListener para el botón
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Obtener el objeto SharedPreferences
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);

                // Guardar el nuevo número en SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("onboarding", 1);
                editor.apply();

                // Crear un intent para la actividad deseada
                Intent intent = new Intent(getActivity(), MainActivity.class);
                // Iniciar la actividad
                startActivity(intent);
                // Finalizar la actividad actual
                getActivity().finish();
            }
        });

        return rootView;
    }
}