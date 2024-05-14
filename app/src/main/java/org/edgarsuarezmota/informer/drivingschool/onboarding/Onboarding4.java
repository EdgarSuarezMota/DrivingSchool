package org.edgarsuarezmota.informer.drivingschool.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.edgarsuarezmota.informer.drivingschool.R;

public class Onboarding4 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View rootView = inflater.inflate(R.layout.activity_onboarding4, container, false);
        // Aquí puedes inicializar y configurar las vistas de tu fragmento
        return rootView;
    }
}