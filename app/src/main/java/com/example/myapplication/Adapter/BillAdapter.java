package com.example.myapplication.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Bill;
import com.example.myapplication.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder>{
    private ArrayList<Bill> lstBill;
    BillAdapter.onClickHelper listener;
    public interface onClickHelper{
        public void adjustBill(Bill bill);
    }
    public BillAdapter(ArrayList<Bill> lstBill, BillAdapter.onClickHelper listener) {
        this.lstBill = lstBill;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BillAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill,parent, false);
        return new BillAdapter.ViewHolder(viewHolder);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BillAdapter.ViewHolder holder, int position) {
        Bill bill = lstBill.get(position);
        holder.txtDrinkName.setText("Name: " + bill.getDrinkName());
        holder.txtPrice.setText("Price: " + String.valueOf(bill.getPrice()));
        holder.txtQuantity.setText("Quantity: " + String.valueOf(bill.getQuantity()));
        holder.txtTotalPrice.setText("Total: " + String.valueOf(bill.getTotalPrice()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.adjustBill(bill);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstBill.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDrinkName, txtPrice, txtQuantity, txtTotalPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDrinkName = itemView.findViewById(R.id.txtItemDrinkNameResultOrder);
            txtPrice = itemView.findViewById(R.id.txtItemPriceResultOrder);
            txtQuantity = itemView.findViewById(R.id.txtItemQuantityResultOrder);
            txtTotalPrice = itemView.findViewById(R.id.txtItemTotalPriceResultOrder);
        }
    }

    public void deleteProductAsPosition(int pos){
        Bill bill = lstBill.get(pos);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Bill");
        myRef.child(String.valueOf(bill.getID())).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
            }
        });
        notifyItemRemoved(pos);
        notifyItemRemoved(pos);
    }
}
