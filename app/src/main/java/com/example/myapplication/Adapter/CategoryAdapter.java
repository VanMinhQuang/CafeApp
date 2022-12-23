package com.example.myapplication.Adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Category;
import com.example.myapplication.Model.Staff;
import com.example.myapplication.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    List<Category> lstCategory;

    public CategoryAdapter(List<Category> lstCategory) {
        this.lstCategory = lstCategory;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent, false);
        return new CategoryAdapter.ViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
            Category category = lstCategory.get(position);
            holder.categoryID.setText(String.valueOf(category.getCategoryID()));
            holder.categoryName.setText(category.getCategoryName());
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Category");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"HEHE",Toast.LENGTH_LONG).show();
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View viewDialogStaff = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_category,null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(viewDialogStaff.getContext());
                    builder.setView(viewDialogStaff);
                    AlertDialog alert = builder.create();
                    alert.show();

                    EditText txtID = viewDialogStaff.findViewById(R.id.txtCategoryID);
                    EditText txtName = viewDialogStaff.findViewById(R.id.txtCategoryName);
                    Button btnPush = viewDialogStaff.findViewById(R.id.btnPushCategory);
                    Button btnCancel = viewDialogStaff.findViewById(R.id.btnCancelCategory);

                    txtID.setText(String.valueOf(category.getCategoryID()));
                    txtID.setEnabled(false);
                    txtName.setText(category.getCategoryName());

                    btnPush.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String newName = txtName.getText().toString().trim();

                            category.setCategoryName(newName);
                            myRef.child(String.valueOf(category.getCategoryID())).updateChildren(category.toMap(), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    Toast.makeText(builder.getContext(), "Update Category Success!!!",Toast.LENGTH_SHORT).show();
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

    @Override
    public int getItemCount() {
        return lstCategory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryID, categoryName;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            categoryID = itemView.findViewById(R.id.txtItemCategoryID);
            categoryName = itemView.findViewById(R.id.txtItemCategoryName);
        }
    }

    public void deleteCategoryAsPosition(int pos){
        Category category = lstCategory.get(pos);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Category");
        myRef.child(String.valueOf(category.getCategoryID())).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
            }
        });
        notifyItemRemoved(pos);
    }
}
