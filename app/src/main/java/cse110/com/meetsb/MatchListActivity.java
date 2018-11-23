package cse110.com.meetsb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MatchListActivity extends AppCompatActivity {

    int[] IMAGES = {R.drawable.profilepic, R.drawable.profilepic};
    String[] NAMES = {"Chen Zhongyu", "Lychee"};
    String[] Messages = {"Hello", "Wanna get laid"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);
        ListView lv = (ListView) findViewById(R.id.match_list_listView_friends);
        CustomAdapter customAdapter = new CustomAdapter();
        lv.setAdapter(customAdapter);


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return IMAGES.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup vewGroup) {
            view = getLayoutInflater().inflate(R.layout.match_item,null);
            ImageView imageView = (ImageView)view.findViewById(R.id.imageView2);
            TextView textView_name = (TextView)view.findViewById(R.id.textView2);
            TextView textView_message = (TextView)view.findViewById(R.id.textView3);

            imageView.setImageResource(IMAGES[i]);
            textView_name.setText(NAMES[i]);
            textView_message.setText(Messages[i]);
            return view;
        }

    }
}

