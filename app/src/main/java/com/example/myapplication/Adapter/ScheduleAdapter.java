package com.example.myapplication.Adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Product;
import com.example.myapplication.Model.Schedule;
import com.example.myapplication.Model.Staff;
import com.example.myapplication.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private List<Schedule> lstSchedule;

    onClickHelper listener;
    public interface onClickHelper{
        public void adjustProduct(Schedule schedule);
    }
    public ScheduleAdapter(List<Schedule> lstSchedule, onClickHelper listener) {
        this.lstSchedule = lstSchedule;
        this.listener = listener;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.adjustProduct(schedule);
            }
        });

    }

    public void deleteScheduleAsPositon(int pos){
        Schedule schedule = lstSchedule.get(pos);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Schedule");
        myRef.child(String.valueOf(schedule.getScheduleID())).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
            }
        });
        notifyItemRemoved(pos);
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
