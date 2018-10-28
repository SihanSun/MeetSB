package cse110.com.meetsb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BasicInfoActivity extends AppCompatActivity {
    EditText userNameInput;
    EditText passwordInput;
    EditText passwordConfirm;
    Button continueBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info);

        continueBtn = (Button) findViewById(R.id.basic_info_button_continue);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitInfo();
            }
        });
    }

    public void submitInfo() {
        userNameInput = (EditText) findViewById(R.id.basic_info_editText_username);
        passwordInput = (EditText) findViewById(R.id.basic_info_editText_password);
        passwordConfirm = (EditText) findViewById(R.id.basic_info_editText_passwordCofirm);

        String userName = userNameInput.getText().toString();
        String password = passwordInput.getText().toString();
        String password_2 = passwordConfirm.getText().toString();
        Toast.makeText(getApplicationContext(), userName, Toast.LENGTH_SHORT).show();
        if (userName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "user name empty", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty() || password_2.isEmpty()) {
            Toast.makeText(getApplicationContext(), "password empty", Toast.LENGTH_SHORT).show();

        } else if (!password.equals(password_2)) {
            Toast.makeText(getApplicationContext(), "password must match", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, AcademicInfoActivity.class);
            intent.putExtra("USERNAME", userName);
            intent.putExtra("PASSWORD", password);
            startActivity(intent);
        }
    }
}
