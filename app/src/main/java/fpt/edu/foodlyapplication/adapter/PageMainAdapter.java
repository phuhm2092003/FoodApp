package fpt.edu.foodlyapplication.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import fpt.edu.foodlyapplication.fragment.CartFragment;
import fpt.edu.foodlyapplication.fragment.HomeFragment;
import fpt.edu.foodlyapplication.fragment.ProfileFragment;

public class PageMainAdapter extends FragmentStateAdapter {

    public PageMainAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new CartFragment();
            case 2:
                return new ProfileFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
