package com.example.tahasaber.chatwithme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


/**
 * Created by TahaSaber on 2/18/2017.
 */

public class UserProfileActivity extends AppCompatActivity {

    private static final int RC_PHOTO_PICKER = 2;
    private CircleImageView profilePic;
    private ImageButton editButton;
    public static TextView userNameTextView, emailTextView, aboutTextView;
    private static final int CAMERA_PIC_REQUEST = 100;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mProfilePhotosStorageReference;
    private String userDescription;
    private UserDataClass currentUser;
   // private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        userNameTextView = (TextView) findViewById(R.id.edit_user_name);
        emailTextView = (TextView) findViewById(R.id.user_email);
        aboutTextView = (TextView) findViewById(R.id.user_about);
        emailTextView.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        getSupportActionBar().setTitle("Profile");
       // progressBar = (ProgressBar) findViewById(R.id.progressBarProfilePhoto);
        profilePic = (CircleImageView) findViewById(R.id.profile_image_editable);
        mFirebaseStorage = FirebaseStorage.getInstance();
        mProfilePhotosStorageReference = mFirebaseStorage.getReference().child("profile_photos");

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(UserDataClass.class);
                userDescription = currentUser.getDescription();
                aboutTextView.setText(currentUser.getDescription());
                userNameTextView.setText(currentUser.getName());
                profilePic.setVisibility(VISIBLE);
                Glide.with(profilePic.getContext())
                        .load(currentUser.getPhotoUrl())
                        .into(profilePic);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        editButton = (ImageButton) findViewById(R.id.myButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // progressBar.setVisibility(VISIBLE);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


            // Get a reference to store file at chat_photos/<FILENAME>
            StorageReference photoRef = mProfilePhotosStorageReference.child(selectedImageUri.getLastPathSegment());

            // Upload file to Firebase Storage
            photoRef.putFile(selectedImageUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // When the image has successfully uploaded, we get its download URL
                            @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Toast.makeText(getApplication(), "Photo edited successfully", Toast.LENGTH_SHORT).show();
                          //  progressBar.setVisibility(INVISIBLE);

                            // Set the download URL to the message box, so that the user can send it to the database
                            mDatabase.child("photoUrl").setValue(downloadUrl.toString());
                        }
                    });
        }
    }

    public void editName(View v) {

        Intent moveToEditUserNameActivity = new Intent(UserProfileActivity.this, EditUserNameActivity.class);
        moveToEditUserNameActivity.putExtra("name", currentUser.getName());
        startActivity(moveToEditUserNameActivity);
    }

    public void editAbout(View v) {
        Intent moveToEditUserDescriptionActivity = new Intent(UserProfileActivity.this, EditUserDescriptionActivity.class);
        moveToEditUserDescriptionActivity.putExtra("description", userDescription);
        startActivity(moveToEditUserDescriptionActivity);
    }


}