package com.example.myapplication.OrderSite;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

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
import com.example.myapplication.Model.Bill;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentResultOrderBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class ResultOrderFragment extends Fragment {

    FragmentResultOrderBinding binding;
    public static ArrayList<Bill> lstBill = new ArrayList<>();
    RecyclerView rcvBills;
    BillAdapter adapter;
    private IUpdateDataListener mUpdate;
    public interface IUpdateDataListener{
        void updateData(ArrayList<Bill> bills);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResultOrderBinding.inflate(inflater, container, false);
        if(FoodOrderFragment.lstBill != null){
            lstBill = FoodOrderFragment.lstBill;
        }
        View root = binding.getRoot();
        AnhXa(root);
        adapter = new BillAdapter(lstBill, new BillAdapter.onClickHelper() {
            @Override
            public void adjustBill(Bill bill) {
                chooseQuantityInFragment(bill);
            }
        });
        rcvBills.setAdapter(adapter);
        return root;
    }

    public void receiveDataFromFoodOrder(ArrayList<Bill> bills){
        lstBill = bills;
    }
    public void AnhXa(View view){

        rcvBills = view.findViewById(R.id.rcvBillsOrder);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        rcvBills.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        rcvBills.addItemDecoration(dividerItemDecoration);

    }
    @SuppressLint("SetTextI18n")
    public void chooseQuantityInFragment(Bill bill){
        View viewDialogStaff = LayoutInflater.from(getContext()).inflate(R.layout.dialog_drinks_order,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(viewDialogStaff.getContext());
        builder.setView(viewDialogStaff);
        AlertDialog alert = builder.create();
        alert.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Product");

        TextView txtID = viewDialogStaff.findViewById(R.id.txtProductIDOrder);
        TextView txtName = viewDialogStaff.findViewById(R.id.txtProductNameOrder);
        TextView txtPrice = viewDialogStaff.findViewById(R.id.txtProductPriceOrder);
        EditText txtQuantity = viewDialogStaff.findViewById(R.id.txtProductQuantityOrder);
        TextView txtTotalPrice = viewDialogStaff.findViewById(R.id.txtProductTotalPriceOrder);
        ImageView imgProduct = viewDialogStaff.findViewById(R.id.product_order_img);
        Button btnPush = viewDialogStaff.findViewById(R.id.btnPushProductOrder);
        Button btnCancel = viewDialogStaff.findViewById(R.id.btnCancelProductOrder);

        txtID.setText("ID: " + bill.getID());
        txtName.setText(bill.getDrinkName());
        txtPrice.setText(String.valueOf(bill.getPrice()));
        txtQuantity.setText(bill.getQuantity());
        txtTotalPrice.setText(String.valueOf(bill.getTotalPrice()));
    }
}