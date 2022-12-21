package com.example.myapplication.AdminSite.Staff;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapter.StaffAdapter;
import com.example.myapplication.Model.Staff;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentStaffBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class StaffFragment extends Fragment {

    private FragmentStaffBinding binding;
    private EditText txtID, txtUser, txtPass, txtCerti, txtPhone, txtAddress;
    private FloatingActionButton btnSave;
    private Button btnPush;
    private Spinner spinPosition;
    private RecyclerView rcvStaff;
    private StaffAdapter adapter;
    private List<Staff> lstStaff;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        anhxa(view);
        lstStaff = new ArrayList<>();
        adapter = new StaffAdapter(lstStaff);
        rcvStaff.setAdapter(adapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View viewDialogStaff = LayoutInflater.from(getContext()).inflate(R.layout.dialog_staff, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(viewDialogStaff);
                AlertDialog alert = builder.create();
                alert.show();
                ArrayAdapter<String> spinArray;

                txtID = viewDialogStaff.findViewById(R.id.txtStaffID);
                txtUser = viewDialogStaff.findViewById(R.id.txtStaffUserName);
                txtPass = viewDialogStaff.findViewById(R.id.txtStaffPassword);
                txtCerti = viewDialogStaff.findViewById(R.id.txtStaffCertificate);
                spinPosition = viewDialogStaff.findViewById(R.id.spinStaffPosition);
                txtPhone = viewDialogStaff.findViewById(R.id.txtStaffPhoneNumber);
                txtAddress = viewDialogStaff.findViewById(R.id.txtStaffAddress);
                btnPush = viewDialogStaff.findViewById(R.id.btnPush);

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
                        alert.dismiss();
                    }
                });
            }
        });
        getAllStaff();


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
                Toast.makeText(getContext(), "Push success", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getAllStaff(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Staff");

        /*myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(lstStaff != null)
                    lstStaff.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Staff staff = dataSnapshot.getValue(Staff.class);
                    lstStaff.add(staff);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Get Staff Failed!!!", Toast.LENGTH_SHORT).show();
            }
        });*/
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Staff staff = snapshot.getValue(Staff.class);
                if(staff != null){
                    lstStaff.add(staff);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Staff staff = snapshot.getValue(Staff.class);
                if(staff == null || lstStaff == null || lstStaff.isEmpty())
                    return;

                for(int i=0;i<lstStaff.size();i++){
                    if(staff.getId() == lstStaff.get(i).getId()){
                        lstStaff.set(i, staff);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void anhxa(View view){
        rcvStaff = view.findViewById(R.id.rcvStaff);
        btnSave = view.findViewById(R.id.floatingbtnAddStaff);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvStaff.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        rcvStaff.addItemDecoration(dividerItemDecoration);


    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}