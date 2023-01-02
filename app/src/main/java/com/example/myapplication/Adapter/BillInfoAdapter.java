package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Bill;
import com.example.myapplication.Model.Cart;
import com.example.myapplication.R;

import java.util.List;

public class BillInfoAdapter extends RecyclerView.Adapter<BillInfoAdapter.ViewHolder> {
    List<Cart> lstBillCart;

    public BillInfoAdapter(List<Cart> lstBillCart) {
        this.lstBillCart = lstBillCart;
    }

    @NonNull
    @Override
    public BillInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_billinfo,parent, false);
        return new BillInfoAdapter.ViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull BillInfoAdapter.ViewHolder holder, int position) {
        Cart billInfo = lstBillCart.get(position);
        holder.txtName.setText(billInfo.getName());
        holder.txtQuantity.setText(String.valueOf(billInfo.getQuantity()));
        holder.txtProductPrice.setText(String.valueOf(billInfo.getPrice()));
        holder.txtTotalPrice.setText(String.valueOf(billInfo.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return lstBillCart.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtProductPrice, txtQuantity, txtTotalPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtDialogBillInfoName);
            txtProductPrice = itemView.findViewById(R.id.txtDialogBillInfoPrice);
            txtQuantity = itemView.findViewById(R.id.txtDialogBillInfoQuantity);
            txtTotalPrice = itemView.findViewById(R.id.txtDialogBillInfoTotalPrice);
        }
    }
}
