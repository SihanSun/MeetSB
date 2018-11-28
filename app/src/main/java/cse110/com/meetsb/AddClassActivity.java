package cse110.com.meetsb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddClassActivity extends AppCompatActivity {

    Button submit;

    private FirebaseDatabase databaseInstance;
    private DatabaseReference databaseRef;
    FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        auth = FirebaseAuth.getInstance();
        databaseInstance = FirebaseDatabase.getInstance();
        databaseRef = databaseInstance.getReference().child("USER");

        submit = findViewById(R.id.class_info_button_submit);

    }
}
