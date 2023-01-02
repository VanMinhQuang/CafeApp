package com.example.myapplication.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Bill;
import com.example.myapplication.Model.Cart;
import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {
    private List<Bill> lstBill;
    private List<Cart> lstCart = new ArrayList<>();
    OnGetAllProduct listener;
    public interface OnGetAllProduct{
        void getAllCart();
    }

    public BillAdapter(List<Bill> lstBill, OnGetAllProduct listener) {
        this.lstBill = lstBill;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BillAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_statistic,parent, false);
        return new BillAdapter.ViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull BillAdapter.ViewHolder holder, int position) {
        Bill bill = lstBill.get(position);
        holder.txtID.setText(bill.getID());
        holder.txtDate.setText(bill.getDateTime());
        holder.txtPrice.setText(String.valueOf(bill.getTotalPrice()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View viewDialogStaff = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_billinfo,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(viewDialogStaff.getContext());
                builder.setView(viewDialogStaff);
                AlertDialog alert = builder.create();
                alert.show();
                RecyclerView rcvBillInfo = viewDialogStaff.findViewById(R.id.rcvBillInfo);
                BillInfoAdapter adapter = new BillInfoAdapter(lstCart);
                rcvBillInfo.setAdapter(adapter);
                rcvBillInfo.setLayoutManager(new LinearLayoutManager(builder.getContext()));
                getAll(bill,adapter);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstBill.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtID, txtDate, txtPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtID = itemView.findViewById(R.id.txtItemBillID);
            txtDate = itemView.findViewById(R.id.txtItemBillDate);
            txtPrice = itemView.findViewById(R.id.txtItemBillTotalPrice);
        }
    }
    public void getAll(Bill bill,BillInfoAdapter adapter){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Bill").child(bill.getID()).child("listCart");
        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstCart.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    lstCart.add(ds.getValue(Cart.class));

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
