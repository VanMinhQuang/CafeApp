package com.example.myapplication.AdminSite.Schedule;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Adapter.ScheduleAdapter;
import com.example.myapplication.Model.Schedule;
import com.example.myapplication.Model.Staff;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentScheduleBinding;
import com.example.myapplication.databinding.FragmentStaffBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ScheduleFragment extends Fragment {
    private FragmentScheduleBinding binding;
    private ScheduleAdapter adapter;
    private RecyclerView rcvSchedule;
    private FloatingActionButton btnAdd;
    private Spinner barista1, barista2, waiter1, waiter2, guard, shift;
    private DatePicker datePickerDialog;
    private Button btnSave, btnCancel, btnDatePicker;
    private DatePickerDialog datePicker;
    private List<Schedule> lstSchedule;
    private List<String> lstBarista = new ArrayList<>();
    private List<String> lstWaiter = new ArrayList<>();
    private List<String> lstGuard = new ArrayList<>();
    private List<String> lstShift = new ArrayList<>();
    ArrayAdapter<String> spinArrayBarista;
    ArrayAdapter<String> spinArrayWaiter;
    ArrayAdapter<String> spinArrayGuard;
    ArrayAdapter<String> spinArrayShift;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentScheduleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initDatePicker();
        anhxa(root.getRootView());
        lstSchedule = new ArrayList<>();
        adapter = new ScheduleAdapter(lstSchedule);
        rcvSchedule.setAdapter(adapter);

        btnDatePicker.setText(getTodayDate());


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View viewDialogStaff = LayoutInflater.from(getContext()).inflate(R.layout.dialog_schedule, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(viewDialogStaff);
                AlertDialog alert = builder.create();
                alert.show();
                datePickerDialog = viewDialogStaff.findViewById(R.id.datePickerDialog);
                shift = viewDialogStaff.findViewById(R.id.spinScheduleShift);
                barista1 = viewDialogStaff.findViewById(R.id.spinScheduleStaffBarista);
                barista2 = viewDialogStaff.findViewById(R.id.spinScheduleStaffBarista2);
                waiter1 = viewDialogStaff.findViewById(R.id.spinScheduleStaffWaiter);
                waiter2 = viewDialogStaff.findViewById(R.id.spinScheduleStaffWaiter2);
                guard = viewDialogStaff.findViewById(R.id.spinScheduleStaffGuard);
                btnSave = viewDialogStaff.findViewById(R.id.btnPushSchedule);
                btnCancel = viewDialogStaff.findViewById(R.id.btnCancelSchedule);
                spinArrayBarista = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, lstBarista);
                getAllBarista();
                spinArrayBarista.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                barista1.setAdapter(spinArrayBarista);
                barista2.setAdapter(spinArrayBarista);
                spinArrayWaiter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, lstWaiter);
                getAllWaiter();
                spinArrayWaiter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                waiter2.setAdapter(spinArrayWaiter);
                waiter1.setAdapter(spinArrayWaiter);
                spinArrayGuard = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, lstGuard);
                getAllGuard();
                spinArrayBarista.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                guard.setAdapter(spinArrayGuard);
                lstShift.clear();

                lstShift.add("Ca 1");
                lstShift.add("Ca 2");
                lstShift.add("Ca 3");
                spinArrayShift = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, lstShift);
                spinArrayShift.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                shift.setAdapter(spinArrayShift);


                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickAddSchedule();
                        alert.dismiss();
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });
            }
        });
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show();
            }
        });


        return root;
    }

    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);

    }

    public void onClickAddSchedule(){
        int day = datePickerDialog.getDayOfMonth();
        int month = datePickerDialog.getMonth() + 1;
        int year = datePickerDialog.getYear();
        String dateDialog = day +"-"+ month + "-"+year;
        String shiftDialog = shift.getSelectedItem().toString();
        String baristaDialog = barista1.getSelectedItem().toString() +","+ barista2.getSelectedItem().toString();
        String waiterDialog = waiter1.getSelectedItem().toString() +","+ waiter2.getSelectedItem().toString();
        String guardDialog = guard.getSelectedItem().toString();
        String id = dateDialog + shiftDialog;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Schedule/" +id);
        Schedule schedule = new Schedule(id, dateDialog, shiftDialog, baristaDialog, waiterDialog, guardDialog);
        myRef.setValue(schedule, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getContext(), "Push success", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getAllBarista(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Staff");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstBarista.clear();
                lstBarista.add(" ");
                for(DataSnapshot ds: snapshot.getChildren()){
                    if(TextUtils.equals(ds.child("position").getValue().toString(),"Barista")){
                        lstBarista.add(ds.child("displayName").getValue().toString());
                    }

                }
                spinArrayBarista.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getAllWaiter(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Staff");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstWaiter.clear();
                lstWaiter.add(" ");
                for(DataSnapshot ds: snapshot.getChildren()){
                    if(TextUtils.equals(ds.child("position").getValue().toString(),"Waiter")){
                        lstWaiter.add(ds.child("displayName").getValue().toString());
                    }

                }
                spinArrayWaiter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getAllGuard(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Staff");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstGuard.clear();
                lstGuard.add(" ");
                for(DataSnapshot ds: snapshot.getChildren()){
                    if(TextUtils.equals(ds.child("position").getValue().toString(),"Guard")){
                        lstGuard.add(ds.child("displayName").getValue().toString());
                    }

                }
                spinArrayGuard.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAllSchedule(String test){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Schedule");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Schedule schedule = snapshot.getValue(Schedule.class);
                if(schedule != null){
                    if(schedule.getDate().contains(test)){
                        lstSchedule.add(schedule);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Schedule schedule = snapshot.getValue(Schedule.class);
                if(schedule == null || lstSchedule == null || lstSchedule.isEmpty())
                    return;

                for(int i=0;i<lstSchedule.size();i++){
                    if(schedule.getScheduleID() == lstSchedule.get(i).getScheduleID()){
                        lstSchedule.set(i, schedule);
                        break;
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Schedule schedule =snapshot.getValue(Schedule.class);
                if(schedule == null || lstSchedule.isEmpty() || lstSchedule == null)
                    return;
                for(int i=0;i<lstSchedule.size();i++){
                    if(schedule.getScheduleID() == lstSchedule.get(i).getScheduleID()){
                        lstSchedule.remove(lstSchedule.get(i));
                        break;
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void anhxa(View view){
        btnDatePicker = view.findViewById(R.id.btnDatePicker);
        rcvSchedule = view.findViewById(R.id.rcvSchedule);
        btnAdd = view.findViewById(R.id.floatingbtnAddSchedule);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvSchedule.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        rcvSchedule.addItemDecoration(dividerItemDecoration);
    }
    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                String date = makeDateString(day,month,year);
                btnDatePicker.setText(date);
                lstSchedule.clear();
                getAllSchedule(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        datePicker = new DatePickerDialog(getContext(),dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return day +"-"+ month +"-"+ year;
    }


}