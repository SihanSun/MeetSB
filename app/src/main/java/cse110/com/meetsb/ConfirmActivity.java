package cse110.com.meetsb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ConfirmActivity extends AppCompatActivity {

    Button verifyButton;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        verifyButton = (Button) findViewById(R.id.confirm_button_verify);
        firebaseAuth = FirebaseAuth.getInstance();

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyEmail();
            }
        });

    }

    void verifyEmail() {
        firebaseAuth.getCurrentUser().reload();
        if(firebaseAuth.getCurrentUser().isEmailVerified()) {
            finish();
            startActivity(new Intent(ConfirmActivity.this, SwipeActivity.class));
        } else {
            Toast.makeText(this, "Email not verified!!!+User is " + firebaseAuth.getCurrentUser().getEmail() + "email:" + firebaseAuth.getCurrentUser().isEmailVerified(), Toast.LENGTH_SHORT).show();
        }
    }

    public void openSetting(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }
}
