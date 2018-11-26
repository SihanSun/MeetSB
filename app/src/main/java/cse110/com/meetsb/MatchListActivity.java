package cse110.com.meetsb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cse110.com.meetsb.Model.Chat;
import cse110.com.meetsb.Model.UserCardMode;
import cse110.com.meetsb.Model.UserSwipe;
import io.grpc.Server;

public class MatchListActivity extends AppCompatActivity {
    List<Uri> imageList;
    List<String> nameList;
    List<String> messageList;
    String[] NAMES = {"Chen Zhongyu", "Lychee"};
    String[] Messages = {"Hello", "Wanna get laid"};

    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;

    //ListView
    ListView lv;

    //uid
    String currentUserUID;

    //matchList
    List<String> matchList;

    //Progress Dialog
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);

        //User List
        imageList = new ArrayList<>();
        nameList = new ArrayList<>();
        messageList = new ArrayList<>();

        //set the listView
        lv = (ListView) findViewById(R.id.match_list_listView_friends);
        final CustomAdapter customAdapter = new CustomAdapter();
        lv.setAdapter(customAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Chat chat = new Chat(matchList.get(i), "hi", Long.toString(System.currentTimeMillis()),nameList.get(i));
                Bundle bundle = new Bundle();
                bundle.putSerializable("chat", chat);
                Intent intent = new Intent(MatchListActivity.this, ChatActivity.class);
                intent.putExtra("UID", matchList.get(i));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //set firebase
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        //set UID
        currentUserUID = firebaseAuth.getCurrentUser().getUid();

        //set matchList
        matchList = new ArrayList<>();

        //get the matchList
        databaseReference.child("USERSWIPE").child(currentUserUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserSwipe userSwipe = dataSnapshot.getValue(UserSwipe.class);
                HashMap<String, String> hashMap = userSwipe.getMatchList();
                if(hashMap != null) {
                    // populate matchList
                    for(String key : hashMap.keySet()) {
                        matchList.add(hashMap.get(key));
                    }
                }

                //go over each item in the match list and add it to the list view
                for(final String otherUID : matchList) {

                    //get the image
                    storageReference.child("IMAGE").child(otherUID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageList.add(uri);

                            //get the name
                            databaseReference.child("USER").child(otherUID).child("userName").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String otherUserName = dataSnapshot.getValue(String.class);
                                    nameList.add(otherUserName);

                                    //get the message
                                    //TODO
                                    customAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return imageList.size();
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
            //imageView.setImageURI(imageList.get(i));
            Glide.with(getApplicationContext()).load(imageList.get(i).toString()).into(imageView);
            textView_name.setText(nameList.get(i));
            textView_message.setText("hello from the other side");
            return view;
        }
    }

    @GlideModule
    public class MyAppGlideModule extends AppGlideModule {

        @Override
        public void registerComponents(Context context, Glide glide, Registry registry) {
            // Register FirebaseImageLoader to handle StorageReference
            registry.append(StorageReference.class, InputStream.class,
                    new FirebaseImageLoader.Factory());
        }
    }
}

