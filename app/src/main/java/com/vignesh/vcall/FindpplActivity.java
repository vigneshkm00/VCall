package com.vignesh.vcall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FindpplActivity extends AppCompatActivity {
    RecyclerView findpplcontactlist;
    EditText searchName;
    private String searchvar = "";
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findppl);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        searchName = findViewById(R.id.search_name);
        findpplcontactlist = findViewById(R.id.findppl_contact_list);
        findpplcontactlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(searchName.getText().toString().equals(""))
                {
                    //...
                }
                else {
                    searchvar = s.toString();
                    onStart();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Contacts> options = null;
        if(searchvar.equals(""))
        {
            options = new FirebaseRecyclerOptions.Builder<Contacts>()
                    .setQuery(userRef,Contacts.class)
                    .build();
        }
        else {
            options = new FirebaseRecyclerOptions.Builder<Contacts>()
                    .setQuery(userRef
                            .orderByChild("name")
                            .startAt(searchvar)
                            .endAt(searchvar + "\uf8ff"),Contacts.class)
                    .build();
        }
        FirebaseRecyclerAdapter<Contacts,FindFriendsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Contacts, FindFriendsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindFriendsViewHolder findFriendsViewHolder, final int i, @NonNull final Contacts contacts)
            {
                findFriendsViewHolder.userNameText.setText(contacts.getName());
                Picasso.get().load(contacts.getImage()).into(findFriendsViewHolder.profileImageView);
                findFriendsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String visit_userd_id = getRef(i).getKey();
                        Intent i = new Intent(FindpplActivity.this,ProfileActivity.class);
                        i.putExtra("visituserid", visit_userd_id);
                        i.putExtra("profile_image", contacts.getImage());
                        i.putExtra("profile_name", contacts.getName());
                        startActivity(i);

                    }
                });

            }

            @NonNull
            @Override
            public FindFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_layout,parent,false);
                FindFriendsViewHolder viewHolder = new FindFriendsViewHolder(view);
                return viewHolder;
            }
        };
        findpplcontactlist.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
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
            videocallbtn.setVisibility(View.GONE);

        }
    }
}
