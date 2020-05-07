package com.vignesh.vcall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView recyclerView_notification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        recyclerView_notification = findViewById(R.id.list_notification);
        recyclerView_notification.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
    public static class NotificationViewHolder extends RecyclerView.ViewHolder
    {
        TextView userNameText;
        Button acptBtn,cnlBtn;
        ImageView profileImageView;
        RelativeLayout relativeLayout;
        public NotificationViewHolder(@NonNull View view) {
            super(view);
            userNameText = view.findViewById(R.id.name_notification);
            acptBtn = view.findViewById(R.id.request_accept);
            cnlBtn = view.findViewById(R.id.request_decline);
            profileImageView = view.findViewById(R.id.image_notification);
            relativeLayout = view.findViewById(R.id.carduserview);

        }
    }
}
