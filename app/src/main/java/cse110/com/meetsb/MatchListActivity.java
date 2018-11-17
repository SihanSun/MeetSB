package cse110.com.meetsb;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cse110.com.meetsb.Model.UserSwipe;

public class MatchListActivity extends AppCompatActivity {

    int[] images = {};
    String[] names = {};
    String[] messages = {"adfadfa", "adfadfadf"};

    FirebaseAuth firebaseAuth;
    private DatabaseReference swipeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);
        ListView lv = (ListView) findViewById(R.id.match_list_listView_friends);
        // get User attributes
        //String UID = firebaseAuth.getCurrentUser().getUid();
        String UID = "3ozZdan648eBY5k4kAf8I1zrbkE3";
        swipeRef = FirebaseDatabase.getInstance().getReference().child("USERSWIPE").child(UID);
        swipeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserSwipe userSwipe = dataSnapshot.getValue(UserSwipe.class);
                List<String> matchList = userSwipe.getMatchList();
                for (String ID : matchList) {
                    Toast.makeText(getApplicationContext(), ID, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // populate the list
        CustomAdapter customAdapter = new CustomAdapter();
        lv.setAdapter(customAdapter);
    }



    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return images.length;
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

            imageView.setImageResource(images[i]);
            textView_name.setText(names[i]);
            textView_message.setText(messages[i]);
            return view;
        }
    }
}

