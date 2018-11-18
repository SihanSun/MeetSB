package cse110.com.meetsb;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    LinearLayoutCompat show;

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
        databaseReference = FirebaseDatabase.getInstance().getReference().child("messages").child(userId);
    }

    @Override
    protected void onCreate(Bundle saveInstanceState){
        
    }

    @Override
    public void onClick(View view) {

    }
}
