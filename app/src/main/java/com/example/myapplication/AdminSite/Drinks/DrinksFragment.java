package com.example.myapplication.AdminSite.Drinks;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapter.ProductAdapter;
import com.example.myapplication.Model.Category;
import com.example.myapplication.Model.Product;
import com.example.myapplication.Model.Staff;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentDrinksBinding;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.ui.home.HomeViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class DrinksFragment extends Fragment {

    private FragmentDrinksBinding binding;
    private List<Product> lstProduct;
    private ProductAdapter adapter;
    private RecyclerView rcvProduct;
    private FloatingActionButton btnAdd;
    private EditText txtID, txtName, txtPrice, txtQuantity;
    private Spinner spinnerProductCategory;
    private CircleImageView productImg;
    private Button btnSave, btnCancel;
    private ActivityResultLauncher<String> launcher;
    private String uriName = "";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDrinksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        anhxa(root.getRootView());
        lstProduct = new ArrayList<>();
        adapter = new ProductAdapter(lstProduct);
        rcvProduct.setAdapter(adapter);

        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                productImg.setImageURI(result);
                uploadImageToFirebase(result);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View viewDialogStaff = LayoutInflater.from(getContext()).inflate(R.layout.dialog_drinks, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(viewDialogStaff);
                AlertDialog alert = builder.create();
                alert.show();
                ArrayAdapter<String> spinArray;
                txtID = viewDialogStaff.findViewById(R.id.txtProductID);
                txtName = viewDialogStaff.findViewById(R.id.txtProductName);
                txtPrice = viewDialogStaff.findViewById(R.id.txtProductPrice);
                txtQuantity = viewDialogStaff.findViewById(R.id.txtProductQuantity);
                productImg = viewDialogStaff.findViewById(R.id.product_img);
                btnSave = viewDialogStaff.findViewById(R.id.btnPushProduct);
                spinnerProductCategory = viewDialogStaff.findViewById(R.id.spinProductCategory);
                btnCancel = viewDialogStaff.findViewById(R.id.btnCancelProduct);





                productImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        launcher.launch("image/*");
                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickAddProduct();
                        alert.dismiss();
                    }
                });
            }
        });
        getAllProduct();
        return root;
    }

    private void onClickAddProduct(){

        int id = Integer.parseInt(txtID.getText().toString());
        String name = txtName.getText().toString();
        String categoryName = spinnerProductCategory.getSelectedItem().toString();
        float price = Float.parseFloat(txtPrice.getText().toString());
        long quantity = Long.parseLong(txtQuantity.getText().toString());
        String imgURI = uriName;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Product/" +id);
        Product product = new Product(id,categoryName,price,name, quantity, imgURI);
        myRef.setValue(product, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getContext(), "Push success", Toast.LENGTH_LONG).show();
            }
        });

    }

    public String getFileExtension(Uri uri){
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private String uploadImageToFirebase(Uri uri){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference("ProductImage").child(System.currentTimeMillis() + "." +getFileExtension(uri));
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        uriName = uri.toString();
                        Toast.makeText(getContext(),"Upload Image Successful",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Upload Image Failed!",Toast.LENGTH_SHORT).show();
            }
        });
        return uriName;
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

    private void anhxa(View view){
        rcvProduct = view.findViewById(R.id.rcvDrinks);
        btnAdd = view.findViewById(R.id.floatingbtnAddDrinks);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        rcvProduct.setLayoutManager(gridLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        rcvProduct.addItemDecoration(dividerItemDecoration);


    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}