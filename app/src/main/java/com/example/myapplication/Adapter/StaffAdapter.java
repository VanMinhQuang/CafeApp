package com.example.myapplication.Adapter;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Staff;
import com.example.myapplication.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.ViewHolder> {

    List<Staff> lstStaff;
    CircleImageView imgStaff;
    ActivityResultLauncher<String> launcher;
    FirebaseStorage storage;
    String uriName = "";





    public StaffAdapter(List<Staff> lstStaff) {
        this.lstStaff = lstStaff;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public StaffAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff,parent, false);
        return new ViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffAdapter.ViewHolder holder, int position) {
        Staff s = lstStaff.get(position);
        holder.txtID.setText(String.valueOf(s.getId()));
        holder.txtName.setText(s.getUsername());
        holder.txtPos.setText(s.getPosition());
        Picasso.get().load(s.getImageURI()).into(holder.imgCircleAvatar, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View viewDialogStaff = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_staff,null);
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
                spinArray = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_item, listSpin);
                spinArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinPosition.setAdapter(spinArray);

                Picasso.get().load(s.getImageURI()).into(imgStaff, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                txtID.setText(String.valueOf(s.getId()));
                txtUser.setText(s.getUsername());
                txtPass.setText(s.getPassword());
                txtDisplay.setText(String.valueOf(s.getDisplayName()));
                txtPhone.setText(s.getPhoneNumber());
                txtAddress.setText(s.getAddress());
                int getPos = spinArray.getPosition(s.getPosition());
                spinPosition.setSelection(getPos);
                txtID.setEnabled(false);
                txtPass.setTransformationMethod(null);
                btnPush.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String newName = txtUser.getText().toString().trim();
                        String newPass = txtPass.getText().toString().trim();
                        String newDisplay = txtDisplay.getText().toString().trim();
                        String newPhone = txtPhone.getText().toString().trim();
                        String newAddress = txtAddress.getText().toString().trim();
                        String newPosition = spinPosition.getSelectedItem().toString();

                        s.setUsername(newName);
                        s.setPassword(newPass);
                        s.setAddress(newAddress);
                        s.setPhoneNumber(newPhone);
                        s.setPosition(newPosition);
                        s.setDisplayName(newDisplay);
                        myRef.child(String.valueOf(s.getId())).updateChildren(s.toMap(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(builder.getContext(), "Update Staff Successfull!!!",Toast.LENGTH_SHORT).show();
                                alert.dismiss();
                            }
                        });
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                        ;
                    }
                });

            }
        });

    }


    public void deleteStaffAsPosition(int  pos){
        Staff staff = lstStaff.get(pos);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Staff");
        myRef.child(String.valueOf(staff.getId())).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
            }
        });
        notifyItemRemoved(pos);
    }


    @Override
    public int getItemCount() {
        return lstStaff.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imgCircleAvatar;
        TextView txtID, txtName, txtPos;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCircleAvatar = itemView.findViewById(R.id.imgItemImageStaff);
            txtID = itemView.findViewById(R.id.txtItemIDStaff);
            txtName = itemView.findViewById(R.id.txtItemNameStaff);
            txtPos = itemView.findViewById(R.id.txtItemPositionStaff);
            progressBar = itemView.findViewById(R.id.progressBarStaff);
        }
    }

}
