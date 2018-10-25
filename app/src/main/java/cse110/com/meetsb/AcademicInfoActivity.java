package cse110.com.meetsb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AcademicInfoActivity extends AppCompatActivity {
    Button continueBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic_info);
        continueBtn = (Button) findViewById(R.id.academic_info_button_continue);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO::
                finish();
                startActivity(new Intent(AcademicInfoActivity.this, ClassInfoActivity.class));
            }
        });
    }
}
