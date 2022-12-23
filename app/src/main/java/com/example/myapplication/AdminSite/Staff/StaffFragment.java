package com.example.myapplication.AdminSite.Staff;

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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class StaffFragment extends Fragment implements OnClickImageListener{

    private FragmentStaffBinding binding;
    private EditText txtID, txtUser, txtPass, txtDisplay, txtPhone, txtAddress;
    private CircleImageView profilePic;
    private FloatingActionButton btnSave;
    private Button btnPush, btnCancel;
    private Spinner spinPosition;
    private RecyclerView rcvStaff;
    private StaffAdapter adapter;
    private List<Staff> lstStaff;
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    private String uriName = "";

    ActivityResultLauncher<String> launcher;




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStaffBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        anhxa(root.getRootView());
        lstStaff = new ArrayList<>();
        adapter = new StaffAdapter(lstStaff);
        rcvStaff.setAdapter(adapter);


        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                profilePic.setImageURI(result);
                uploadImageToFirebase(result);
            }
        });


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
                        launcher.launch("image/*");
                    }
                });

                btnPush.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        onClickAddStaff();
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

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeItemStaff(adapter));
        itemTouchHelper.attachToRecyclerView(rcvStaff);
        getAllStaff();
        return root;


    }


    public String uploadImageToFirebase(Uri uri){
        storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference("StaffImage").child(System.currentTimeMillis() + "." +getFileExtension(uri));
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

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onClick() {
        launcher.launch("image/*");
    }


}