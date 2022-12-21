package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.Model.Staff;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText username,password;
    private long countStaff;
    private Button signInBtn, signUpBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AnhXa();


    }

    public void login(){
        String un, pw;
        un = username.getText().toString();
        pw = password.getText().toString();
        if(TextUtils.isEmpty(un) || TextUtils.isEmpty(pw)){
            Toast.makeText(this,"Phải nhập đầy đủ thông tin",Toast.LENGTH_LONG).show();
            return;
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Staff");
        //Do Login
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemSnapshot: snapshot.getChildren()){
                    if(itemSnapshot.exists()){
                        String a = itemSnapshot.child("username").getValue().toString();
                        String b = itemSnapshot.child("password").getValue().toString();
                        if( a.equals(un)&& b.equals(pw)){
                            Intent admin = new Intent(LoginActivity.this, AdminActivity.class);
                            startActivity(admin);
                            break;
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Logging...", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Sai thông tin đăng nhập", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void AnhXa(){
        username = findViewById(R.id.usernameLogin);
        password = findViewById(R.id.passwordLogin);
        signInBtn = findViewById(R.id.signInBtn);
        signUpBtn = findViewById(R.id.signUpBtn);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }
}