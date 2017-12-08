package com.example.tahasaber.chatwithme;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.TintAwareDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity {

    private ListView chatHeadsListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseUser cUser;
    private ProgressBar mProgressBar;
    private ChatHeadsAdapter chatHeadsAdapter;
    static ArrayList<UserDataClass> usersData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_heads);
        cUser = FirebaseAuth.getInstance().getCurrentUser();
        if (cUser == null) {
            finish();
        }
        chatHeadsListView = (ListView) findViewById(R.id.chat_heads_list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBarMain);
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        usersData = new ArrayList<>();
        // chatHeadsAdapter = new ChatHeadsAdapter(getApplication(), usersData);
        //chatHeadsListView.setAdapter(chatHeadsAdapter);

        final DatabaseReference userCheck = FirebaseDatabase.getInstance().getReference().child("users");
        userCheck.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (!snapshot.hasChild(cUser.getUid())) {

                    UserDataClass userDataClass = new UserDataClass(cUser.getDisplayName(), cUser.getEmail(), cUser.getUid(), cUser.getPhotoUrl().toString());
                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                    usersRef.child(cUser.getUid()).setValue(userDataClass);
                }
            }

            // }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //   Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        usersData = new ArrayList<UserDataClass>();
                        uploadChatHeads();
                    }
                }
        );


        // function calling
        usersData = new ArrayList<UserDataClass>();
        uploadChatHeads();

        chatHeadsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String myFriend = chatHeadsAdapter.friendsList.get(position).getName();
                // Toast.makeText(getApplicationContext(), myFriend, Toast.LENGTH_LONG).show();

                DatabaseReference chatIdDB;
                chatIdDB = FirebaseDatabase.getInstance().getReference().child("friends").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(chatHeadsAdapter.friendsList.get(position).getId());

                chatIdDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            // TODO: handle the post
                            String chatId = userSnapshot.getKey();
                            //  Toast.makeText(getApplicationContext(), chatId, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                            intent.putExtra("chatId", chatId);
                            intent.putExtra("myFriend", myFriend);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    public void addNewUser(View v) {

        startActivity(new Intent(MainActivity.this, AddUserActivity.class));

    }


    public void uploadChatHeads() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            finish();
        }
        usersData.clear();
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("friends").child(cUser.getUid());

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                    String Uid = userSnapshot.getKey();
                    DatabaseReference userObjectDBRef;

                    userObjectDBRef = FirebaseDatabase.getInstance().getReference().child("users").child(Uid);


                    usersData.clear();
                    userObjectDBRef.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            UserDataClass user = dataSnapshot.getValue(UserDataClass.class);
                            usersData.add(user);

                            // chatHeadsAdapter.notifyDataSetChanged();
                            //chatHeadsAdapter.getItemId(usersData.size() - 1);
                            Toast.makeText(getApplicationContext(), Integer.toString(usersData.size()), Toast.LENGTH_SHORT).show();
                            chatHeadsAdapter = new ChatHeadsAdapter(getApplication(), usersData);
                            chatHeadsListView.setAdapter(chatHeadsAdapter);
                            swipeRefreshLayout.setRefreshing(false);
                            mProgressBar.setVisibility(ProgressBar.INVISIBLE);

                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.profile_view:
                Intent userProfileActivty = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(userProfileActivty);


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}