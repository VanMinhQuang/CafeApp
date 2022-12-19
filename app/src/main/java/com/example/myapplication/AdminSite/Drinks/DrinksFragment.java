package com.example.myapplication.AdminSite.Drinks;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.databinding.FragmentDrinksBinding;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.ui.home.HomeViewModel;


public class DrinksFragment extends Fragment {

    private FragmentDrinksBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DrinkViewModel drinkViewModel =
                new ViewModelProvider(this).get(DrinkViewModel.class);

        binding = FragmentDrinksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDrink;
        drinkViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}