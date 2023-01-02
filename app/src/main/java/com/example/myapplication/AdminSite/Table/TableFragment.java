package com.example.myapplication.AdminSite.Table;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.StaffAdapter;
import com.example.myapplication.Adapter.TableAdapter;
import com.example.myapplication.Model.Category;
import com.example.myapplication.Model.Table;
import com.example.myapplication.R;
import com.example.myapplication.SwipeCallBack.SwipeItemCategory;
import com.example.myapplication.SwipeCallBack.SwipeItemTable;
import com.example.myapplication.databinding.FragmentTableBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TableFragment extends Fragment {

    private FragmentTableBinding binding;
    private EditText txtTableID;
    private Spinner spinTableStatus;
    private Button btnPush, btnCancel;
    private FloatingActionButton btnAdd;
    private TableAdapter adapter;
    private List<Table> lstTable;
    private RecyclerView rcvTable;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTableBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        anhxa(root.getRootView());
        lstTable = new ArrayList<>();

        adapter = new TableAdapter(lstTable);

        rcvTable.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View viewDialogStaff = LayoutInflater.from(getContext()).inflate(R.layout.dialog_table, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(viewDialogStaff);
                AlertDialog alert = builder.create();
                alert.show();
                ArrayAdapter<String> spinArray;
                txtTableID = viewDialogStaff.findViewById(R.id.txtTableID);
                spinTableStatus = viewDialogStaff.findViewById(R.id.txtTableStatus);
                btnPush = viewDialogStaff.findViewById(R.id.btnPushTable);
                btnCancel = viewDialogStaff.findViewById(R.id.btnCancelTable);
                List<String> listSpin = new ArrayList<String>();
                listSpin.add("AVAILABLE");
                listSpin.add("UNAVAILABLE");
                spinArray = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_item, listSpin);
                spinArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinTableStatus.setAdapter(spinArray);
                btnPush.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(txtTableID.getText().toString().isEmpty()){
                            Toast.makeText(getContext(), "Vui lòng nhập thông tin bàn", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            onClickAddTable();
                            alert.dismiss();
                        }
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeItemTable(adapter));
        itemTouchHelper.attachToRecyclerView(rcvTable);
        getAllTable();

        return root;
    }

    private void onClickAddTable() {

            String id = txtTableID.getText().toString();
            String status = spinTableStatus.getSelectedItem().toString();
            if(TextUtils.isEmpty(id)){
                Toast.makeText(getContext(),"Vui long dung de trong thong tin",Toast.LENGTH_LONG).show();
                return;
            }

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Table/" +id);
            Table table = new Table(id,status);
            myRef.setValue(table, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    Toast.makeText(getContext(), "Push success", Toast.LENGTH_LONG).show();
                }
            });



    }

    private void getAllTable(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Table");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Table table = snapshot.getValue(Table.class);
                if(table != null){
                    lstTable.add(table);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Table table = snapshot.getValue(Table.class);
                if(table == null || lstTable == null || lstTable.isEmpty())
                    return;

                for(int i=0;i<lstTable.size();i++){
                    if(table.getTableID() == lstTable.get(i).getTableID()){
                        lstTable.set(i, table);
                        break;
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                Table table =snapshot.getValue(Table.class);
                if(table == null || lstTable.isEmpty() || lstTable == null)
                    return;
                for(int i=0;i<lstTable.size();i++){
                    if(table.getTableID() == lstTable.get(i).getTableID()){
                        lstTable.remove(lstTable.get(i));
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


    private void anhxa(View view){
        rcvTable = view.findViewById(R.id.rcvTable);
        btnAdd = view.findViewById(R.id.floatingbtnAddTable);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),4);
        rcvTable.setLayoutManager(gridLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        rcvTable.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}