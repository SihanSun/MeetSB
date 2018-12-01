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

import org.w3c.dom.Text;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class SignupActivity extends AppCompatActivity {


    public static final int MIN_LENGTH_PASSWORD = 8;

    private Button registerButton;

    private TextView returnToLogin;

    private DatabaseReference firebaseReference;

    private FirebaseAuth firebaseAuth;

    private TextView emailAddress;

    private EditText passwordEntered;

    private EditText passwordConfirmed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        registerButton = (Button) findViewById(R.id.signUp_button_signUp);
        returnToLogin = (TextView) findViewById(R.id.signUp_textView_returnToSignIn);
        //firebaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        emailAddress = (TextView) findViewById(R.id.signUp_editText_email);
        passwordEntered = (EditText) findViewById(R.id.signUp_editText_password);
        //passwordConfirmed = (EditText) findViewById(R.id.signUp_editText_passwordConfirm);

        //TODO
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUserWithEmail();
            }
        });

        returnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(SignupActivity.this, SigninActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
    }


    private void registerUserWithEmail() {
        String email = emailAddress.getText().toString().trim();
        String password = passwordEntered.getText().toString().trim();

        if (!isValid(email, password)) {
            return;
        }

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
                                        progressDialog.dismiss();
                                        finish();
                                        startActivity(new Intent(SignupActivity.this, ConfirmActivity.class));
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
    private boolean isValid(String email, String password) {

        if(TextUtils.isEmpty(email)) {
            //email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();

            return false;
        }
        if(TextUtils.isEmpty(password)) {
            //password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();

            return false;
        }

        // 检查邮箱是不是valid
        String pattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}" +
                "[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(email);
        if (!m.find()) {
            Toast.makeText(this, email +
                    " is not a valid email address", Toast.LENGTH_LONG).show();

            return false;
        }

        // 检查是不是ucsd email
        pattern = "@(\\w)+((\\.\\w+)+)$";
        r = Pattern.compile(pattern);
        m = r.matcher(email);
        if (!m.find() || !m.group(0).equals("@ucsd.edu")) {
            Toast.makeText(this, email +
                    " is not a valid UCSD email address", Toast.LENGTH_LONG).show();
            return false;
        }

        // 检查密码是不是valid
        // 密码要求： 八个字符
        if (password.length() < MIN_LENGTH_PASSWORD) {
            Toast.makeText(this, "Password needs to be at least " +
                    MIN_LENGTH_PASSWORD + " digits", Toast.LENGTH_LONG).show();

            return false;
        }


//        //以下密码要求太严格，不采用
//        //密码要求：至少八个字符，至少一个字母和一个数字
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

}
