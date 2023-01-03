package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.Model.Staff;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.ui.gallery.GalleryFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity  {
    TextView displayNameHeader, positionHeader;
    String displayName, position, uri, username, password, address, phoneNumber, id;
    CircleImageView imageStaff;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    public static String name = "", ID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AnhXa();
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navHeaderView;


        imageStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileMove();
            }
        });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.admin_site, R.id.nav_employeeStatistic)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.admin_site){
                    if(TextUtils.equals(position,"Manager")){
                        Intent intent = new Intent(MainActivity.this,AdminActivity.class);
                        startActivity(intent);

                    }else{
                        Toast.makeText(MainActivity.this,"Khu vuc chi danh cho quan ly",Toast.LENGTH_SHORT).show();
                    }
                }else if(id == R.id.nav_slideshow){
                    Intent inent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(inent);
                }
                else{
                    NavigationUI.onNavDestinationSelected(item, navController);
                }
                drawer.close();
                return true;
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Cart");
        if(myRef.getKey() != null){
            myRef.child(MainActivity.ID).removeValue();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.order_action_bar_item:
                Intent order = new Intent(this, OrderActivity.class);
                startActivity(order);
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
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
        ID = id;
        name = displayName;
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