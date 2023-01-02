package com.example.myapplication.ui.home.OrderSite;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapter.BillAdapter;
import com.example.myapplication.Adapter.CartAdapter;
import com.example.myapplication.Adapter.ItemAdapter;
import com.example.myapplication.Listener.ICartLoadListener;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Model.Bill;
import com.example.myapplication.Model.Cart;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentResultOrderBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResultOrderFragment extends Fragment implements ICartLoadListener {

    FragmentResultOrderBinding binding;
    RecyclerView rcvBills;
    Button btnResult, btnSum;
    ICartLoadListener iCartLoadListener;
    float sum =0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResultOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        AnhXa(root);
        loadCartFromFirebase();
        return root;
    }

    public void AnhXa(View view){
        iCartLoadListener = this;
        btnSum = view.findViewById(R.id.btnSUM);
        btnResult = view.findViewById(R.id.btnResult);
        rcvBills = view.findViewById(R.id.rcvBillsOrder);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvBills.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        rcvBills.addItemDecoration(dividerItemDecoration);

    }

    private void loadCartFromFirebase(){
        List<Cart> lstCart = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Cart")
                .child(MainActivity.name)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot snapshot1: snapshot.getChildren()){
                                Cart cart = snapshot1.getValue(Cart.class);
                                lstCart.add(cart);
                            }
                            iCartLoadListener.onCartLoadSuccess(lstCart);
                        }else{
                            iCartLoadListener.onCartLoadFail("Empty");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iCartLoadListener.onCartLoadFail(error.getMessage());
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCartLoadSuccess(List<Cart> cartModelList) {
        for(Cart cart: cartModelList){
            sum += cart.getTotalPrice();
        }
        btnSum.setText(sum + "vnđ");
        CartAdapter adapter = new CartAdapter(cartModelList);
        rcvBills.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCartLoadFail(String message) {
        Snackbar.make(rcvBills.getRootView(), message,Snackbar.LENGTH_LONG).show();
    }
}