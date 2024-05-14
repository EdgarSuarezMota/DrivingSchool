package org.edgarsuarezmota.informer.drivingschool.onboarding;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class OnboardingPagerAdapter extends FragmentStatePagerAdapter {

    public OnboardingPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Onboarding1();
            case 1:
                return new Onboarding2();
            case 2:
                return new Onboarding3();
            case 3:
                return new Onboarding4();
            case 4:
                return new Onboarding5();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 5; // Número total de páginas del Onboarding
    }
}

