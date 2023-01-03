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

import com.example.myapplication.Listener.IRecycleViewClickListener;
import com.example.myapplication.Model.Product;
import com.example.myapplication.Model.Staff;
import com.example.myapplication.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> lstProduct;
    onClickHelper listener;
    public interface onClickHelper{
        public void adjustProduct(Product product);
    }
    public ProductAdapter(List<Product> lstProduct,onClickHelper listener) {
        this.lstProduct = lstProduct;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drinks,parent, false);
        return new ProductAdapter.ViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.adjustProduct(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstProduct.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtCategoryName, txtPrice;
        ImageView imgProduct;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategoryName = itemView.findViewById(R.id.txtItemProductCategory);
            txtName = itemView.findViewById(R.id.txtItemProductName);
            txtPrice = itemView.findViewById(R.id.txtItemPrice);
            imgProduct = itemView.findViewById(R.id.imgItemProduct);
            progressBar = itemView.findViewById(R.id.progressBarProduct);

        }


    }

    public void deleteProductAsPosition(int pos){
        Product product = lstProduct.get(pos);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Product");
        myRef.child(String.valueOf(product.getProductID())).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
            }
        });
        notifyItemRemoved(pos);
    }
}
