package com.example.myapplication.ui.home.OrderSite;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.myapplication.Adapter.TableAdapter;
import com.example.myapplication.Model.Table;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentTableOrderBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TableOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TableOrderFragment extends Fragment {

    private FragmentTableOrderBinding binding;
    private EditText txtTableID;
    private Spinner spinTableStatus;
    private Button btnPush, btnCancel;
    private FloatingActionButton btnAdd;
    private TableAdapter adapter;
    private List<Table> lstTable;
    private RecyclerView rcvTable;
    public TableOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTableOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        lstTable = new ArrayList<>();
        return inflater.inflate(R.layout.fragment_table_order, container, false);
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
}