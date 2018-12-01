package cse110.com.meetsb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ConfirmActivity extends AppCompatActivity {

    Button verifyButton;
    Button resendButton;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        verifyButton = (Button) findViewById(R.id.confirm_button_verify);
        resendButton = (Button) findViewById(R.id.confirm_button_resend);

        //progress dialog
        progressDialog = new ProgressDialog(this);

        //set firebase
        firebaseAuth = FirebaseAuth.getInstance();

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyEmail();
            }
        });
        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ConfirmActivity.this, "Resend success. Please check your email", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    void verifyEmail() {
        firebaseAuth.getCurrentUser().reload().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if(firebaseAuth.getCurrentUser().isEmailVerified()) {
                    finish();
                    startActivity(new Intent(ConfirmActivity.this, BasicInfoActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    Toast.makeText(ConfirmActivity.this, "Please verify your email first!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
