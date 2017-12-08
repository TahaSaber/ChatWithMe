package com.example.tahasaber.chatwithme;

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

public class EditUserNameActivity extends AppCompatActivity {

    EditText editUserName;
    FirebaseUser firebaseUser;
    Button editButton, cancelButton;
    TextView errorMessage;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);

        getSupportActionBar().setTitle("Edit User Name");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * 0.95), (int) (height * 0.4));
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        editUserName = (EditText) findViewById(R.id.user_name_edit_text);
        errorMessage = (TextView) findViewById(R.id.error_message_user_name);
        Bundle bundle = getIntent().getExtras();
        String userName = bundle.getString("name");

        editUserName.setText(userName);
        editButton = (Button) findViewById(R.id.edit_name_button);

        cancelButton = (Button) findViewById(R.id.cancel_button_user_name);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editUserName.getText().toString();
                if (userName.matches("")) {
                    errorMessage.setText("You must enter user name");
                } else {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
                    databaseReference.child("name").setValue(userName);
                    errorMessage.setText("");
                    finish();
                }

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
