package cse110.com.meetsb;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SigninActivity extends AppCompatActivity {

    EditText userEmailAddress;

    EditText userPassword;

    Button signInButton;

    TextView returnToSignUp;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        //set all the buttons, editText, and textView
        userEmailAddress = (EditText) findViewById(R.id.signIn_editText_password);
        userPassword = (EditText) findViewById(R.id.signIn_editText_password);
        signInButton = (Button) findViewById(R.id.signIn_button_signIn);
        returnToSignUp = (TextView) findViewById(R.id.signIn_textView_returnToSignUp);
        firebaseAuth = FirebaseAuth.getInstance();

        Toast.makeText(this, "In signin activity",Toast.LENGTH_SHORT).show();
        //check if the user has already logged in
//        if(firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()) {
//            Toast.makeText(this, "current user is " + firebaseAuth.getCurrentUser().getEmail(),Toast.LENGTH_SHORT).show();
//            jumpToSwipePage();
//        }

        //set button listner
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSignIn();
            }
        });

        returnToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToSignUpPage();
            }
        });
    }

    private void userSignIn() {

        //TODO
        //check user email or password
        Toast.makeText(this,"user email is " + userEmailAddress.getText(),Toast.LENGTH_SHORT).show();

        firebaseAuth.signInWithEmailAndPassword(userEmailAddress.getText().toString().trim()
        ,userPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    if(firebaseAuth.getCurrentUser().isEmailVerified()) {
                        jumpToSwipePage();
                    } else {
                        Toast.makeText(SigninActivity.this,
                                "Please verify your email first",
                                Toast.LENGTH_SHORT).show();
                        jumpToConfirmPage();
                    }
                } else {
                    Toast.makeText(SigninActivity.this,
                            task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void jumpToSwipePage() {
        finish();
        startActivity(new Intent(this, SwipeActivity.class));
    }

    private void jumpToConfirmPage() {
        finish();
        startActivity(new Intent(this, ConfirmActivity.class));
    }

    private void jumpToSignUpPage() {
        finish();
        startActivity(new Intent(this, SignupActivity.class));
    }
}
