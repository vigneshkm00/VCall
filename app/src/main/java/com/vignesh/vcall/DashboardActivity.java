package com.vignesh.vcall;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardActivity extends AppCompatActivity {
    BottomNavigationView navView;
    RecyclerView contactList;
    ImageView findPplImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        navView = findViewById(R.id.nav_view);
        findPplImage = findViewById(R.id.findpplbtn);
        contactList = findViewById(R.id.contact_list);
        contactList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        findPplImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this,FindpplActivity.class);
                startActivity(i);
            }
        });


    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.navigation_home:
                    Intent homeIntent = new Intent(DashboardActivity.this,DashboardActivity.class);
                    startActivity(homeIntent);
                    break;
                case R.id.navigation_settings:
                    Intent settingIntent = new Intent(DashboardActivity.this,SettingActivity.class);
                    startActivity(settingIntent);
                    break;
                case R.id.navigation_notifications:
                    Intent notificationIntent = new Intent(DashboardActivity.this,NotificationActivity.class);
                    startActivity(notificationIntent);
                    break;
                case R.id.navigation_logout:
                    FirebaseAuth.getInstance().signOut();
                    Intent logoutIntent = new Intent(DashboardActivity.this,LoginActivity.class);
                    startActivity(logoutIntent);
                    finish();
                    break;
            }
            return true;
        }
    };

}
