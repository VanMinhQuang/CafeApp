package com.example.myapplication.ui.Statistic;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.Adapter.BillAdapter;
import com.example.myapplication.Model.Bill;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentEmployeeStatisticBinding;
import com.example.myapplication.databinding.FragmentStatisticBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class EmployeeStatisticFragment extends Fragment {


    private FragmentEmployeeStatisticBinding binding;
    private List<Bill> lstBill;
    private RecyclerView rcvStatistic;
    private EditText txtStatisticDateStart, txtStatisticDateEnd;
    private Button btnSum, btnDateStart, btnDateEnd,btnShow, btnFirstShift, btnSecondShift,btnThirdShift;
    private BillAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEmployeeStatisticBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        anhXa(root.getRootView());
        lstBill = new ArrayList<>();
        adapter = new BillAdapter(lstBill, new BillAdapter.OnGetAllProduct() {
            @Override
            public void getAllCart() {
            }
        });
        rcvStatistic.setAdapter(adapter);
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                updateCalendar(txtStatisticDateStart,calendar);
            }


        };
        DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                updateCalendar(txtStatisticDateEnd,calendar);
            }


        };
        btnDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(),date, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btnDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(),date2, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btnFirstShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateStart = txtStatisticDateStart.getText().toString();
                String dateEnd = txtStatisticDateEnd.getText().toString();
                if(dateStart == "" || dateEnd == ""){
                    Snackbar.make(root,"Error getting Data",Snackbar.LENGTH_LONG).show();
                    return;
                }
                getAllBillShift1(dateStart,dateEnd);
            }
        });
        btnSecondShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateStart = txtStatisticDateStart.getText().toString();
                String dateEnd = txtStatisticDateEnd.getText().toString();
                if(dateStart == "" || dateEnd == ""){
                    Snackbar.make(root,"Error getting Data",Snackbar.LENGTH_LONG).show();
                    return;
                }
                getAllBillShift2(dateStart, dateEnd);
            }
        });
        btnThirdShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateStart = txtStatisticDateStart.getText().toString();
                String dateEnd = txtStatisticDateEnd.getText().toString();
                if(dateStart == "" || dateEnd == ""){
                    Snackbar.make(root,"Error getting Data",Snackbar.LENGTH_LONG).show();
                    return;
                }
                getAllBillShift3(dateStart,dateEnd);
            }
        });
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dateStart = txtStatisticDateStart.getText().toString();
                String dateEnd = txtStatisticDateEnd.getText().toString();
                if(dateStart == "" || dateEnd == ""){
                    Toast.makeText(getContext(),"Error getting Data",Toast.LENGTH_LONG).show();
                    return;
                }
                getAllBill(dateStart,dateEnd);
            }
        });
        return root;
    }

    private boolean checktimings(String time, String timeStart, String endtime) {

        String pattern = "HH:mm:ss";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date = sdf.parse(time);
            Date date1 = sdf.parse(timeStart);
            Date date2 = sdf.parse(endtime);

            if(date.after(date1) && date.before(date2)){
                return true;
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
        return false;
    }

    private void updateCalendar(EditText txtText, Calendar calendar) {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("dd MM yyyy");
        txtText.setText(df.format(calendar.getTime()));
    }


    public void getAllBillShift1(String dateStart, String dateEnd){
        lstBill.clear();
        Query query = FirebaseDatabase.getInstance().getReference("Bill").orderByChild("date").startAt(dateStart).endAt(dateEnd);
        query.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Bill bill = snapshot.getValue(Bill.class);
                if(bill != null){
                    if(checktimings(bill.getTime(),"7:00:00","11:29:00" )){
                        lstBill.add(bill);
                    }
                    adapter.notifyDataSetChanged();
                    getSum(lstBill);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Bill bill =snapshot.getValue(Bill.class);
                if(bill == null || lstBill.isEmpty() || lstBill == null)
                    return;
                for(int i=0;i<lstBill.size();i++){
                    if(bill.getID() == lstBill.get(i).getID()){
                        lstBill.remove(lstBill.get(i));
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
    public void getAllBillShift2(String dateStart, String dateEnd){
        lstBill.clear();
        Query query = FirebaseDatabase.getInstance().getReference("Bill").orderByChild("date").startAt(dateStart).endAt(dateEnd);
        query.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Bill bill = snapshot.getValue(Bill.class);
                if(bill != null){
                    if(checktimings(bill.getTime(),"11:30:00","16:59:00" )){
                        lstBill.add(bill);
                    }
                    adapter.notifyDataSetChanged();
                    getSum(lstBill);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Bill bill =snapshot.getValue(Bill.class);
                if(bill == null || lstBill.isEmpty() || lstBill == null)
                    return;
                for(int i=0;i<lstBill.size();i++){
                    if(bill.getID() == lstBill.get(i).getID()){
                        lstBill.remove(lstBill.get(i));
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
    public void getAllBillShift3(String dateStart, String dateEnd){
        lstBill.clear();
        Query query = FirebaseDatabase.getInstance().getReference("Bill").orderByChild("date").startAt(dateStart).endAt(dateEnd);
        query.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Bill bill = snapshot.getValue(Bill.class);
                if(bill != null){
                    if(checktimings(bill.getTime(), "17:00:00","22:00:00" )){
                        lstBill.add(bill);
                    }
                    adapter.notifyDataSetChanged();
                    getSum(lstBill);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Bill bill =snapshot.getValue(Bill.class);
                if(bill == null || lstBill.isEmpty() || lstBill == null)
                    return;
                for(int i=0;i<lstBill.size();i++){
                    if(bill.getID() == lstBill.get(i).getID()){
                        lstBill.remove(lstBill.get(i));
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


    public void getAllBill(String dateStart, String dateEnd){
        lstBill.clear();
        Query query = FirebaseDatabase.getInstance().getReference("Bill").orderByChild("date").startAt(dateStart).endAt(dateEnd);
        query.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Bill bill = snapshot.getValue(Bill.class);
                if(bill != null){
                    lstBill.add(bill);
                    adapter.notifyDataSetChanged();
                    getSum(lstBill);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Bill bill =snapshot.getValue(Bill.class);
                if(bill == null || lstBill.isEmpty() || lstBill == null)
                    return;
                for(int i=0;i<lstBill.size();i++){
                    if(bill.getID() == lstBill.get(i).getID()){
                        lstBill.remove(lstBill.get(i));
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

    @SuppressLint("SetTextI18n")
    public void getSum(List<Bill> lstBill){
        float sum = 0;
        for(Bill bill: lstBill){
            sum += bill.getTotalPrice();
        }
        btnSum.setText("Tổng: "+sum);

    }
    @SuppressLint("CutPasteId")
    public void anhXa(View view){
        btnFirstShift = view.findViewById(R.id.btnFirstShift);
        btnSecondShift = view.findViewById(R.id.btnSecondShift);
        btnThirdShift = view.findViewById(R.id.btnThirdShift);
        txtStatisticDateStart = view.findViewById(R.id.txtStatisticDateStart);
        txtStatisticDateEnd = view.findViewById(R.id.txtStatisticDateEnd);
        btnDateEnd = view.findViewById(R.id.btnStatisticDateEnd);
        btnDateStart = view.findViewById(R.id.btnStatisticDateStart);
        btnShow = view.findViewById(R.id.btnShow);
        btnSum = view.findViewById(R.id.btnSumStatistic);
        rcvStatistic = view.findViewById(R.id.rcvStatistic);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvStatistic.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        rcvStatistic.addItemDecoration(dividerItemDecoration);

    }
}