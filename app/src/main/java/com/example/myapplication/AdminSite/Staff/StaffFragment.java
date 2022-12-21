package com.example.myapplication.AdminSite.Staff;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Model.Staff;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentStaffBinding;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class StaffFragment extends Fragment {

    private FragmentStaffBinding binding;
    private EditText txtID, txtUser, txtPass, txtCerti, txtPhone, txtAddress;
    private Spinner spinPosition;
    private Button btnPush;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        anhxa(view);

        ArrayAdapter<String> spinArray;
        List<String> listSpin = new ArrayList<String>();
        listSpin.add("Mananger");
        listSpin.add("Bartender");
        listSpin.add("Waiter");
        listSpin.add("Guard");
        spinArray = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, listSpin);
        spinArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPosition.setAdapter(spinArray);
        btnPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAddStaff();
            }
        });

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStaffBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    private void onClickAddStaff(){


        int id = Integer.parseInt(txtID.getText().toString());
        String username = txtUser.getText().toString();
        String password =txtPass.getText().toString();
        String phone = txtPhone.getText().toString();
        String address = txtAddress.getText().toString();
        String position = spinPosition.getSelectedItem().toString();
        int certificate = Integer.parseInt(txtCerti.getText().toString());



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Staff/" +id);
        Staff staff = new Staff(id,username,password,address,phone, certificate, position);

        myRef.setValue(staff, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getContext(), "Push success", Toast.LENGTH_LONG);
            }
        });



    }
    private void anhxa(View view){
        txtID = view.findViewById(R.id.txtStaffID);
        txtUser = view.findViewById(R.id.txtStaffUserName);
        txtPass = view.findViewById(R.id.txtStaffPassword);
        txtCerti = view.findViewById(R.id.txtStaffCertificate);
        spinPosition = view.findViewById(R.id.spinStaffPosition);
        txtPhone = view.findViewById(R.id.txtStaffPhoneNumber);
        txtAddress = view.findViewById(R.id.txtStaffAddress);
        btnPush = view.findViewById(R.id.btnPush);

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}