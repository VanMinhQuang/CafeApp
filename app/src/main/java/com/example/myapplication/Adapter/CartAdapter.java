package com.example.myapplication.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.Model.Cart;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    List<Cart> lstCart;

    public CartAdapter(List<Cart> lstCart){
        this.lstCart = lstCart;
    }
    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill,parent, false);
        return new CartAdapter.ViewHolder(viewHolder);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        Cart cart = lstCart.get(position);
        holder.name.setText(cart.getName());
        holder.price.setText(cart.getPrice() + "vnđ");
        holder.quantity.setText(String.valueOf(cart.getQuantity()));
        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusCart(holder,cart);
            }
        });
        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plusCart(holder,cart);
            }
        });

    }

    private void updateFirebase(Cart cart){
        FirebaseDatabase.getInstance().getReference("Cart").child(MainActivity.name).child(String.valueOf(cart.getId())).setValue(cart);
    }

    private void minusCart(CartAdapter.ViewHolder viewHolder, Cart cart){
        if(cart.getQuantity() > 1){
            cart.setQuantity(cart.getQuantity() - 1);
            cart.setTotalPrice(cart.getPrice() * cart.getQuantity());
            viewHolder.quantity.setText(String.valueOf(cart.getQuantity()));
            updateFirebase(cart);
        }else{
            Snackbar.make(viewHolder.itemView.getRootView(),"Can't do that",Snackbar.LENGTH_LONG).show();
        }
    }
    private void plusCart(CartAdapter.ViewHolder viewHolder, Cart cart){
            cart.setQuantity(cart.getQuantity() + 1);
            cart.setTotalPrice(cart.getPrice() * cart.getQuantity());
            viewHolder.quantity.setText(String.valueOf(cart.getQuantity()));
            updateFirebase(cart);
    }

    @Override
    public int getItemCount() {
        return lstCart.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, quantity, totalPrice;
        Button btnPlus, btnMinus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtItemDrinkNameResultOrder);
            price = itemView.findViewById(R.id.txtItemPriceResultOrder);
            quantity = itemView.findViewById(R.id.txtItemQuantityResultOrder);
            totalPrice = itemView.findViewById(R.id.txtItemTotalPriceResultOrder);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
        }
    }
}
