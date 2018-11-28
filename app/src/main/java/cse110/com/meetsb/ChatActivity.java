package cse110.com.meetsb;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.net.InternetDomainName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cse110.com.meetsb.Model.Chat;
import cse110.com.meetsb.Model.Message;
import cse110.com.meetsb.Model.MessageAdapter;
import cse110.com.meetsb.Model.MessageType;
import cse110.com.meetsb.Model.TextType;
import cse110.com.meetsb.Model.User;


public class ChatActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView recyclerView;
    EditText editText;
    LinearLayoutManager linearLayoutManager;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutCompat linearLayoutCompat;

    private static final int showItem = 5;
    int position = 0;
    boolean moreMessage = true;
    User user;
    Chat chat;
    private Uri myImage;
    private Uri yourImage;
    private MessageType me = MessageType.me;
    private MessageType you = MessageType.you;
    private TextType text = TextType.text;
    private TextType image = TextType.image;
    private ImageButton addLocation;

    String otherUID;
    ProgressDialog progressDialog;
    private static String lastMessage = "";
    private MessageAdapter messageAdapter;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    FusedLocationProviderClient fusedLocationProviderClient;
    private String imageURL;
    private LinkedList<Message> messageList = new LinkedList<Message>();
    private LinkedList<String> messageId = new LinkedList<String>();

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
    }

    private void loadMoreData(){
        String userId = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("MESSAGE").child(userId).child(otherUID);
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (messageId.indexOf(dataSnapshot.getKey()) == -1) {
                    if(lastMessage.equals(dataSnapshot.getKey())){
                        moreMessage = false;
                        lastMessage = "";
                        messageAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    else{
                        Message message = dataSnapshot.getValue(Message.class);
                        messageId.add(position, dataSnapshot.getKey());
                        messageList.add(position, message);
                        moreMessage = false;
                        if(position == 1){
                            lastMessage = dataSnapshot.getKey();
                        }
                        messageAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
                if(!moreMessage){
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        Query query = databaseReference.orderByKey().endAt(lastMessage).limitToLast(showItem);
        query.addChildEventListener(childEventListener);
        if(moreMessage){
            Handler handler = new Handler();
            Runnable runnable = new Runnable(){

                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getApplicationContext(),"OK", Toast.LENGTH_SHORT).show();
                }
            };
            handler.postDelayed(runnable, 2000);
        }
    }

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerView = (RecyclerView) findViewById(R.id.messages_view);
        editText = (EditText) findViewById(R.id.footer_bar).findViewById(R.id.editText);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setRefreshing(true);

        linearLayoutCompat = (LinearLayoutCompat) findViewById(R.id.tool_bar);
        addLocation = (ImageButton) findViewById(R.id.add_btn);
        addLocation.setOnClickListener(this);

        //get other user UID
        otherUID = getIntent().getStringExtra("UID");

        linearLayoutManager = new LinearLayoutManager(this);
        messageAdapter = new MessageAdapter(this, messageList);
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.scrollToPosition(messageList.size() - 1);

        firebaseAuth = FirebaseAuth.getInstance();
        String currentUserUID = firebaseAuth.getCurrentUser().getUid();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        if(saveInstanceState == null){
            Intent intent = this.getIntent();
            Bundle bundle = intent.getExtras();
            chat = (Chat) bundle.getSerializable("chat");
            user = new User();
            String userId = chat.getUserId();


            storageReference.child("IMAGE").child(currentUserUID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    myImage = uri;
                }
            });
            storageReference.child("IMAGE").child(otherUID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    yourImage = uri;
                }
            });
        }
        loadData();
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(editText.getText() != null && !editText.getText().toString().trim().equals("")){
                    SendMessage(view);
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                position = 0;
                loadMoreData();
            }
        });
    }

    private void loadData() {
        firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("MESSAGE").child(userId).child(otherUID);
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                messageId.add(dataSnapshot.getKey());
                Message message = dataSnapshot.getValue(Message.class);
                messageList.add(message);
                position ++;
                if(position == 1){
                    lastMessage = dataSnapshot.getKey();
                }
                messageAdapter.notifyItemInserted(messageList.size() -1);
                moreMessage = false;
                recyclerView.scrollToPosition(messageList.size() -1);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        Query query = databaseReference.orderByKey().limitToLast(showItem);
        query.addChildEventListener(childEventListener);
        if(moreMessage){
            Handler handler = new Handler();
            Runnable runnable = new Runnable(){

                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                }
            };
            handler.postDelayed(runnable, 2000);
        }
    }

    private void SendMessage(View view){
        firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();

        //push my message into my message field
        databaseReference = FirebaseDatabase.getInstance().getReference().child("MESSAGE").child(userId).child(otherUID);
        Message Mymessage = new Message(userId, userId, editText.getText().toString(), ServerValue.TIMESTAMP, false, me, text, myImage.toString());
        String Mykey = databaseReference.push().getKey();
        databaseReference.child(Mykey).setValue(Mymessage);

        //push my message into your message field
        databaseReference = FirebaseDatabase.getInstance().getReference().child("MESSAGE").child(otherUID).child(userId);
        Message Yourmessage = new Message(otherUID, otherUID, editText.getText().toString(), ServerValue.TIMESTAMP, false, you, text, myImage.toString());
        String Yourkey = databaseReference.push().getKey();
        databaseReference.child(Yourkey).setValue(Yourmessage);

        editText.setText("");
        recyclerView.scrollToPosition(messageList.size() - 1);
        Toast.makeText(this, "Send successfully", Toast.LENGTH_SHORT);
    }

    public void sendImage(Uri imageURL, Bitmap bitmap) {
        firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();

        //push my message into my message field
        databaseReference = FirebaseDatabase.getInstance().getReference().child("MESSAGE").child(userId).child(otherUID);
        Message Mymessage = new Message(userId, userId, imageURL.toString(), ServerValue.TIMESTAMP, false, me, image, myImage.toString());
        String Mykey = databaseReference.push().getKey();
        databaseReference.child(Mykey).setValue(Mymessage);

        //push my message into your message field
        databaseReference = FirebaseDatabase.getInstance().getReference().child("MESSAGE").child(otherUID).child(userId);
        Message Yourmessage = new Message(otherUID, otherUID, imageURL.toString(), ServerValue.TIMESTAMP, false, you, image, myImage.toString());
        String Yourkey = databaseReference.push().getKey();
        databaseReference.child(Yourkey).setValue(Yourmessage);

        recyclerView.scrollToPosition(messageList.size() - 1);


        storageReference = FirebaseStorage.getInstance().getReference().child("IMAGE").child(Mykey);
        if(imageURL != null) {
            UploadTask uploadTask = storageReference.putFile(imageURL);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(ChatActivity.this, "Upload Successfully -> " , Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ChatActivity.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                    return;
                }
            });
        }

        Toast.makeText(this, "Send Image successfully", Toast.LENGTH_SHORT);
    }

    public void btnAddImage(View view){
        linearLayoutCompat.setVisibility(View.GONE);
        Intent image = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(image, 2);
    }

    public void btnAddLocation(View view){
        linearLayoutCompat.setVisibility(View.GONE);
        //Toast.makeText(this, "Location", Toast.LENGTH_SHORT).show();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
        else sendLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) sendLocation();
                else Toast.makeText(this, "You need to give permission to share location", Toast.LENGTH_SHORT).show();
        }
    }


    private void sendLocation() {
        Toast.makeText(this, "sendLocation", Toast.LENGTH_SHORT).show();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.

                            // Logic to handle location object
                            if(location != null){
                                String lat = Double.toString(location.getLatitude());
                                String lng = Double.toString(location.getLongitude());
                                String url = "http://maps.google.com/maps/api/staticmap?center=" + lat + "," + lng + "&zoom=15&size=200x200&sensor=false";

                                firebaseAuth = FirebaseAuth.getInstance();
                                String userId = firebaseAuth.getCurrentUser().getUid();

                                databaseReference = FirebaseDatabase.getInstance().getReference().child("MESSAGE").child(userId).child(otherUID);
                                Message Mymessage = new Message(userId, userId, url, ServerValue.TIMESTAMP, false, me, text, myImage.toString());
                                String Mykey = databaseReference.push().getKey();
                                databaseReference.child(Mykey).setValue(Mymessage);

                                //push my message into your message field
                                databaseReference = FirebaseDatabase.getInstance().getReference().child("MESSAGE").child(otherUID).child(userId);
                                Message Yourmessage = new Message(otherUID, otherUID, url, ServerValue.TIMESTAMP, false, you, text, myImage.toString());
                                String Yourkey = databaseReference.push().getKey();
                                databaseReference.child(Yourkey).setValue(Yourmessage);

                            }
                            else {
                                Toast.makeText(ChatActivity.this, "Failed to share location. Please check if Google Play Services can access your location", Toast.LENGTH_LONG).show();
                                String url = "http://maps.google.com";

                                firebaseAuth = FirebaseAuth.getInstance();
                                String userId = firebaseAuth.getCurrentUser().getUid();

                                databaseReference = FirebaseDatabase.getInstance().getReference().child("MESSAGE").child(userId).child(otherUID);
                                Message Mymessage = new Message(userId, userId, url, ServerValue.TIMESTAMP, false, me, text, myImage.toString());
                                String Mykey = databaseReference.push().getKey();
                                databaseReference.child(Mykey).setValue(Mymessage);

                                //push my message into your message field
                                databaseReference = FirebaseDatabase.getInstance().getReference().child("MESSAGE").child(otherUID).child(userId);
                                Message Yourmessage = new Message(otherUID, otherUID, url, ServerValue.TIMESTAMP, false, you, text, myImage.toString());
                                String Yourkey = databaseReference.push().getKey();
                                databaseReference.child(Yourkey).setValue(Yourmessage);
                            }
                        }
                    });
        }
        catch (SecurityException ex) {}

    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.add_btn){
            if(linearLayoutCompat.getVisibility() == View.GONE){
                linearLayoutCompat.setVisibility(View.VISIBLE);
                addLocation.setImageResource(R.drawable.ic_cancel);
            }
            else if(linearLayoutCompat.getVisibility() == View.VISIBLE){
                linearLayoutCompat.setVisibility(View.GONE);
                addLocation.setImageResource(R.drawable.ic_add_black);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null) {
            if (requestCode == 2) {
                try {
                    Uri imageUri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                    //convert bitmap to string
                    ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
                    sendImage(imageUri, bitmap);

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            Toast.makeText(this, "No connection available", Toast.LENGTH_LONG).show();
        }
    }
}
