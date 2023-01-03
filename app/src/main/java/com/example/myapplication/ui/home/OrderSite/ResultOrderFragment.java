package com.example.myapplication.ui.home.OrderSite;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.myapplication.Adapter.CartAdapter;
import com.example.myapplication.Listener.ICartLoadListener;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Model.Bill;
import com.example.myapplication.Model.Cart;
import com.example.myapplication.R;
import com.example.myapplication.SwipeCallBack.SwipeItemCart;
import com.example.myapplication.databinding.FragmentResultOrderBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ResultOrderFragment extends Fragment implements ICartLoadListener {

    FragmentResultOrderBinding binding;
    RecyclerView rcvBills;
    Button btnResult, btnSum;
    DatePicker datePickerDialog;
    CartAdapter adapter;
    List<Cart> lstCart;
    ICartLoadListener iCartLoadListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResultOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        AnhXa(root);
        lstCart = new ArrayList<>();
        adapter = new CartAdapter(lstCart);
        rcvBills.setAdapter(adapter);
        loadCartFromFirebase();

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAddBill();
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeItemCart(adapter));
        itemTouchHelper.attachToRecyclerView(rcvBills);
        return root;
    }

    public void onClickAddBill(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Bill");
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
        String currentDate = dateFormat.format(Calendar.getInstance().getTime());
        @SuppressLint("SimpleDateFormat") DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String currentTime = timeFormat.format(Calendar.getInstance().getTime());
        float totalPrice = Float.parseFloat(btnSum.getText().toString());
        String id = myRef.push().getKey();
        List<Cart> lstCartToAdd = lstCart;
        Bill bill = new Bill(id, totalPrice, currentDate,currentTime,lstCart);
        new AlertDialog.Builder(getContext())
                .setTitle("Thanh toán")
                .setMessage("Bạn có muốn thanh toán không ?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myRef.child(id).setValue(bill, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                getActivity().onBackPressed();
                                Snackbar.make(btnSum.getRootView(),"Thanh toán thành công",Snackbar.LENGTH_LONG).show();
                                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Cart");
                                myRef.child(MainActivity.ID).removeValue();

                            }
                        });

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

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
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Cart").child(MainActivity.ID);
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Cart cart = snapshot.getValue(Cart.class);
                if(cart != null){
                    lstCart.add(cart);
                    iCartLoadListener.onCartLoadSuccess(lstCart);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Cart cart = snapshot.getValue(Cart.class);
                if(cart == null || lstCart == null || lstCart.isEmpty())
                    return;
                for(int i=0;i<lstCart.size();i++){
                    if(cart.getId() == lstCart.get(i).getId()){
                        lstCart.set(i, cart);
                        iCartLoadListener.onCartLoadSuccess(lstCart);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Cart cart = snapshot.getValue(Cart.class);
                if(cart == null || lstCart.isEmpty())
                    return;
                for(int i=0;i<lstCart.size();i++){
                    if(cart.getId() == lstCart.get(i).getId()){
                        lstCart.remove(lstCart.get(i));
                        iCartLoadListener.onCartLoadSuccess(lstCart);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCartLoadSuccess(List<Cart> cartModelList) {
        float sum=0;
        for(Cart cart: cartModelList){
            sum += cart.getTotalPrice();
        }
        btnSum.setText(String.valueOf(sum));
    }



    @Override
    public void onCartLoadFail(String message) {
        Snackbar.make(rcvBills.getRootView(), message,Snackbar.LENGTH_LONG).show();
    }
}