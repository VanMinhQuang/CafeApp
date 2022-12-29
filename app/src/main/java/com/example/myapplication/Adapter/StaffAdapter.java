package com.example.myapplication.Adapter;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Staff;
import com.example.myapplication.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.ViewHolder> {

    List<Staff> lstStaff;
    private mIClickListener mIClickListener; //CHÚ Ý CÁI NÀY - tạo cái interface đê thực hiện chỉnh sửa ngay trong phần Fragment
    public interface  mIClickListener{
        void onClickListener(Staff s);
    }


    public StaffAdapter(List<Staff> lstStaff,  mIClickListener listener) { //Gọi interface vào constructor, không có vấn đề
        this.lstStaff = lstStaff;
        this.mIClickListener = listener;
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
                mIClickListener.onClickListener(s); //Đây là lúc sử dụng interface

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
