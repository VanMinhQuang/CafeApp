package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityAdminBinding;
import com.example.myapplication.databinding.ActivityOrderBinding;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class OrderActivity extends AppCompatActivity {
    private ActivityOrderBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_drink_order,  R.id.navigation_result_order)
                .build();
        BottomNavigationView bottomNavigationView = binding.navOrder;
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_order);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navOrder, navController);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bottom_nav_order, menu);
        return true;
    }
    public void AnhXa(){

    }
}