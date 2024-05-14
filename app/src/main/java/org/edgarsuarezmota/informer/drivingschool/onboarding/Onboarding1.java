package org.edgarsuarezmota.informer.drivingschool.onboarding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.edgarsuarezmota.informer.drivingschool.main.MainActivity;
import org.edgarsuarezmota.informer.drivingschool.R;

public class Onboarding1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View rootView = inflater.inflate(R.layout.activity_onboarding1, container, false);
        // Aquí puedes inicializar y configurar las vistas de tu fragmento

        // Obtener el objeto SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);

        // Obtener el número actual almacenado en SharedPreferences
        int numeroActual = sharedPreferences.getInt("onboarding", 0);
        if (numeroActual == 1) {
            // Crear un intent para la actividad deseada
            Intent intent = new Intent(getActivity(), MainActivity.class);
            // Iniciar la actividad
            startActivity(intent);
            getActivity().finish();
        }

        return rootView;
    }
}
