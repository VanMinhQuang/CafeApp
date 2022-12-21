package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Staff;
import com.example.myapplication.R;

import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.ViewHolder> {

    List<Staff> lstStaff;

    public StaffAdapter(List<Staff> lstStaff) {
        this.lstStaff = lstStaff;
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

    }

    @Override
    public int getItemCount() {
        return lstStaff.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtID, txtName, txtPos;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtID = itemView.findViewById(R.id.txtItemIDStaff);
            txtName = itemView.findViewById(R.id.txtItemNameStaff);
            txtPos = itemView.findViewById(R.id.txtItemPositionStaff);
        }
    }
}
