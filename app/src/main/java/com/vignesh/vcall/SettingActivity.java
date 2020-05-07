package com.vignesh.vcall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {
    ImageView avatar;
    EditText uname,ustatus;
    Button saveBtn;
    private static int GalleryPic = 1;
    private Uri imageuri;
    private StorageReference userProfileImgRef;
    private String downloadUrl;
    private DatabaseReference userdatabaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        avatar = findViewById(R.id.avatar_setting);
        uname = findViewById(R.id.name_setting);
        ustatus = findViewById(R.id.status_setting);
        saveBtn = findViewById(R.id.save_setting);
        progressDialog = new ProgressDialog(this);
        userProfileImgRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        userdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,GalleryPic);

            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
        retriveUserInfo();
    }

    private void saveData() {
        final String getUserName = uname.getText().toString();
        final String getStatus = ustatus.getText().toString();
        if(imageuri == null){
            userdatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).hasChild("image"))
                    {
                        saveInfoOnlyWithoutImg();
                    }
                    else
                    {
                        Toast.makeText(SettingActivity.this,"Please Sellect Profile Picture",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else if(uname.equals("")){
            Toast.makeText(SettingActivity.this,"User name is mandatory",Toast.LENGTH_LONG).show();
        }
        else if (ustatus.equals("")){
            Toast.makeText(SettingActivity.this,"User status is mandatory",Toast.LENGTH_LONG).show();
        }
        else {
            progressDialog.setTitle("Account Setting");
            progressDialog.setMessage("Please Wait");
            progressDialog.show();
            final StorageReference filepath = userProfileImgRef.
                    child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            final UploadTask uploadTask = filepath.putFile(imageuri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    downloadUrl = filepath.getDownloadUrl().toString();
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        downloadUrl = task.getResult().toString();
                        HashMap<String, Object> profileMap = new HashMap<>();
                        profileMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        profileMap.put("name",getUserName);
                        profileMap.put("status",getStatus);
                        profileMap.put("image",downloadUrl);
                        userdatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                              if (task.isSuccessful())
                              {
                                  Intent i = new Intent(SettingActivity.this,DashboardActivity.class);
                                  startActivity(i);
                                  finish();
                                  progressDialog.dismiss();
                                  Toast.makeText(SettingActivity.this,"Profile Updates",Toast.LENGTH_LONG).show();
                              }
                            }
                        });
                    }
                }
            });
        }
    }

    private void saveInfoOnlyWithoutImg() {
        final String getUserName = uname.getText().toString();
        final String getStatus = ustatus.getText().toString();

        if(uname.equals("")){
            Toast.makeText(SettingActivity.this,"User name is mandatory",Toast.LENGTH_LONG).show();
        }
        else if (ustatus.equals("")){
            Toast.makeText(SettingActivity.this,"User status is mandatory",Toast.LENGTH_LONG).show();
        }
        else
            {
                progressDialog.setTitle("Account Setting");
                progressDialog.setMessage("Please Wait");
                progressDialog.show();
                HashMap<String, Object> profileMap = new HashMap<>();
                profileMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                profileMap.put("name",getUserName);
                profileMap.put("status",getStatus);
                userdatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent i = new Intent(SettingActivity.this,DashboardActivity.class);
                            startActivity(i);
                            finish();
                            progressDialog.dismiss();
                            Toast.makeText(SettingActivity.this,"Profile Updates",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GalleryPic && resultCode == RESULT_OK && data!=null)
        {
            System.out.println(" am here");
            imageuri = data.getData();
            avatar.setImageURI(imageuri);
        }
    }
    private void retriveUserInfo()
    {
        userdatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String imageDb = dataSnapshot.child("image").getValue().toString();
                    String nameDb = dataSnapshot.child("name").getValue().toString();
                    String statusDb = dataSnapshot.child("status").getValue().toString();

                    uname.setText(nameDb);
                    ustatus.setText(statusDb);
                    Picasso.get().load(imageDb).placeholder(R.drawable.profile_image).into(avatar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
