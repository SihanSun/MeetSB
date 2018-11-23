package cse110.com.meetsb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class AcademicInfoActivity extends AppCompatActivity {
    Button continueBtn;
    Spinner majorSpinner;
    EditText gpaEditText;

    String userName = null;
    String gender = null;
    String description = null;
    String filePathStr = null;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic_info);

        //set all the buttons
        majorSpinner = (Spinner) findViewById(R.id.academic_info_spinner_major);
        gpaEditText = (EditText) findViewById(R.id.academic_info_editText_GPA);
        continueBtn = (Button) findViewById(R.id.academic_info_button_continue);

        //get all the intent extras;
        userName = getIntent().getStringExtra("USERNAME");
        gender = getIntent().getStringExtra("GENDER");
        description = getIntent().getStringExtra("DESCRIPTION");
        filePathStr = getIntent().getStringExtra("IMAGE");


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               submitInfo();
            }
        });
    }

    void submitInfo() {
        String major = majorSpinner.getSelectedItem().toString();

        //verify GPA
        String gpaString = gpaEditText.getText().toString();
        if(!gpaString.isEmpty()) {
            double gpa;
            try {
                gpa = Double.parseDouble(gpaString);
            } catch (Exception e) {
                Toast.makeText(this, "Please enter valid GPA", Toast.LENGTH_SHORT).show();
                return;
            }
            if(gpa < 0 || gpa > 4) {
                Toast.makeText(this, "Please enter valid GPA", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            gpaString = "Not available";
        }


        Intent intent = new Intent(AcademicInfoActivity.this, ClassInfoActivity.class);
        intent.putExtra("USERNAME", userName);
        intent.putExtra("GENDER",gender);
        intent.putExtra("DESCRIPTION",description);
        intent.putExtra("IMAGE", filePathStr);
        intent.putExtra("MAJOR", major);
        intent.putExtra("GPA", gpaString);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
