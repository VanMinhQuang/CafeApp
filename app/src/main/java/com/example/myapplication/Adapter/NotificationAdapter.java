package com.example.myapplication.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.Model.Notification;
import com.example.myapplication.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<Notification> lstNotification;

    public NotificationAdapter(List<Notification> lstNotification) {
        this.lstNotification = lstNotification;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification,parent, false);
        return new NotificationAdapter.ViewHolder(viewHolder);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        Notification notification = lstNotification.get(position);
        holder.txtName.setText("Quản lý " +notification.getName()+":");
        holder.txtText.setText(notification.getText());
        holder.txtDate.setText(notification.getDateTime());
    }

    @Override
    public int getItemCount() {
        return lstNotification.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName,txtText,txtDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtNotificationName);
            txtText = itemView.findViewById(R.id.txtNotificationText);
            txtDate = itemView.findViewById(R.id.txtNotificationDate);
        }
    }
}
