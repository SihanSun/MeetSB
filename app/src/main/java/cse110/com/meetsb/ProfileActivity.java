package cse110.com.meetsb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;

import java.io.InputStream;

import cse110.com.meetsb.Model.User;

public class ProfileActivity extends AppCompatActivity {
    Button editButton;
    Button signOutButton;
    Button backButton;
    FirebaseAuth auth;
    DatabaseReference ref;
    FirebaseStorage storage;
    StorageReference storageRef;
    private FirebaseAuth.AuthStateListener authListener;

    TextView userNameText;
    TextView emailText;
    TextView majorText;
    TextView gpaText;
    TextView genderText;
    TextView descriptionsText;
    ImageView profilePictureImageView;

    ProgressDialog progressDialog;

    @GlideModule
    public class MyAppGlideModule extends AppGlideModule {

        @Override
        public void registerComponents(Context context, Glide glide, Registry registry) {
            // Register FirebaseImageLoader to handle StorageReference
            registry.append(StorageReference.class, InputStream.class,
                    new FirebaseImageLoader.Factory());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userNameText = findViewById(R.id.profile_textView_userName);
        emailText = findViewById(R.id.profile_textView_email);
        majorText = findViewById(R.id.profile_textView_major);
        gpaText = findViewById(R.id.profile_textView_GPA);
        genderText = findViewById(R.id.profile_textView_gender);
        descriptionsText = findViewById(R.id.profile_textView_description);
        profilePictureImageView = findViewById(R.id.profile_imageView_avatar);

        editButton = findViewById(R.id.profile_button_ToSetting);
        signOutButton = findViewById(R.id.profile_button_signOut);
        backButton = findViewById(R.id.profile_button_back);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSetting();
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(ProfileActivity.this, SigninActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Hang on tight...");
        progressDialog.show();

        auth = FirebaseAuth.getInstance();

        emailText.setText(auth.getCurrentUser().getEmail());

        ref = FirebaseDatabase.getInstance().getReference().child("USER").child(auth.getCurrentUser().getUid());
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("IMAGE").child(auth.getCurrentUser().getUid());

//        // Download directly from StorageReference using Glide
//        // (See MyAppGlideModule for Loader registration)
//        Glide.with(this /* context */)
//                .load(storageRef)
//                .into(profilePictureImageView);
      storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageURL = uri.toString();
                Glide.with(getApplicationContext()).load(imageURL).into(profilePictureImageView);
            }
        }).addOnSuccessListener(new OnSuccessListener<Uri>() {
          @Override
          public void onSuccess(Uri uri) {
              ref.addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      User user = dataSnapshot.getValue(User.class);

                      String userName = user.getUserName();
                      String major = user.getMajor();
                      String gpa = user.getGpa();
                      String gender = user.getGender();
                      String description = user.getDescription();

                      userNameText.setText(userName);
                      majorText.setText(major);
                      gpaText.setText(gpa);
                      genderText.setText(gender);
                      descriptionsText.setText(description);

                      progressDialog.dismiss();
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError databaseError) {
                      Toast.makeText(getApplicationContext(), databaseError.getMessage(),Toast.LENGTH_LONG).show();
                      progressDialog.dismiss();
                  }
              });
          }
      }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
              progressDialog.dismiss();
          }
      });
    }

    public void openSetting() {
        finish();
        Intent intent = new Intent(ProfileActivity.this, SettingActivity.class);
        intent.putExtra("USERNAME", userNameText.getText().toString());
        intent.putExtra("GENDER",genderText.getText().toString());
        intent.putExtra("DESCRIPTION",descriptionsText.getText().toString());
        intent.putExtra("MAJOR", majorText.getText().toString());
        intent.putExtra("GPA", gpaText.getText().toString());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
