package com.vignesh.vcall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FindpplActivity extends AppCompatActivity {
    RecyclerView findpplcontactlist;
    EditText searchName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findppl);
        searchName = findViewById(R.id.search_name);
        findpplcontactlist = findViewById(R.id.findppl_contact_list);
        findpplcontactlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder
    {
        TextView userNameText;
        Button videocallbtn;
        ImageView profileImageView;
        RelativeLayout relativeLayout;
        public FindFriendsViewHolder(@NonNull View view) {
            super(view);
            userNameText = view.findViewById(R.id.name_contact);
            videocallbtn = view.findViewById(R.id.call_contact);
            profileImageView = view.findViewById(R.id.image_contact);
            relativeLayout = view.findViewById(R.id.carduserview_contact);

        }
    }
}
