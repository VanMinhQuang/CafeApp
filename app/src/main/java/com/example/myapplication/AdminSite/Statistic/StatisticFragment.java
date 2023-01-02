package com.example.myapplication.AdminSite.Statistic;

import android.annotation.SuppressLint;
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

import com.example.myapplication.Adapter.BillAdapter;
import com.example.myapplication.Model.Bill;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentStatisticBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class StatisticFragment extends Fragment {

    private FragmentStatisticBinding binding;
    private List<Bill> lstBill;
    private RecyclerView rcvStatistic;
    private Button btnSum;
    private BillAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStatisticBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        anhXa(root.getRootView());
        lstBill = new ArrayList<>();
        adapter = new BillAdapter(lstBill);
        rcvStatistic.setAdapter(adapter);
        getAllBill();
        return root;
    }

    public void getAllBill(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Bill");
        myRef.addChildEventListener(new ChildEventListener() {
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
    public void anhXa(View view){
        btnSum = view.findViewById(R.id.btnSumStatistic);
        rcvStatistic = view.findViewById(R.id.rcvStatistic);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvStatistic.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        rcvStatistic.addItemDecoration(dividerItemDecoration);

    }
}