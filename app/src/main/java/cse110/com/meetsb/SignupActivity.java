package cse110.com.meetsb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {


    private Button registerButton;

    private Button returnToLoginButton;

    private DatabaseReference firebaseReference;

    private FirebaseAuth firebaseAuth;

    private AutoCompleteTextView emailAddress;

    private EditText passwordEntered;

    private EditText passwordConfirmed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        registerButton = (Button) findViewById(R.id.signUp_button_signUp);
        returnToLoginButton = (Button) findViewById(R.id.signUp_button_returnToLogin);
        //firebaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        emailAddress = (AutoCompleteTextView) findViewById(R.id.signUp_textView_emailAddress);
        passwordEntered = (EditText) findViewById(R.id.signUp_editText_password);
        passwordConfirmed = (EditText) findViewById(R.id.signUp_editText_passwordConfirm);

        //TODO
        //if(firebaseAuth.getCurrentUser() != null) {

        //}
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUserWithEmail();
            }
        });

        returnToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, SigninActivity.class));
            }
        });

    }


    private void registerUserWithEmail() {
        String email = emailAddress.getText().toString().trim();
        String password = passwordEntered.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            //email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();

            return;
        }
        if(TextUtils.isEmpty(password)) {
            //password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();

            return;
        }
        //检查ucsd 邮箱
        //TODO


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Working on it...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            //start the profile activity
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignupActivity.this,
                                                "安排上了",
                                                Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        finish();
                                        startActivity(new Intent(SignupActivity.this, ConfirmActivity.class));
                                    } else {
                                        Toast.makeText(SignupActivity.this,
                                                task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                        firebaseAuth.getCurrentUser().delete();
                                        progressDialog.dismiss();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(SignupActivity.this, "Failed to send the email",Toast.LENGTH_SHORT).show();
                            //firebaseAuth.getCurrentUser().delete();
                            progressDialog.dismiss();
                        }
                    }
                });

    }
}
