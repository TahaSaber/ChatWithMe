package com.example.tahasaber.chatwithme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by TahaSaber on 7/22/2017.
 */

public class EditUserDescriptionActivity extends AppCompatActivity {

    EditText editUserDescription;
    FirebaseUser firebaseUser;
    Button editButton, cancelButton;
    TextView errorMessage;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_about);

        getSupportActionBar().setTitle("Edit Description");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * 0.95), (int) (height * 0.4));
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Bundle bundle = getIntent().getExtras();
        String description = bundle.getString("description");


        editUserDescription = (EditText) findViewById(R.id.user_about_edit_text);
        errorMessage = (TextView) findViewById(R.id.error_message_user_about);

        editUserDescription.setText(description);
        editButton = (Button) findViewById(R.id.edit_user_about_button);

        cancelButton = (Button) findViewById(R.id.cancel_user_about_button);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editUserDescription.getText().toString();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
                databaseReference.child("description").setValue(userName);
                errorMessage.setText("");
                finish();


            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorMessage.setText("");
                finish();

            }
        });


    }
}
