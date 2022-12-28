package com.example.myapplication;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Model.Staff;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    TextView positionTV, maNV;
    EditText passwordET, addressET, phoneET,displayNameET;
    Button changeBtn;
    CircleImageView profileImage;
    String displayName, position, uri, username,password, address, phoneNumber, id;
    ActivityResultLauncher<String> launcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        AnhXa();
    }




    public void AnhXa(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Staff");
        //Lấy dữ liệu từ Main Activity
        Intent intent = getIntent();
        displayName= intent.getExtras().getString("KEY_Display_Name");
        position = intent.getExtras().getString("KEY_Position");
        uri = intent.getExtras().getString("KEY_URI");
        id = intent.getExtras().getString("KEY_ID");
        password = intent.getExtras().getString("KEY_PW");
        phoneNumber = intent.getExtras().getString("KEY_PHONE");
        address = intent.getExtras().getString("KEY_ADDRESS");
        username = intent.getExtras().getString("KEY_UN");
        Staff s = new Staff(Integer.parseInt(id),username,password,address,phoneNumber, displayName, position, uri);
        //Khởi tạo đường dẫn cho các biến
        profileImage = findViewById(R.id.imageProfile);
        maNV = findViewById(R.id.idNameProfile);
        displayNameET = findViewById(R.id.displayNameProfile);
        phoneET = findViewById(R.id.phoneProfile);
        addressET = findViewById(R.id.addressProfile);
        passwordET = findViewById(R.id.passwordProfile);
        positionTV = findViewById(R.id.positionProfile);
        changeBtn = findViewById(R.id.changeProfileBtn);
        //Set các giá trị
        Picasso.get().load(uri).into(profileImage);
        maNV.setText(id);
        displayNameET.setText(displayName);
        phoneET.setText(phoneNumber);
        addressET.setText(address);
        passwordET.setText(password);
        positionTV.setText(position);
        maNV.setEnabled(false);
        displayNameET.setEnabled(false);
        positionTV.setEnabled(false);


        //set các thay đổi trong Edit Text và enable Change Button
        AnhXaEditText();
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cập nhật lại database và xml
                String newPass = passwordET.getText().toString().trim();
                String newPhone = phoneET.getText().toString().trim();
                String newAddress = addressET.getText().toString().trim();

                s.setPassword(newPass);
                s.setAddress(newAddress);
                s.setPhoneNumber(newPhone);
                myRef.child(String.valueOf(s.getId())).updateChildren(s.toMap(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(ProfileActivity.this, "Update Staff Success  !!!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public void AnhXaEditText(){
        displayNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s.toString().trim().equals(displayName)){
                    changeBtn.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        phoneET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s.toString().trim().equals(phoneNumber)){
                    changeBtn.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        addressET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s.toString().trim().equals(address)){
                    changeBtn.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s.toString().trim().equals(password)){
                    changeBtn.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}