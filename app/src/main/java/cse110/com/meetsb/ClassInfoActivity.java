package cse110.com.meetsb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cse110.com.meetsb.Model.User;


public class ClassInfoActivity extends AppCompatActivity {
    Button addClass;
    Button submit;

    String userName;
    String gpaString;
    String major;
    ArrayList<String> course = new ArrayList<>();
    List<String> courseTaking = new ArrayList<>();

    FirebaseAuth firebaseAuth;

    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    ArrayList<String> selectClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_info);

        ListView lv = (ListView)findViewById(R.id.class_info_listview_classlist);
        final ArrayList<String> arrayClass = new ArrayList<>();
        selectClass = new ArrayList<>();
        arrayClass.addAll(Arrays.asList(getResources().getStringArray(R.array.class_array)));
        adapter = new ArrayAdapter<>(ClassInfoActivity.this,
                android.R.layout.simple_list_item_1,
                arrayClass);

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
                if(!selectClass.contains(arrayClass.get(i)))
                    selectClass.add(arrayClass.get(i));
            }
        });

        ListView sl = (ListView)findViewById(R.id.class_info_listview_selectlist);
        adapter2 = new ArrayAdapter<>(ClassInfoActivity.this,
                android.R.layout.simple_list_item_1,selectClass);
        sl.setAdapter(adapter2);

        //get extras
        userName = getIntent().getStringExtra("USERNAME");
        gpaString = getIntent().getStringExtra("GPA");
        major = getIntent().getStringExtra("MAJOR");

        //set button
        submit = (Button) findViewById(R.id.class_info_button_submit) ;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitClassInfo();
            }
        });

        //set firebase
        firebaseAuth = FirebaseAuth.getInstance();

        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this,
                R.array.class_array,
                R.layout.activity_class_info);

    }

    private void submitClassInfo() {
        User newUser = new User();
        newUser.setUserName(userName);
        newUser.setDescription("I am teacher tony");
        newUser.setGender("undecided");
        //newUser.getAcademicInformation().getCourseTaking().add("CSE110");
        newUser.setGpa(gpaString);
        newUser.setMajor(major);
        String toast= "";
        for(String className : selectClass) {
            toast += className + " ";
        }
        Toast.makeText(this, toast,Toast.LENGTH_SHORT).show();



        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        String userId = firebaseAuth.getCurrentUser().getUid();
        databaseReference.child("USERTABLE").child(userId).setValue(newUser);

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
