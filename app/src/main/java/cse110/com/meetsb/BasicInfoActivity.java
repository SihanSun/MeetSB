package cse110.com.meetsb;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

public class BasicInfoActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    private int PICK_IMAGE_REQUEST = 1;
    EditText userNameInput;
    EditText descriptionInput;
    Button continueBtn;
    Spinner gender;
    ImageView imageView;
    Uri filePath;
    String imageString;

    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info);

        //set all the buttons
        imageView = (ImageView) findViewById(R.id.basic_info_imageView_avatar);
        userNameInput = (EditText) findViewById(R.id.basic_info_editText_username);
        descriptionInput = (EditText) findViewById(R.id.basic_info_multilineText_description);
        gender = (Spinner)findViewById(R.id.basic_info_spinner_gender);
        continueBtn = (Button) findViewById(R.id.basic_info_button_continue);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        imageString = null;

        //set on click listener for continue button
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitInfo();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkIfAlreadyhavePermission()) {
                    ActivityCompat.requestPermissions(BasicInfoActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });
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

                //store the bitmap string to the image String
                imageString = Base64.encodeToString(b, Base64.DEFAULT);

                //Setting image to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void submitInfo() {
        String userName = userNameInput.getText().toString();
        String genderOption = gender.getSelectedItem().toString();
        String description = descriptionInput.getText().toString();

        if(imageString == null) {
            Toast.makeText(this, "Please upload your picture.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter your user name.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(description.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please introduce yourself.", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "DESCRIPTION IS " + description, Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(this, AcademicInfoActivity.class);
        intent.putExtra("USERNAME", userName);
        intent.putExtra("GENDER",genderOption);
        intent.putExtra("DESCRIPTION",genderOption);
        intent.putExtra("IMAGE", filePath);
        startActivity(intent);

    }

    private boolean checkIfAlreadyhavePermission() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
}
