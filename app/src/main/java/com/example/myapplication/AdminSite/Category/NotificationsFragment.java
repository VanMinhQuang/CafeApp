package com.example.myapplication.AdminSite.Category;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.CategoryAdapter;
import com.example.myapplication.Adapter.StaffAdapter;
import com.example.myapplication.Model.Category;
import com.example.myapplication.Model.Staff;
import com.example.myapplication.R;
import com.example.myapplication.SwipeCallBack.SwipeItemCategory;
import com.example.myapplication.SwipeCallBack.SwipeItemStaff;
import com.example.myapplication.databinding.FragmentNotificationsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private EditText txtCategoryID, txtCategoryName;
    private Button btnPush, btnCancel;
    private FloatingActionButton btnAdd;
    private CategoryAdapter adapter;
    private RecyclerView rcvCategory;
    private List<Category> lstCategory;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        anhxa(root.getRootView());
        lstCategory = new ArrayList<>();
        adapter = new CategoryAdapter(lstCategory);
        rcvCategory.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View viewDialogStaff = LayoutInflater.from(getContext()).inflate(R.layout.dialog_category, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(viewDialogStaff);
                AlertDialog alert = builder.create();
                alert.show();

                txtCategoryID = viewDialogStaff.findViewById(R.id.txtCategoryID);
                txtCategoryName = viewDialogStaff.findViewById(R.id.txtCategoryName);
                btnPush = viewDialogStaff.findViewById(R.id.btnPushCategory);
                btnCancel = viewDialogStaff.findViewById(R.id.btnCancelCategory);

                btnPush.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        onClickAddCategory();
                        alert.dismiss();
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
        getAllCategory();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeItemCategory(adapter));
        itemTouchHelper.attachToRecyclerView(rcvCategory);
        return root;
    }

    private void onClickAddCategory() {
        int id = Integer.parseInt(txtCategoryID.getText().toString());
        String name = txtCategoryName.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Category/" +id);
        Category category = new Category(id,name);
        myRef.setValue(category, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getContext(), "Push success", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getAllCategory(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Category");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Category category = snapshot.getValue(Category.class);
                if(category != null){
                    lstCategory.add(category);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Category category = snapshot.getValue(Category.class);
                if(category == null || lstCategory == null || lstCategory.isEmpty())
                    return;

                for(int i=0;i<lstCategory.size();i++){
                    if(category.getCategoryID() == lstCategory.get(i).getCategoryID()){
                        lstCategory.set(i, category);
                        break;
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                Category category =snapshot.getValue(Category.class);
                if(category == null || lstCategory.isEmpty() || lstCategory == null)
                    return;
                for(int i=0;i<lstCategory.size();i++){
                    if(category.getCategoryID() == lstCategory.get(i).getCategoryID()){
                        lstCategory.remove(lstCategory.get(i));
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

    public void anhxa(View view){
        rcvCategory = view.findViewById(R.id.rcvCategory);
        btnAdd = view.findViewById(R.id.floatingbtnAddCategory);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvCategory.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        rcvCategory.addItemDecoration(dividerItemDecoration);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}