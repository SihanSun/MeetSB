package cse110.com.meetsb;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import cse110.com.meetsb.Model.Course;
import cse110.com.meetsb.Model.User;
import cse110.com.meetsb.Model.UserCardAdapter;
import cse110.com.meetsb.Model.UserCardMode;
import cse110.com.meetsb.Model.UserSwipe;

public class SwipeActivity extends AppCompatActivity {

    final int refreshSize = 5;

    private ArrayList<UserCardMode> userCard;
    private UserCardAdapter arrayAdapter;
    private SwipeFlingAdapterView flingContainer;


    private List<List<String>> imageList;
    private Button btnDislike, btnLike, btnProfile, btnChat;

    //animation
    ImageView iv_loading;
    AnimationDrawable loadingDrawable;
    boolean annimatioOn;

    //retrieving finish flag
    boolean finishRefresh;

    //course information
    User user;
    List<String> courseTaking;
    String currentCourse;

    //user swipe information
    UserSwipe userSwipe;

    //Progress Bar
    private ProgressDialog progressDialog;

    //Firebase
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    //thread flag
    boolean stopThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        //create a progress dialog instance pointing to this activity
        progressDialog = new ProgressDialog(this);

        //initialize the Firebase Relevant
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getReference();

        //get the user's UID
        String uid = firebaseAuth.getCurrentUser().getUid();

        //catch all the btns
        btnDislike = (Button) findViewById(R.id.buttons_button_dislike);
        btnLike = (Button) findViewById(R.id.buttons_button_like);
        btnProfile  = (Button) findViewById(R.id.buttons_button_profile);
        btnChat = (Button) findViewById(R.id.buttons_button_chat);

