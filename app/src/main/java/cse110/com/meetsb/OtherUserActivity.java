package cse110.com.meetsb;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

import cse110.com.meetsb.Model.User;

public class OtherUserActivity extends AppCompatActivity {

    ImageView profilePicImageView;
    TextView userNameTextView, majorTextView, gpaTextView, descriptionTextView;

    DatabaseReference otherUserRef;
    StorageReference storageRef;

    String otherUserUid;

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
        setContentView(R.layout.activity_other_user);

        otherUserUid = getIntent().getStringExtra("otherUser");

        profilePicImageView = findViewById(R.id.other_user_imageView_avatar_);
        userNameTextView = findViewById(R.id.other_user_textView_name);
        majorTextView = findViewById(R.id.other_user_textView_major);
        gpaTextView = findViewById(R.id.other_user_textView_gpa);
        descriptionTextView = findViewById(R.id.other_user_textView_description);

        otherUserRef = FirebaseDatabase.getInstance().getReference().child("USER").child(otherUserUid);
        storageRef = FirebaseStorage.getInstance().getReference().child("IMAGE").child(otherUserUid);

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageURL = uri.toString();
                Glide.with(getApplicationContext()).load(imageURL).into(profilePicImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        otherUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                String userName = user.getUserName();
                String major = user.getMajor();
                String gpa = user.getGpa();
                String gender = user.getGender();
                String description = user.getDescription();

                userNameTextView.setText(userName);
                majorTextView.setText(major);
                gpaTextView.setText(gpa);
                descriptionTextView.setText(description);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


    }

}
