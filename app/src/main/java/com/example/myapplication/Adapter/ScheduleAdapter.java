package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Schedule;
import com.example.myapplication.Model.Staff;
import com.example.myapplication.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private List<Schedule> lstSchedule;

    public ScheduleAdapter(List<Schedule> lstSchedule) {
        this.lstSchedule = lstSchedule;
    }

    @NonNull
    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule,parent, false);
        return new ScheduleAdapter.ViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.ViewHolder holder, int position) {
        Schedule schedule = lstSchedule.get(position);
        holder.txtShift.setText(schedule.getShift());
        holder.txtBarista.setText(schedule.getStaffBarista());
        holder.txtWaiter.setText(schedule.getStaffWaiter());
        holder.txtGuard.setText(schedule.getStaffGuard());

    }

    @Override
    public int getItemCount() {
        return lstSchedule.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtBarista, txtWaiter, txtGuard, txtShift;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtShift = itemView.findViewById(R.id.txtItemScheduleShift);
            txtBarista = itemView.findViewById(R.id.txtItemScheduleBarista);
            txtWaiter = itemView.findViewById(R.id.txtItemScheduleWaiter);
            txtGuard = itemView.findViewById(R.id.txtItemScheduleGuard);
        }
    }
}
