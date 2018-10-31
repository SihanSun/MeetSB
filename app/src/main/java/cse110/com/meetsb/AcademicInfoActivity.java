package cse110.com.meetsb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AcademicInfoActivity extends AppCompatActivity {
    Button continueBtn;
    Spinner majorSpinner;
    EditText gpaEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic_info);

        //set all the buttons
        majorSpinner = (Spinner) findViewById(R.id.academic_info_spinner_major);
        gpaEditText = (EditText) findViewById(R.id.academic_info_editText_GPA);
        continueBtn = (Button) findViewById(R.id.academic_info_button_continue);



        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = getIntent().getStringExtra("USERNAME");

                String major = majorSpinner.getSelectedItem().toString();

                //verify GPA
                String gpaString = gpaEditText.getText().toString();
                if(gpaString == null
                        || gpaString == "") {
                    String gpa = "N/A";
                } else {
                    try {
                        Double.parseDouble(gpaString);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Please enter valid GPA or leave it empty.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // TODO::
                Intent intent = new Intent(AcademicInfoActivity.this, ClassInfoActivity.class);
                intent.putExtra("USERNAME", userName);
                intent.putExtra("MAJOR", major);
                intent.putExtra("GPA", gpaString);
                finish();
                startActivity(intent);
            }
        });
    }
}
