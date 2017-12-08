package com.example.tahasaber.chatwithme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by TahaSaber on 7/10/2017.
 */

public class AddUserActivity extends Activity {

    Button addButton, cancelButton;
    EditText userEmail;
    TextView errorMessage;
    static int flag = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user_email);

      //  getSupportActionBar().setTitle("Add New Friend");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * 0.95), (int) (height * 0.4));

        addButton = (Button) findViewById(R.id.add_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        userEmail = (EditText) findViewById(R.id.user_email_edit_text);
        errorMessage = (TextView) findViewById(R.id.error_message);
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flag = 0;
                final String email = userEmail.getText().toString();

                String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
                Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(email);

                if (!matcher.matches()) {
                    int textColor = Color.parseColor("#ff3300");
                    errorMessage.setTextColor(textColor);
                    errorMessage.setText("Sorry! Invalid email address");

                } else {

                    final DatabaseReference mDatabase1, mDatabase2;
                    mDatabase1 = FirebaseDatabase.getInstance().getReference().child("users");
                    mDatabase2 = FirebaseDatabase.getInstance().getReference().child("friends").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


                    mDatabase1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                // TODO: handle the post
                                final UserDataClass userDataClass = userSnapshot.getValue(UserDataClass.class);
                                if (userDataClass.getEmail().equals(email)) {

                                    flag = 1;
                                    mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {

                                            // for (DataSnapshot friendSnapshot : snapshot.getChildren()) {
                                            // String friendId = friendSnapshot.getKey();
                                            if (!snapshot.hasChild(userDataClass.getId())) {
                                                String chatId = userDataClass.getId() + FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                MessageDataClass welcome = new MessageDataClass("You are now connected on ChatWithMe", "app", "app", null);
                                                DatabaseReference chats;
                                                chats = FirebaseDatabase.getInstance().getReference().child("chats");
                                                chats.child(chatId).child("message").push().setValue(welcome);
                                                chats = FirebaseDatabase.getInstance().getReference().child("friends");
                                                chats.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(userDataClass.getId()).child(chatId).setValue("1");

                                                chats = FirebaseDatabase.getInstance().getReference().child("friends");
                                                chats.child(userDataClass.getId()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(chatId).setValue("1");


                                                flag = 2;
                                                int textColor = Color.parseColor("#4cee3d");
                                                errorMessage.setTextColor(textColor);
                                                errorMessage.setText("Friend added succeefully");

                                            }
                                        }

                                        // }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                }
                            }

                            if (flag == 1) {
                                int textColor = Color.parseColor("#4cee3d");
                                errorMessage.setTextColor(textColor);
                                errorMessage.setText("Friend has already added");
                            }

                            if (flag != 1 && flag != 2) {
                                int textColor = Color.parseColor("#ff3300");
                                errorMessage.setTextColor(textColor);
                                errorMessage.setText("Sorry! this user is not available now");
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


            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorMessage.setText("");
                userEmail.setText("");
                finish();

            }
        });

    }


}
