
package cse110.com.meetsb;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cse110.com.meetsb.Model.User;

public class AddClassActivity extends AppCompatActivity {

    Button submit;

    private FirebaseDatabase databaseInstance;
    private DatabaseReference userRef;
    FirebaseAuth auth;
    ArrayAdapter<String> adapter;
    ArrayList<String> courseTaking;
    HashMap<String, Integer> mapCourseTaking = new HashMap<>();

    //User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_info);

        final ListView lvAddedClasses = findViewById(R.id.class_info_listview_addedClass);
        final ListView lvClassesOffered = findViewById(R.id.class_info_listview_classlist);
        submit = findViewById(R.id.class_info_button_submit);

        // set up firebase relevant
        auth = FirebaseAuth.getInstance();
        databaseInstance = FirebaseDatabase.getInstance();
        userRef = databaseInstance.getReference().child("USER").child(auth.getCurrentUser().getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                for(Map.Entry<String, Integer> entry : user.getCourseTakingOffsetMap().entrySet()){
                    mapCourseTaking.put(entry.getKey(), entry.getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        // get the list of classes that the user is currently taking
        courseTaking = new ArrayList<>();
        for(String className : mapCourseTaking.keySet()){
            courseTaking.add(className);
        }

        // add the current list of classes to the view
        ArrayAdapter<String> adapterCourseTaking = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, courseTaking);
        lvAddedClasses.setAdapter(adapterCourseTaking);


        //set class selection list view
        final ArrayList<String> arrayClass = new ArrayList<>();
        arrayClass.addAll(Arrays.asList(getResources().getStringArray(R.array.class_array)));
        adapter = new ArrayAdapter<>(AddClassActivity.this,
                android.R.layout.simple_list_item_1,
                arrayClass);
        lvClassesOffered.setAdapter(adapter);

        // add the class when the user clicks
        lvClassesOffered.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (lvClassesOffered.getItemAtPosition(i)).toString();

                /*  if user added a new course, then add it to the view.
                    also add it to user's courseTakingOffsetMap with an offset 0
                 */
                if (!courseTaking.contains(item)) {
                    courseTaking.add(item);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                            android.R.layout.simple_list_item_1, courseTaking);
                    lvAddedClasses.setAdapter(adapter);

                    mapCourseTaking.put(item, 0);
                }
            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Save infomation in the fire base
                userRef.child("courseTakingOffsetMap").setValue(mapCourseTaking);
            }
        });


    }

}
