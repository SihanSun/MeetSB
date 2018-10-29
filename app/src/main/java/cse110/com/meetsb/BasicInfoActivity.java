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

        String userName = userNameInput.getText().toString();
        Toast.makeText(getApplicationContext(), userName, Toast.LENGTH_SHORT).show();
        if (userName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "user name empty", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, AcademicInfoActivity.class);
            intent.putExtra("USERNAME", userName);
            startActivity(intent);
        }
    }
}
