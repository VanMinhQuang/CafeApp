package com.example.myapplication.AdminSite.Staff;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.databinding.FragmentStaffBinding;


public class StaffFragment extends Fragment {

    private FragmentStaffBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StaffViewModel staffViewModel =
                new ViewModelProvider(this).get(StaffViewModel.class);

        binding = FragmentStaffBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textStaff;
        staffViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}