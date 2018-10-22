package cse110.com.meetsb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import cse110.com.meetsb.database.firebaseSDK.AccountHandler;

public class MainActivity extends AppCompatActivity {

    private Button buttonRegister;
    private DatabaseReference mDatabase;

    private EditText editTextEmail;
    private EditText editTextPassword;

    private TextView textViewSignIn;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set all the views and button
        buttonRegister = (Button) findViewById(R.id.registerButton);
        editTextEmail = (EditText) findViewById(R.id.emailField);
        editTextPassword = (EditText) findViewById(R.id.passwordField);
        textViewSignIn = (TextView) findViewById(R.id.textViewSignIn);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null ){
            //profile activity
            finish();
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        }

        progressDialog = new ProgressDialog(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //register
                registerUser();
            }
        });

        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sign in
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });


//        mFirebaseBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String name = mNameField.getText().toString().trim();
//                String email = mEmailField.getText().toString().trim();
//
//                HashMap<String, String> dataMap = new HashMap<String, String>();
//                dataMap.put("Name", name);
//                dataMap.put("Email", email);
//                mFirebaseBtn.setText("clicked");
//                mDatabase.push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()) {
//                            Toast.makeText(MainActivity.this, "Stored...", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//            }
//        });

    }

    private void registerUserWithEmail() {
        String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

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
                                Toast.makeText(MainActivity.this,
                                        "安排上了",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            } else {
                                Toast.makeText(MainActivity.this,
                                        task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                firebaseAuth.getCurrentUser().delete();
                            }
                        }
                    });

                } else {
                    Toast.makeText(MainActivity.this, "Failed to send the email",Toast.LENGTH_SHORT).show();
                    firebaseAuth.getCurrentUser().delete();
                }
            }
        });

    }
}
