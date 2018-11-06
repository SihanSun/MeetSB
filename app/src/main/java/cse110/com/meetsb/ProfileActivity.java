package cse110.com.meetsb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import cse110.com.meetsb.Model.User;

public class ProfileActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    Button editButton;

    TextView courseTakingTextView;
    TextView userNameTextView;
    TextView gpaTextView;
    TextView graduationYearTextView;
    TextView majorTextView;
    TextView descriptoinTextView;
    TextView genderTextView;

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

        userNameTextView = (TextView) findViewById(R.id.profile_textView_userName);
        gpaTextView = (TextView) findViewById(R.id.profile_textView_GPA);
        majorTextView = (TextView) findViewById(R.id.profile_textView_major);
        descriptoinTextView = (TextView) findViewById(R.id.profile_textView_description);

        getPersonalInfo();
    }

    public void openSetting() {
        startActivity(new Intent(ProfileActivity.this, SettingActivity.class));
    }

    private void getPersonalInfo() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        String userId = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference ref = databaseReference.child("USERTABLE").child(userId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User newUser = dataSnapshot.getValue(User.class);

                userNameTextView.setText(newUser.getPersonalInformation().getUserName());
                gpaTextView.setText(newUser.getAcademicInformation().getGpa());
                majorTextView.setText(newUser.getAcademicInformation().getMajor());
                descriptoinTextView.setText(newUser.getPersonalInformation().getDescription());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        Intent intent = new Intent(this, SwipeActivity.class);
        finish();
        startActivity(intent);
    }
}
