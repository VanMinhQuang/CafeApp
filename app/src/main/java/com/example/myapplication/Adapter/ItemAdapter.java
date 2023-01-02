package com.example.myapplication.Adapter;

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

import com.example.myapplication.Listener.ICartLoadListener;
import com.example.myapplication.Listener.IRecycleViewClickListener;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Model.Bill;
import com.example.myapplication.Model.Cart;
import com.example.myapplication.Model.Product;
import com.example.myapplication.Model.Staff;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<Product> lstProduct;
    private ICartLoadListener iCartLoadListener;

    public ItemAdapter(List<Product> lstProduct,ICartLoadListener iCartLoadListener) {
        this.lstProduct = lstProduct;
        this.iCartLoadListener = iCartLoadListener;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drinks,parent, false);
        return new ItemAdapter.ViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {
        Product product = lstProduct.get(position);
        holder.txtName.setText(product.getProductName());
        holder.txtCategoryName.setText(product.getCategoryProduct());
        holder.txtPrice.setText(String.valueOf(product.getPrice()));
        Picasso.get().load(product.getProductURI()).into(holder.imgProduct, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        if(product.getQuantity() < 10){
            holder.txtName.setBackgroundColor(Color.RED);
            holder.txtCategoryName.setBackgroundColor(Color.RED);
        }else{
            holder.txtName.setBackgroundColor(Color.BLACK);
            holder.txtCategoryName.setBackgroundColor(Color.BLACK);
        }
        holder.setListener(new IRecycleViewClickListener() {
            @Override
            public void onRecycleClick(View view, int position) {
                addtoCart(product);
            }
        });


    }

    private void addtoCart(Product product) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Cart").child(MainActivity.name);
        myRef.child(String.valueOf(product.getProductID())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Cart cart = snapshot.getValue(Cart.class);
                    assert cart != null;
                    cart.setQuantity(cart.getQuantity()+1);
                    Map<String,Object> updateData = new HashMap<>();
                    updateData.put("quantity",cart.getQuantity());
                    updateData.put("totalPrice",cart.getPrice() * cart.getQuantity());
                    myRef.child(String.valueOf(product.getProductID())).updateChildren(updateData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            iCartLoadListener.onCartLoadFail("Add Success");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            iCartLoadListener.onCartLoadFail(e.getMessage());
                        }
                    });

                }else{
                    Cart cart = new Cart(product.getProductID(),product.getProductName(),product.getPrice(),product.getPrice(),1);
                    myRef.child(String.valueOf(cart.getId())).setValue(cart).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            iCartLoadListener.onCartLoadFail("Add Success");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            iCartLoadListener.onCartLoadFail(e.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return lstProduct.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtName, txtCategoryName, txtPrice;
        ImageView imgProduct;
        ProgressBar progressBar;
        IRecycleViewClickListener listener;

        public void setListener(IRecycleViewClickListener listener) {
            this.listener = listener;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategoryName = itemView.findViewById(R.id.txtItemProductCategory);
            txtName = itemView.findViewById(R.id.txtItemProductName);
            imgProduct = itemView.findViewById(R.id.imgItemProduct);
            txtPrice = itemView.findViewById(R.id.txtItemPrice);
            progressBar = itemView.findViewById(R.id.progressBarProduct);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            listener.onRecycleClick(v,getAdapterPosition());
        }
    }


}
