package cse110.com.meetsb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.*;
import cse110.com.meetsb.Model.Chat;
import cse110.com.meetsb.Model.ChatAdapter;
import cse110.com.meetsb.Model.User;

public class ChatActivity extends AppCompatActivity {
    private DatabaseReference firebaseReference;
    static final String CHAT_REFERENCE = "chat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private User user = new User();
    private String message;

    private RecyclerView rvMessage;
    private FirebaseAuth firebaseAuth;
    /**
     * send message to firebase
     */
    private void sendMessageFirebase(){
        Chat model = new Chat(user,message, Calendar.getInstance().getTime().getTime()+"");
        firebaseReference.child(CHAT_REFERENCE).push().setValue(model);
    }

    /**
     * Get message from firebase
     */
    private void getMessagesFirebase(){
        firebaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        //final ChatAdapter chatAdapter = new ChatAdapter(firebaseReference.child(CHAT_REFERENCE),userId,this);
        //chatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            /*@Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = chatAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    rvMessage.scrollToPosition(positionStart);
                }
            }
        });
        rvMessage.setLayoutManager(mLinearLayoutManager);
        rvMessage.setAdapter(firebaseAdapter);
    */
    }
}
