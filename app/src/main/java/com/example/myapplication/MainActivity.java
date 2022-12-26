package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.Model.Staff;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    TextView displayNameHeader, positionHeader;
    String displayName, position, uri, username, password, address, phoneNumber, id;
    CircleImageView imageStaff;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AnhXa();
        int i = 0;
        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navHeaderView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void AnhXa(){
        Intent intent = getIntent();
        displayName= intent.getExtras().getString("KEY_Display_Name");
        position = intent.getExtras().getString("KEY_Position");
        uri = intent.getExtras().getString("KEY_URI");
        id = intent.getExtras().getString("KEY_ID");
        username = intent.getExtras().getString("KEY_UN");
        password = intent.getExtras().getString("KEY_PW");
        phoneNumber = intent.getExtras().getString("KEY_PHONE");
        address = intent.getExtras().getString("KEY_ADDRESS");
        NavigationView navigationView = binding.navHeaderView;
        View headerView = navigationView.getHeaderView(0);

        displayNameHeader = headerView.findViewById(R.id.displayNameHeaderText);
        positionHeader = headerView.findViewById(R.id.positionHeaderText);
        imageStaff = headerView.findViewById(R.id.imageHeader);

        displayNameHeader.setText(displayName);
        positionHeader.setText(position);
        Picasso.get().load(uri).into(imageStaff);

        imageStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileMove();
            }
        });
    }

    public void ProfileMove(){
        Intent profile = new Intent(MainActivity.this, ProfileActivity.class);
        profile.putExtra("KEY_Display_Name",  displayName);
        profile.putExtra("KEY_Position",  position);
        profile.putExtra("KEY_URI", uri);
        profile.putExtra("KEY_ADDRESS",  address);
        profile.putExtra("KEY_ID",  id);
        profile.putExtra("KEY_PHONE",  phoneNumber);
        profile.putExtra("KEY_UN",  username);
        profile.putExtra("KEY_PW",  password);
        startActivity(profile);
    }
}