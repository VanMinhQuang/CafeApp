package com.example.myapplication.ui.gallery;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.ScheduleAdapter;
import com.example.myapplication.Model.Schedule;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentGalleryBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private ScheduleAdapter adapter;
    private DatePickerDialog datePickerDialog;
    private Button btnDatePicker;
    private RecyclerView rcvSchedule;
    private List<Schedule> lstSchedule;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initDatePicker();
        anhxa(root.getRootView());
        btnDatePicker.setText(getTodayDate());
        lstSchedule = new ArrayList<>();
        adapter = new ScheduleAdapter(lstSchedule, new ScheduleAdapter.onClickHelper() {
            @Override
            public void adjustProduct(Schedule schedule) {
            }
        });
        rcvSchedule.setAdapter(adapter);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        return root;
    }
    public void anhxa(View view){
        btnDatePicker = view.findViewById(R.id.btnDatePickerMain);
        rcvSchedule = view.findViewById(R.id.rcvScheduleMain);
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
        datePickerDialog = new DatePickerDialog(getContext(),dateSetListener, year, month, day);
    }
    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);

    }
    private String makeDateString(int day, int month, int year) {
        return day +"-"+ month +"-"+ year;
    }
    private void getAllSchedule(String date){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Schedule");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Schedule schedule = snapshot.getValue(Schedule.class);
                if(schedule != null){
                    if(schedule.getDate().contains(date)){
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}