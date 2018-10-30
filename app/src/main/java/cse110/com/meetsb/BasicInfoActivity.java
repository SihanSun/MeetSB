package cse110.com.meetsb;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class BasicInfoActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;
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

        ImageView avatar = (ImageView) findViewById(R.id.basic_info_imageView_avatar);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.basic_info_imageView_avatar);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }

    }
}
