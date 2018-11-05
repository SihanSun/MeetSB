package cse110.com.meetsb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cse110.com.meetsb.Model.User;


public class ClassInfoActivity extends AppCompatActivity {
    Button addClass;
    Button submit;

    String userName;
    String filePathStr;
    String gender;
    String description;
    String gpaString;
    String major;
    List<String> courseTaking = new ArrayList<>();

    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    ArrayAdapter<String> adapter;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_info);

        //set firebase storage
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        //set list view
        ListView lv = (ListView)findViewById(R.id.class_info_listview_classlist);
        ArrayList<String> arrayClass = new ArrayList<>();
        arrayClass.addAll(Arrays.asList(getResources().getStringArray(R.array.class_array)));
        adapter = new ArrayAdapter<>(ClassInfoActivity.this,
                android.R.layout.simple_list_item_1,
                arrayClass);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                courseTaking.add(view.toString());
            }
        });

        //get extras
        userName = getIntent().getStringExtra("USERNAME");
        description = getIntent().getStringExtra("DESCRIPTION");
        gender = getIntent().getStringExtra("GENDER");
        gpaString = getIntent().getStringExtra("GPA");
        major = getIntent().getStringExtra("MAJOR");
        filePathStr = getIntent().getStringExtra("IMAGE");

        //set progress bar
        progressDialog = new ProgressDialog(this);
        //set button
        submit = (Button) findViewById(R.id.class_info_button_submit) ;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitInfo();
            }
        });

        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this,
                R.array.class_array,
                R.layout.activity_class_info);
    }

    private void submitInfo() {
        //set the new user
        User newUser = new User();
        newUser.getPersonalInformation().setUserName(userName);
        newUser.getPersonalInformation().setDescription(description);
        newUser.getPersonalInformation().setGender(gender);
        newUser.getAcademicInformation().getCourseTaking().add("CSE110");
        newUser.getAcademicInformation().setGpa(gpaString);
        newUser.getAcademicInformation().setMajor(major);

        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        String userId = firebaseAuth.getCurrentUser().getUid();
        databaseReference.child("USER").child(userId).setValue(newUser);

        StorageReference childRef = storageRef.child("IMAGE").child(userId);

        //convert filePathString to Uri
        Uri filePath = Uri.parse(filePathStr);
        //uploading the image
        UploadTask uploadTask = childRef.putFile(filePath);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(ClassInfoActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ClassInfoActivity.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
            }
        });

        progressDialog.dismiss();
        Intent intent = new Intent(this, SwipeActivity.class);
        finish();
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.search_class);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
