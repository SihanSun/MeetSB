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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SigninActivity extends AppCompatActivity {

    EditText userEmailAddress;

    EditText userPassword;

    Button signInButton;

    TextView returnToSignUp;

    TextView passwordRecovery;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);


        //set all the buttons, editText, and textView
        userEmailAddress = (EditText) findViewById(R.id.signIn_editText_emailAdress);
        userPassword = (EditText) findViewById(R.id.signIn_editText_password);
        signInButton = (Button) findViewById(R.id.signIn_button_signIn);
        returnToSignUp = (TextView) findViewById(R.id.signIn_textView_returnToSignUp);
        passwordRecovery = (TextView) findViewById(R.id.signIn_textView_passwordRecovery);
        firebaseAuth = FirebaseAuth.getInstance();

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
        passwordRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPasswordRecovery();
            }
        });

//        //check if the user has already
//        if(firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()) {
//            jumpToSwipePage();
//        }
//

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void userSignIn() {

        if(firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()) {
            jumpToSwipePage();
        }

        //check user email or password
        String email = userEmailAddress.getText().toString();
        String password = userPassword.getText().toString();
        if (!isValid(email, password)) {
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email.trim()
        ,password.trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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

    private void setPasswordRecovery() {
        String email = userEmailAddress.getText().toString();
        if (email.isEmpty()) {
            Toast.makeText(SigninActivity.this,
                    "Please enter your email",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SigninActivity.this,
                                    "Check your email and follow instruction",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean isValid(String email, String password) {

        if (email.isEmpty()) {
            Toast.makeText(this,
                    "Please enter your email",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(this,
                    "Please enter your password",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        // 检查邮箱是不是valid
        String pattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}" +
                "[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(email);
        if (!m.find()) {
            Toast.makeText(this, "Not valid email", Toast.LENGTH_LONG).show();

            return false;
        }

        // 检查是不是ucsd email
        pattern = "@(\\w)+((\\.\\w+)+)$";
        r = Pattern.compile(pattern);
        m = r.matcher(email);
        if (!m.find() || !m.group(0).equals("@ucsd.edu")) {
            Toast.makeText(this, m.group(0) +
                    " is not a valid UCSD email", Toast.LENGTH_LONG).show();
            return false;
        }

        // 检查密码是不是valid
        // 至少八个字符，至少一个字母和一个数字
//        pattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
//        r = Pattern.compile(pattern);
//        m = r.matcher(password);
//        if (!m.find()) {
//            Toast.makeText(this, "Not valid password", Toast.LENGTH_LONG).show();
//
//            return false;
//        }

        return true;
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
        //finish();
        startActivity(new Intent(this, SignupActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
