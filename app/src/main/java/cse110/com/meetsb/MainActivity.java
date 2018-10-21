package cse110.com.meetsb;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<StudentCardMode> al;
    private StudentCardAdapter arrayAdapter;
    private SwipeFlingAdapterView flingContainer;
    private int i;
    private List<List<String>> list;
    private ImageView btnDislike, btnLike, btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();
        for (int i = 0; i < imageUrls.length; i++) {
            List<String> s = new ArrayList<>();
            s.add(imageUrls[i]);
            list.add(s);
        }


        al = new ArrayList<>();
        Student gary = new Student("Gary", 21, list.get(1));
        al.add(new StudentCardMode( gary ));
        al.add(new StudentCardMode("Still Gary", 21, list.get(1)));
        al.add(new StudentCardMode("Another Gary", 21, list.get(1)));
        al.add(new StudentCardMode("Gary Again", 21, list.get(1)));
        al.add(new StudentCardMode("Double Gary", 21, list.get(1)));
        al.add(new StudentCardMode("Professor Gary", 21, list.get(1)));
        al.add(new StudentCardMode("Gary Gary", 21, list.get(1)));
        al.add(new StudentCardMode("Gary 教授", 21, list.get(1)));

        arrayAdapter = new StudentCardAdapter(this, R.layout.item, R.id.buddyName, al);

        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                al.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                makeToast(MainActivity.this, "Dislike Gary! How dare are you!");
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                makeToast(MainActivity.this, "You made one of the best decision in your lifetime ;)");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                al.add(new StudentCardMode("No More Gary", 21, list.get(itemsInAdapter % imageUrls.length - 1)));
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                try {
                    View view = flingContainer.getSelectedView();
                    view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                    view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "You Clicked Gary!!", Toast.LENGTH_SHORT).show();
            }
        });

        btnDislike = (ImageView) findViewById(R.id.left);
        btnLike = (ImageView) findViewById(R.id.right);
        btnProfile  = (ImageView) findViewById(R.id.info);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeToast(MainActivity.this, "Professionalism Time!");
            }
        });
        btnDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeToast(MainActivity.this, "Dislike Gary! How dare are you!");
            }
        });
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeToast(MainActivity.this, "You made one of the best decision in your lifetime ;)");
            }
        });

    }
    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    public void right() {
        flingContainer.getTopCardListener().selectRight();
    }

    public void left() {
        flingContainer.getTopCardListener().selectLeft();
    }

    public final String[] imageUrls = new String[]{
            "http://i.imgur.com/P7Z6Ogv.png",
            "http://i.imgur.com/P7Z6Ogv.png"
    };
}