package cse110.com.meetsb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cse110.com.meetsb.Model.User;

public class ProfileActivity extends AppCompatActivity {
<<<<<<< HEAD

    FirebaseAuth firebaseAuth;
=======
    Button editButton;
>>>>>>> master
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editButton = findViewById(R.id.profile_button_backToSetting);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSetting();
            }
        });
    }

    public void openSetting() {
        startActivity(new Intent(ProfileActivity.this, SettingActivity.class));
    }

    private void getPersonalInfo() {
        User newUser = new User();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        String userId = firebaseAuth.getCurrentUser().getUid();
        databaseReference.child("USERTABLE").child(userId);

        Intent intent = new Intent(this, SwipeActivity.class);
        finish();
        startActivity(intent);
    }
}
