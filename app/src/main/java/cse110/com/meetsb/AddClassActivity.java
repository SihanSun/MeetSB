package cse110.com.meetsb;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import cse110.com.meetsb.Model.User;

public class AddClassActivity extends AppCompatActivity {

    Button submit;

    private FirebaseDatabase databaseInstance;
    private DatabaseReference databaseRef;
    FirebaseAuth auth;
    ArrayAdapter<String> adapter;
    ArrayList<String> selectClass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_info);


        //set list view
        final ListView lv = (ListView)findViewById(R.id.class_info_listview_classlist);
        final ArrayList<String> arrayClass = new ArrayList<>();
        arrayClass.addAll(Arrays.asList(getResources().getStringArray(R.array.class_array)));
        selectClass = new ArrayList<String>();
        adapter = new ArrayAdapter<>(AddClassActivity.this,
                android.R.layout.simple_list_item_1,
                arrayClass);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //courseTaking.add(view.toString());
                //TODO
                //add the existing classes to the view
                String item = (lv.getItemAtPosition(i)).toString();
                if (!selectClass.contains(item)) {
                    selectClass.add(item);
                    ListView sl = (ListView)findViewById(R.id.class_info_listview_addedClass);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                            android.R.layout.simple_list_item_1, selectClass);
                    sl.setAdapter(adapter);
                }
            }
        });



        submit = findViewById(R.id.class_info_button_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                //Save infomation in the fire base

            }
        });

        setContentView(R.layout.activity_class_info);

    }



}
