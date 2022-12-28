package com.example.myapplication.OrderSite;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import com.example.myapplication.Adapter.ProductAdapter;
import com.example.myapplication.Model.Product;
import com.example.myapplication.R;
import com.example.myapplication.SwipeCallBack.SwipeItemProduct;
import com.example.myapplication.databinding.FragmentDrinksBinding;
import com.example.myapplication.databinding.FragmentFoodOrderBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class FoodOrderFragment extends Fragment {
    private FragmentFoodOrderBinding binding;
    RecyclerView rcvProduct;
    private List<Product> lstProduct;
    ProductAdapter adapter;
    private ArrayList<String> listCategoryName = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFoodOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        AnhXa(root.getRootView());
        lstProduct = new ArrayList<>();
        adapter = new ProductAdapter(lstProduct, new ProductAdapter.onClickHelper() {
            @Override
            public void adjustProduct(Product product) {
                chooseQuantityInFragment(product);
            }
        });
        rcvProduct.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeItemProduct(adapter));
        itemTouchHelper.attachToRecyclerView(rcvProduct);
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
    public void chooseQuantityInFragment(Product product){
        View viewDialogStaff = LayoutInflater.from(getContext()).inflate(R.layout.dialog_drinks_order,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(viewDialogStaff.getContext());
        builder.setView(viewDialogStaff);
        AlertDialog alert = builder.create();
        alert.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Product");


        //Anh Xa
        TextView txtID = viewDialogStaff.findViewById(R.id.txtProductIDOrder);
        TextView txtName = viewDialogStaff.findViewById(R.id.txtProductNameOrder);
        TextView txtPrice = viewDialogStaff.findViewById(R.id.txtProductPriceOrder);
        EditText txtQuantity = viewDialogStaff.findViewById(R.id.txtProductQuantityOrder);
        TextView txtTotalPrice = viewDialogStaff.findViewById(R.id.txtProductTotalPriceOrder);
        ImageView imgProduct = viewDialogStaff.findViewById(R.id.product_order_img);
        Button btnPush = viewDialogStaff.findViewById(R.id.btnPushProductOrder);
        Button btnCancel = viewDialogStaff.findViewById(R.id.btnCancelProductOrder);
        txtID.setText("ID: " + product.getProductID());
        txtName.setText(product.getProductName());
        txtPrice.setText(String.valueOf(product.getPrice()));
        txtQuantity.setText("1");
        txtTotalPrice.setText(String.valueOf(product.getPrice() * Integer.parseInt(txtQuantity.getText().toString())));
        Picasso.get().load(product.getProductURI()).into(imgProduct);

        txtQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!txtQuantity.getText().toString().isEmpty()){
                    if(Integer.parseInt(txtQuantity.getText().toString()) != 0){
                        txtTotalPrice.setText(String.valueOf(product.getPrice() * Integer.parseInt(txtQuantity.getText().toString())));
                    }
                }
                else{
                    txtTotalPrice.setText("0");
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!txtQuantity.getText().toString().isEmpty()){
                    if(Integer.parseInt(txtQuantity.getText().toString()) != 0){
                        txtTotalPrice.setText(String.valueOf(product.getPrice() * Integer.parseInt(txtQuantity.getText().toString())));
                    }
                }
                else{
                    txtTotalPrice.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!txtQuantity.getText().toString().isEmpty()){
                    if(Integer.parseInt(txtQuantity.getText().toString()) != 0){
                        txtTotalPrice.setText(String.valueOf(product.getPrice() * Integer.parseInt(txtQuantity.getText().toString())));
                    }
                }
                else{
                    txtTotalPrice.setText("0");
                }
            }
        });

        btnPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Do something on Bill
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });
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