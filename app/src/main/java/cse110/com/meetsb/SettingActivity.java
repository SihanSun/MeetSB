package cse110.com.meetsb;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
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

import java.io.ByteArrayOutputStream;

import cse110.com.meetsb.Model.Course;

public class SettingActivity extends AppCompatActivity {
    Button submit;
    ImageView profilePictureImageView;
    EditText userNameEditText;
    EditText gpaEditText;
    EditText descriptionEditText;
    Spinner majorSpinner;
    Spinner genderSpinner;
    StorageReference storageRef;
    FirebaseAuth auth;
    DatabaseReference userRef;

    private int PICK_IMAGE_REQUEST = 1;
    Uri filePath;

    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        progressDialog = new ProgressDialog(this);

        profilePictureImageView = findViewById(R.id.setting_imageView_avatar);
        userNameEditText = findViewById(R.id.setting_editText_username);
        gpaEditText = findViewById(R.id.setting_editText_GPA);
        descriptionEditText = findViewById(R.id.setting_editText_description);
        majorSpinner = findViewById(R.id.setting_spinner_major);
        genderSpinner = findViewById(R.id.setting_spinner_gender);
        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("USER").child(uid);

        // set profile image
        storageRef = FirebaseStorage.getInstance().getReference().child("IMAGE").child(uid);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageURL = uri.toString();
                Glide.with(getApplicationContext()).load(imageURL).into(profilePictureImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


        Intent intent = getIntent();

        userNameEditText.setText(intent.getStringExtra("USERNAME"));
        gpaEditText.setText(intent.getStringExtra("GPA"));
        descriptionEditText.setText(intent.getStringExtra("DESCRIPTION"));
        majorSpinner.setSelection(getIndex(majorSpinner, intent.getStringExtra("MAJOR")));
        genderSpinner.setSelection(getIndex(genderSpinner, intent.getStringExtra("GENDER")));

        submit = findViewById(R.id.setting_button_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitInfo();

            }
        });

        profilePictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkIfAlreadyHavePermission()) {
                    ActivityCompat.requestPermissions(SettingActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });
    }

    private int getIndex(Spinner spinner, String value){
        for(int i = 0; i < spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value))
                return i;
        }
        return 0;
    }

    private void submitInfo(){
        String userName = userNameEditText.getText().toString();
        String major = majorSpinner.getSelectedItem().toString();
        String gender = genderSpinner.getSelectedItem().toString();
        String gpa = gpaEditText.getText().toString();
        String description = descriptionEditText.getText().toString();



        if(userName.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please enter your username", Toast.LENGTH_SHORT).show();
            return;
        }

        if(description.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please introduce yourself", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!gpa.equals("Not available")) {
            if (!gpa.isEmpty()) {
                double gpaDouble;
                try {
                    gpaDouble = Double.parseDouble(gpa);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid GPA", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (gpaDouble < 0 || gpaDouble > 4) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid GPA", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else
                gpa = "Not available";
        }

        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        userRef.child("userName").setValue(userName);
        userRef.child("major").setValue(major);
        userRef.child("gender").setValue(gender);
        userRef.child("gpa").setValue(gpa);
        userRef.child("description").setValue(description);

        if(filePath != null) {
            UploadTask uploadTask = storageRef.putFile(filePath);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    finish();
                    startActivity(new Intent(SettingActivity.this, ProfileActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    //Toast.makeText(ClassInfoActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SettingActivity.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                    return;
                }
            });
        }
        else {
            progressDialog.dismiss();
            finish();
            startActivity(new Intent(SettingActivity.this, ProfileActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

    }

    private boolean checkIfAlreadyHavePermission() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //convert bitmap to string
                ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
                byte [] b=baos.toByteArray();

                //Setting image to ImageView
                profilePictureImageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        startActivity(new Intent(SettingActivity.this, ProfileActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}
