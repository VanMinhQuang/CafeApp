package com.example.myapplication.AdminSite.Staff;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Adapter.StaffAdapter;
import com.example.myapplication.Model.Staff;
import com.example.myapplication.R;
import com.example.myapplication.SwipeCallBack.SwipeItemStaff;
import com.example.myapplication.databinding.FragmentStaffBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class StaffFragment extends Fragment {

    private FragmentStaffBinding binding;
    private EditText txtID, txtUser, txtPass, txtDisplay, txtPhone, txtAddress;
    private CircleImageView profilePic, imgStaff;
    private FloatingActionButton btnSave;
    private Button btnPush, btnCancel;
    private Spinner spinPosition;
    private RecyclerView rcvStaff;
    private StaffAdapter adapter;
    private List<Staff> lstStaff;


    private String uriName; //Một biến để lưu đường dẫn
    private String uriName2;
    ActivityResultLauncher<String> launch;
    ActivityResultLauncher<String> launch2;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentStaffBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        anhxa(root.getRootView());


        //Đây là cái launcher để thực hiện mở Gallery
        launch = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                profilePic.setImageURI(result);
                uploadImageToFirebase(result);

            }
        });

        launch2 = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                imgStaff.setImageURI(result);
                uploadImageToFirebase2(result);
            }
        });



        lstStaff = new ArrayList<>();
        adapter = new StaffAdapter(lstStaff, new StaffAdapter.mIClickListener() { // Thực hiện chỉnh sửa
            @Override
            public void onClickListener(Staff s) {
                chinhsua(s);
            }

        });
        rcvStaff.setAdapter(adapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View viewDialogStaff = LayoutInflater.from(getContext()).inflate(R.layout.dialog_staff, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(viewDialogStaff);
                AlertDialog alert = builder.create();
                alert.show();
                ArrayAdapter<String> spinArray;

                txtID = viewDialogStaff.findViewById(R.id.txtStaffID);
                txtUser = viewDialogStaff.findViewById(R.id.txtStaffUserName);
                txtPass = viewDialogStaff.findViewById(R.id.txtStaffPassword);
                txtDisplay = viewDialogStaff.findViewById(R.id.txtStaffDisplayName);
                spinPosition = viewDialogStaff.findViewById(R.id.spinStaffPosition);
                txtPhone = viewDialogStaff.findViewById(R.id.txtStaffPhoneNumber);
                txtAddress = viewDialogStaff.findViewById(R.id.txtStaffAddress);
                btnPush = viewDialogStaff.findViewById(R.id.btnPush);
                btnCancel = viewDialogStaff.findViewById(R.id.btnCancel);
                profilePic = viewDialogStaff.findViewById(R.id.profile_img);

                 List<String> listSpin = new ArrayList<String>();
                listSpin.add("Mananger");
                listSpin.add("Bartender");
                listSpin.add("Waiter");
                listSpin.add("Guard");
                spinArray = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, listSpin);
                spinArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinPosition.setAdapter(spinArray);

                profilePic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        launch.launch("image/*");
                    }
                });

                btnPush.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try{
                            onClickAddStaff();
                            alert.dismiss();
                        }catch (Exception exception){
                            Toast.makeText(getContext(),"Co loi xay ra vui long nhap lai",Toast.LENGTH_SHORT).show();
                            return;
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

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeItemStaff(adapter));
        itemTouchHelper.attachToRecyclerView(rcvStaff);
        getAllStaff();
        return root;


    }


    public String uploadImageToFirebase(Uri uri){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference("StaffImage").child(System.currentTimeMillis() + "." +getFileExtension(uri));
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        uriName = uri.toString();
                        uriName2 = uri.toString();
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

    public String uploadImageToFirebase2(Uri uri){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference("StaffImage").child(System.currentTimeMillis() + "." +getFileExtension(uri));
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        uriName2 = uri.toString();
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
        return uriName2;
    }

    public String getFileExtension(Uri uri){
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    private void onClickAddStaff(){
        int id = Integer.parseInt(txtID.getText().toString());
        String username = txtUser.getText().toString();
        String password =txtPass.getText().toString();
        String phone = txtPhone.getText().toString();
        String address = txtAddress.getText().toString();
        String position = spinPosition.getSelectedItem().toString();
        String displayname = txtDisplay.getText().toString();
        String imasd = uriName;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Staff/" +id);
        Staff staff = new Staff(id,username,password,address,phone, displayname, position,imasd);
        myRef.setValue(staff, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getContext(), "Push success", Toast.LENGTH_LONG).show();
            }
        });


    }

    private void getAllStaff(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Staff");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Staff staff = snapshot.getValue(Staff.class);
                if(staff != null){
                    lstStaff.add(staff);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Staff staff = snapshot.getValue(Staff.class);
                if(staff == null || lstStaff == null || lstStaff.isEmpty())
                    return;

                for(int i=0;i<lstStaff.size();i++){
                    if(staff.getId() == lstStaff.get(i).getId()){
                        lstStaff.set(i, staff);
                        break;
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Staff staff =snapshot.getValue(Staff.class);
                if(staff == null || lstStaff.isEmpty() || lstStaff == null)
                    return;
                for(int i=0;i<lstStaff.size();i++){
                    if(staff.getId() == lstStaff.get(i).getId()){
                        lstStaff.remove(lstStaff.get(i));
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
        rcvStaff = view.findViewById(R.id.rcvStaff);
        btnSave = view.findViewById(R.id.floatingbtnAddStaff);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvStaff.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        rcvStaff.addItemDecoration(dividerItemDecoration);


    }


    public void chinhsua(Staff s){


        try{
            View viewDialogStaff = LayoutInflater.from(getContext()).inflate(R.layout.dialog_staff,null);
            AlertDialog.Builder builder = new AlertDialog.Builder(viewDialogStaff.getContext());
            builder.setView(viewDialogStaff);
            AlertDialog alert = builder.create();
            alert.show();
            ArrayAdapter<String> spinArray;
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Staff");

            imgStaff = viewDialogStaff.findViewById(R.id.profile_img);
            EditText txtID = viewDialogStaff.findViewById(R.id.txtStaffID);
            EditText txtUser = viewDialogStaff.findViewById(R.id.txtStaffUserName);
            EditText txtPass = viewDialogStaff.findViewById(R.id.txtStaffPassword);
            EditText txtDisplay = viewDialogStaff.findViewById(R.id.txtStaffDisplayName);
            Spinner spinPosition = viewDialogStaff.findViewById(R.id.spinStaffPosition);
            EditText txtPhone = viewDialogStaff.findViewById(R.id.txtStaffPhoneNumber);
            EditText txtAddress = viewDialogStaff.findViewById(R.id.txtStaffAddress);
            Button btnPush = viewDialogStaff.findViewById(R.id.btnPush);
            Button btnCancel = viewDialogStaff.findViewById(R.id.btnCancel);
            List<String> listSpin = new ArrayList<String>();
            listSpin.add("Mananger");
            listSpin.add("Bartender");
            listSpin.add("Waiter");
            listSpin.add("Guard");
            spinArray = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, listSpin);
            spinArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinPosition.setAdapter(spinArray);

            txtID.setText(String.valueOf(s.getId()));
            txtUser.setText(s.getUsername());
            txtPass.setText(s.getPassword());
            txtDisplay.setText(String.valueOf(s.getDisplayName()));
            txtPhone.setText(s.getPhoneNumber());
            txtAddress.setText(s.getAddress());
            int getPos = spinArray.getPosition(s.getPosition());
            spinPosition.setSelection(getPos);
            Picasso.get().load(s.getImageURI()).into(imgStaff);
            txtID.setEnabled(false);
            txtPass.setTransformationMethod(null);

            imgStaff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launch2.launch("image/*");
                }
            });

            btnPush.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String newName = txtUser.getText().toString().trim();
                    String newPass = txtPass.getText().toString().trim();
                    String newDisplay = txtDisplay.getText().toString().trim();
                    String newPhone = txtPhone.getText().toString().trim();
                    String newAddress = txtAddress.getText().toString().trim();
                    String newPosition = spinPosition.getSelectedItem().toString();
                    String newUri = uriName2;

                    s.setUsername(newName);
                    s.setPassword(newPass);
                    s.setAddress(newAddress);
                    s.setPhoneNumber(newPhone);
                    s.setPosition(newPosition);
                    s.setDisplayName(newDisplay);
                    s.setImageURI(newUri);
                    myRef.child(String.valueOf(s.getId())).updateChildren(s.toMap(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            Toast.makeText(builder.getContext(), "Update Staff Success  !!!",Toast.LENGTH_SHORT).show();
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
        }catch (Exception exception){
            Toast.makeText(getContext(),"Co loi xay ra vui long nhap lai",Toast.LENGTH_SHORT).show();
            return;
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}