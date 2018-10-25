package cse110.com.meetsb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.support.v7.widget.SearchView;

import java.util.ArrayList;
import java.util.Arrays;


public class ClassInfoActivity extends AppCompatActivity {
    Button addClass;

    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_info);
        ListView lv = (ListView)findViewById(R.id.class_info_listview_classlist);
        ArrayList<String> arrayClass = new ArrayList<>();
        arrayClass.addAll(Arrays.asList(getResources().getStringArray(R.array.class_array)));
        adapter = new ArrayAdapter<>(ClassInfoActivity.this,
                android.R.layout.simple_list_item_1,
                arrayClass);
        lv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.search_class);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
