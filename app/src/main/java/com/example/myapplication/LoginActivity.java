package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
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

import java.util.zip.Inflater;

public class LoginActivity extends AppCompatActivity {
    private EditText username,password, usernameForgot, PhoneForgot, passwordForgot;
    private long countStaff;
    private Button signInBtn, forgotBtn,resetBtn;
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
                            Intent main = new Intent(LoginActivity.this, MainActivity.class);
                            main.putExtra("KEY_Display_Name",  itemSnapshot.child("DisplayName").getValue().toString());
                            startActivity(main);
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
    public void ForgotPassword(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_forgotpassword, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        usernameForgot = dialogView.findViewById(R.id.forgot_username_edittext);
        passwordForgot = dialogView.findViewById(R.id.forgot_new_password_edittext);
        PhoneForgot = dialogView.findViewById(R.id.forgot_phone_edittext);
        resetBtn = dialogView.findViewById(R.id.reset_password_btn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String un = usernameForgot.getText().toString();
                String pw = passwordForgot.getText().toString();
                String cmnd = PhoneForgot.getText().toString();
                if(TextUtils.isEmpty(un) || TextUtils.isEmpty(cmnd) || TextUtils.isEmpty(pw)){
                    Toast.makeText(LoginActivity.this,"Phải nhập đầy đủ thông tin",Toast.LENGTH_LONG).show();
                    return;
                }
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Staff");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot itemSnapshot: snapshot.getChildren()){
                            if(itemSnapshot.exists()){
                                String a = itemSnapshot.child("username").getValue().toString();
                                String b = itemSnapshot.child("phoneNumber").getValue().toString();
                                if( a.equals(un)&& b.equals(cmnd)){
                                    myRef.child(itemSnapshot.getKey()).child("password").setValue(pw);
                                    alertDialog.cancel();
                                    break;
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "Resetting...", Toast.LENGTH_SHORT).show();
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
        });
    }
    public void AnhXa(){
        //Login
        username = findViewById(R.id.usernameLogin);
        password = findViewById(R.id.passwordLogin);
        signInBtn = findViewById(R.id.signInBtn);
        forgotBtn = findViewById(R.id.forgotPasswordBtn);

        //Forgot
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        forgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgotPassword();
            }
        });
    }

}