        //initialize the imageList of size ten
        imageList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<String> s = new ArrayList<>();
            s.add(imageUrls[0]);
            imageList.add(s);
        }

        //set the swipe view
        this.flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        this.userCard = new ArrayList<>();
        this.arrayAdapter = new UserCardAdapter(SwipeActivity.this, R.layout.item, R.id.item_textView_user, userCard);
        this.flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                userCard.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                UserCardMode userCardMode = (UserCardMode) dataObject;
                makeToast(SwipeActivity.this, "Dislike " + userCardMode.getName());
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                //Do something on the right!
                UserCardMode userCardMode = (UserCardMode) dataObject;
                makeToast(SwipeActivity.this, "Like"  + userCardMode.getName());
            }

            @Override
            /**
             * what to do when list is about to be empty
             */
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                //makeToast(SwipeActivity.this, "refreshing!!!!");
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

        // add an OnItemClickListener to define what to do after item being clicked
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                makeToast(SwipeActivity.this, "You Clicked Gary!!");
                startActivity(new Intent(SwipeActivity.this, OtherUserActivity.class));
            }
        });

        //start animation
        setUpAnnimation();
        beginFlash();

        //set up course information, swipe information, and the list
        setUp();
    }

    /*
        Annmimation Controller
     */
    private void setUpAnnimation() {
        annimatioOn = false;
        iv_loading = (ImageView) findViewById(R.id.iv_loading);
        loadingDrawable = (AnimationDrawable) iv_loading.getDrawable();
    }
    private void beginFlash() {
        if(annimatioOn) {
            return;
        }
        //begin the flash
        flingContainer.setVisibility(View.INVISIBLE);
        loadingDrawable.start();
        annimatioOn = true;
    }

    private void endFlash() {
        if(!annimatioOn) {
            return;
        }
        flingContainer.setVisibility(View.VISIBLE);
        loadingDrawable.stop();
        annimatioOn = false;
    }

    /*
        User Offset Controller
     */
    private void updateUserOffset() {
        int currentOffset = user.getCourseTakingOffsetMap().get(currentCourse);
        user.getCourseTakingOffsetMap().put(currentCourse, currentOffset+1);
    }

    /*
        Thread Controller
     */
    private void startThread() {
        stopThread = false;
        finishRefresh = true;
        Runnable refreshHelper = new Runnable() {
            @Override
            public void run() {
                while( !stopThread ) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        System.out.println("Failed to sleep");
                    }
                    if(userCard != null && userCard.size() <= 1) {
                        if(userCard.size() == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    beginFlash();
                                }
                            });
                        }
                        refreshUserCard();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                endFlash();
                            }
                        });
                    }
                }
                endFlash();
            }
        };
        Thread refreshThread = new Thread(refreshHelper);
        refreshThread.start();
    }

    protected void onDestroy() {
        stopThread=true;
        super.onDestroy();
    };

    private void setUp() {
        setCourseTaking();
    }

    private void setCourseTaking() {
        String uid = firebaseAuth.getCurrentUser().getUid();

        databaseReference.child("USER").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                //get the courses that the user is taking and get the default course
                for(String courseName : user.getCourseTakingOffsetMap().keySet()) {
                    if(courseTaking == null) {
                        courseTaking = new ArrayList<>();
                    }
                    courseTaking.add(courseName);
                }

                currentCourse = courseTaking.get(0);
                setUserSwipe();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                flingContainer.setVisibility(View.VISIBLE);
                makeToast(SwipeActivity.this, "Failed to get user information");
            }
        });
    }

    private void setUserSwipe() {

        //get current user's uid
        String uid = firebaseAuth.getCurrentUser().getUid();

        //get the userSwipe information of current user
        databaseReference.child("USERSWIPE").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get the user swipe information
                userSwipe = dataSnapshot.getValue(UserSwipe.class);

                //set swipe card
                setSwipeCard();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                flingContainer.setVisibility(View.VISIBLE);
                //make a toast
                Toast.makeText(SwipeActivity.this, "Failed to refresh user swipe card", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSwipeCard() {
        //set click listenr for swipe card
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SwipeActivity.this, ProfileActivity.class));
            }
        });
        btnDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userCard == null || userCard.size() == 0) {
                    return;
                }
                flingContainer.getTopCardListener().selectLeft();
            }
        });
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userCard == null || userCard.size() == 0) {
                    return;
                }
                flingContainer.getTopCardListener().selectRight();
            }
        });
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SwipeActivity.this, MatchListActivity.class));
            }
        });

        //begin the careRefreshThread
        startThread();
    }

    private void refreshUserCard() {
        if(!finishRefresh) {
            return;
        }
        finishRefresh = false;
        //update the user
        databaseReference.child("USER").child(firebaseAuth.getCurrentUser().getUid()).setValue(user);

        //update student uid list
        databaseReference.child("COURSE").child(currentCourse).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Course course = dataSnapshot.getValue(Course.class);
                List<String> uidList = course.getStudentsInTheCourse();

                //get current offset
                int offset = user.getCourseTakingOffsetMap().get(currentCourse);

                //get next refreshSize of user UID
                List<String> userUidToBeLoaded = new ArrayList<>();
                for(int count = 0 ; count < refreshSize ; count++) {
                    if(offset >= uidList.size()) {
                        break;
                    }
                    userUidToBeLoaded.add(uidList.get(offset));
                    offset++;
                }

                //update user offset
                user.getCourseTakingOffsetMap().put(currentCourse, offset);

                //update card view
                for(int i = 0 ; i < userUidToBeLoaded.size() ; i++) {
                    final String otherUserUid = userUidToBeLoaded.get(i);
                    if(otherUserUid.equals(firebaseAuth.getCurrentUser().getUid())) {
                        continue;
                    }
                    databaseReference.child("USER").child(otherUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User otherUser = dataSnapshot.getValue(User.class);
                            addUserCard(otherUser);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                finishRefresh = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private synchronized void addUserCard(User user){
        String name = user.getUserName();
        int year = user.getGraduationYear();
        List<String> image = imageList.get(1);
        userCard.add(new UserCardMode(name, year, image));
        arrayAdapter.notifyDataSetChanged();
    }

    /*
        Functionality after swiping right
        pre-condition: the user swipe right on a user
        post-condition: 1. add the uid to user's like list in userSwipe
                        2. check if the other user also likes current user
                                if yes, add both user to each other's match list and prompt a toast.
     */
    private void swipeRight(final String uid) {
        //TODO
    }

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    public final String[] imageUrls = new String[]{
            "http://i.imgur.com/P7Z6Ogv.png",
            "http://i.imgur.com/P7Z6Ogv.png"
    };
}