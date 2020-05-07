package com.vignesh.vcall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private String receiveUserId="",receiveUsrImg = "", receiveUsrName = "";
    ImageView profileImage;
    TextView profileName;
    Button addFrndBtn,cancelReqBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        receiveUserId = getIntent().getExtras().get("visituserid").toString();
        receiveUsrImg = getIntent().getExtras().get("profile_image").toString();
        receiveUsrName = getIntent().getExtras().get("profile_name").toString();
        profileImage = findViewById(R.id.image_profile);
        profileName = findViewById(R.id.name_profile);

        Picasso.get().load(receiveUsrImg).into(profileImage);
        profileName.setText(receiveUsrName);

    }
}
