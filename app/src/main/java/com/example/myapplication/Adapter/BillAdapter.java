package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Bill;
import com.example.myapplication.R;

import org.w3c.dom.Text;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {
    private List<Bill> lstBill;

    public BillAdapter(List<Bill> lstBill) {
        this.lstBill = lstBill;
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
}
