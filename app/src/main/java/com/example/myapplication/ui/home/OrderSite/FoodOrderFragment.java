package com.example.myapplication.ui.home.OrderSite;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapter.ItemAdapter;
import com.example.myapplication.Adapter.ProductAdapter;
import com.example.myapplication.Listener.ICartLoadListener;
import com.example.myapplication.Model.Bill;
import com.example.myapplication.Model.Cart;
import com.example.myapplication.Model.Product;
import com.example.myapplication.OrderActivity;
import com.example.myapplication.R;
import com.example.myapplication.SwipeCallBack.SwipeItemProduct;
import com.example.myapplication.databinding.FragmentDrinksBinding;
import com.example.myapplication.databinding.FragmentFoodOrderBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;


public class FoodOrderFragment extends Fragment {
    private FragmentFoodOrderBinding binding;
    RecyclerView rcvProduct;
    private List<Product> lstProduct;
    ItemAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFoodOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        AnhXa(root.getRootView());
        lstProduct = new ArrayList<>();
        adapter = new ItemAdapter(lstProduct, new ICartLoadListener() {
            @Override
            public void onCartLoadSuccess(List<Cart> cartModelList) {
            }

            @Override
            public void onCartLoadFail(String message) {
                Snackbar.make(root.getRootView(),message,Snackbar.LENGTH_LONG).show();
            }
        });
        rcvProduct.setAdapter(adapter);
        getAllProduct();
        return root;
    }
    public void AnhXa(View view){
        rcvProduct = view.findViewById(R.id.rcvDrinksOrder);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        rcvProduct.setLayoutManager(gridLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        rcvProduct.addItemDecoration(dividerItemDecoration);

    }

    private void getAllProduct(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Product");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product product = snapshot.getValue(Product.class);
                if(product != null){
                    lstProduct.add(product);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product product = snapshot.getValue(Product.class);
                if(product == null || lstProduct == null || lstProduct.isEmpty())
                    return;
                for(int i=0;i<lstProduct.size();i++){
                    if(product.getProductID() == lstProduct.get(i).getProductID()){
                        lstProduct.set(i, product);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Product product =snapshot.getValue(Product.class);
                if(product == null || lstProduct.isEmpty() || lstProduct == null)
                    return;
                for(int i=0;i<lstProduct.size();i++){
                    if(product.getProductID() == lstProduct.get(i).getProductID()){
                        lstProduct.remove(lstProduct.get(i));
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