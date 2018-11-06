package cse110.com.meetsb;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cse110.com.meetsb.Model.User;

public class OtherUserActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_other_user);

        editButton = findViewById(R.id.profile_button_backToSetting);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSetting();
            }
        });

        userNameTextView = (TextView) findViewById(R.id.other_user_textView_name);
        gpaTextView = (TextView) findViewById(R.id.other_user_textView_gpa);
        majorTextView = (TextView) findViewById(R.id.other_user_textView_major);
        descriptoinTextView = (TextView) findViewById(R.id.other_user_textView_description);

        getOtherInfo();
    }

    public void openSetting() {
        startActivity(new Intent(OtherUserActivity.this, OtherUserActivity.class));
    }
    private void getOtherInfo() {
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
