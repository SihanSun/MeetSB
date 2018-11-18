package cse110.com.meetsb;


import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;

import java.util.LinkedList;

import cse110.com.meetsb.Model.Chat;
import cse110.com.meetsb.Model.Message;
import cse110.com.meetsb.Model.MessageAdapter;
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

    private static String lastMessage = "";
    private MessageAdapter messageAdapter;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private LinkedList<Message> messageList = new LinkedList<Message>();
    private LinkedList<String> messageId = new LinkedList<String>();

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
    }

    private void loadMoreData(){
        firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("message").child(userId);
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
        recyclerView = (RecyclerView) findViewById(R.id.message_view);
        editText = (EditText) findViewById(R.id.footer_bar).findViewById(R.id.editText);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setRefreshing(true);

        linearLayoutManager = new LinearLayoutManager(this);
        messageAdapter = new MessageAdapter(this, messageList);
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.scrollToPosition(messageList.size() - 1);

        if(saveInstanceState == null){
            Intent intent = this.getIntent();
            Bundle bundle = intent.getExtras();
            chat = (Chat) bundle.getSerializable("chat");
            user = new User();
            getSupportActionBar().setTitle(chat.getUserId());
            Toast.makeText(this, chat.getUserId(), Toast.LENGTH_SHORT).show();
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
        databaseReference = FirebaseDatabase.getInstance().getReference().child("message").child(userId);
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                messageList.add(message);
                messageId.add(dataSnapshot.getKey());
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
        databaseReference = FirebaseDatabase.getInstance().getReference().child("message").child(userId);
        Message message = new Message(userId, userId, editText.getText().toString(), "", ServerValue.TIMESTAMP, false);
        String key = databaseReference.push().getKey();
        databaseReference.child(key).setValue(message);
        editText.setText("");
        recyclerView.scrollToPosition(messageList.size() - 1);
        Toast.makeText(this, "Send successfully", Toast.LENGTH_SHORT);
    }

    @Override
    public void onClick(View view) {

    }
}
