package com.example.myapplication.Adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Staff;
import com.example.myapplication.Model.Table;
import com.example.myapplication.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {
    List<Table> lstTable;

    public TableAdapter(List<Table> lstTable) {
        this.lstTable = lstTable;
    }

    @NonNull
    @Override
    public TableAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table,parent, false);
        return new TableAdapter.ViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull TableAdapter.ViewHolder holder, int position) {
        Table t = lstTable.get(position);
        holder.tableID.setText(t.getTableID());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Table");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View viewDialogStaff = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_table,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(viewDialogStaff.getContext());
                builder.setView(viewDialogStaff);
                AlertDialog alert = builder.create();
                alert.show();
                ArrayAdapter<String> spinArray;
                EditText txtID = viewDialogStaff.findViewById(R.id.txtTableID);
                Spinner spinStatus = viewDialogStaff.findViewById(R.id.txtTableStatus);
                Button btnPush = viewDialogStaff.findViewById(R.id.btnPushTable);
                Button btnCancel = viewDialogStaff.findViewById(R.id.btnCancelTable);

                List<String> listSpin = new ArrayList<String>();
                listSpin.add("AVAILABLE");
                listSpin.add("UNAVAILABLE");
                spinArray = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_item, listSpin);
                spinArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinStatus.setAdapter(spinArray);

                txtID.setText(t.getTableID());
                int getPos = spinArray.getPosition(t.getStatus());
                spinStatus.setSelection(getPos);

                btnPush.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newID = txtID.getText().toString().trim();
                        String newStatus = spinStatus.getSelectedItem().toString();

                        if(newID.equals("")){
                            Toast.makeText(builder.getContext(), "Vui long dung de trong thong tin",Toast.LENGTH_LONG).show();
                            return;
                        }

                        t.setTableID(newID);
                        t.setStatus(newStatus);

                        myRef.child(t.getTableID()).updateChildren(t.toMap(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(builder.getContext(), "Update Table Success  !!!",Toast.LENGTH_SHORT).show();
                                alert.dismiss();
                            }
                        });
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

    }

    public void deleteTableAsPosition(int pos){
        Table table = lstTable.get(pos);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Table");
        myRef.child(String.valueOf(table.getTableID())).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
            }
        });
        notifyItemRemoved(pos);
    }

    @Override
    public int getItemCount() {
        return lstTable.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tableID;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tableID = itemView.findViewById(R.id.txtItemTable);
        }
    }
}
