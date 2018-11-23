package cse110.com.meetsb;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
    int refreshCount = 5;

    UserCardMode preUserCard;
    private ArrayList<UserCardMode> userCard;
    private UserCardAdapter arrayAdapter;
    private SwipeFlingAdapterView flingContainer;


    private List<List<String>> imageList;
    private Button btnDislike, btnLike, btnProfile, btnChat;

    //animation
    ImageView iv_loading;
    AnimationDrawable loadingDrawable;
    boolean annimatioOn;

    //user uid
    String userUID;

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
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

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
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        //get the user's UID
        userUID = firebaseAuth.getCurrentUser().getUid();

        //catch all the btns
        btnDislike = (Button) findViewById(R.id.buttons_button_dislike);
        btnLike = (Button) findViewById(R.id.buttons_button_like);
        btnProfile  = (Button) findViewById(R.id.buttons_button_profile);
        btnChat = (Button) findViewById(R.id.buttons_button_chat);

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
                preUserCard = new UserCardMode(userCardMode.getName(), userCardMode.getYear(), userCardMode.getImages(), userCardMode.getUid());

                makeToast(SwipeActivity.this, "Dislike " + userCardMode.getName());
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                //Do something on the right!
                UserCardMode userCardMode = (UserCardMode) dataObject;
                String otherUID = userCardMode.getUid();
                swipeRight(otherUID);
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

        //set the button
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
        Thread Controller
     */
    private void startThread() {
        stopThread = false;
        Runnable refreshHelper = new Runnable() {
            @Override
            public void run() {
                while( !stopThread ) {
                    try {
                        Thread.sleep(300);
                    } catch (Exception e) {
                        System.out.println("Failed to sleep");
                    }

                    //update the matchList
                    databaseReference.child("USERSWIPE")
                            .child(userUID)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            UserSwipe tempUserSwipe = dataSnapshot.getValue(UserSwipe.class);
                            if(tempUserSwipe != null) {
                                HashMap<String, String> tempMap = tempUserSwipe.getMatchList();
                                if(tempMap != null && userSwipe.getMatchList().size() < tempMap.size()) {
                                    userSwipe.setMatchList(tempMap);
                                    //make a toast
                                    Toast.makeText(SwipeActivity.this, "Congratulations, you got a new match!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    if(userCard != null && userCard.size() == 0) {
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

    /*
        Setup Contoller during initialization
     */
    private void setUp() {

        //setup user and course
        databaseReference.child("USER").child(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
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

                //get the userSwipe
                databaseReference.child("USERSWIPE").child(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //get the user swipe information
                        userSwipe = dataSnapshot.getValue(UserSwipe.class);

                        if(userSwipe == null) {
                            userSwipe = new UserSwipe();
                            databaseReference.child("USERSWIPE").child(firebaseAuth.getCurrentUser().getUid()).setValue(userSwipe);
                        }

                        preUserCard = null;

                        //begin the careRefreshThread
                        startThread();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        flingContainer.setVisibility(View.VISIBLE);
                        //make a toast
                        Toast.makeText(SwipeActivity.this, "Failed to refresh user swipe card", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                flingContainer.setVisibility(View.VISIBLE);
                makeToast(SwipeActivity.this, "Failed to get user information");
            }
        });
    }

    /*
        Refresh Card Controller
     */
    private void refreshUserCard() {
        if(refreshCount < refreshSize) {
            return;
        }
        refreshCount = 0;

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
                            final User otherUser = dataSnapshot.getValue(User.class);

                            //get other user's image
                            storageReference.child("IMAGE").child(otherUserUid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUri = uri.toString();
                                    List<String> image = new ArrayList<>();
                                    image.add(imageUri);
                                    String name = otherUser.getUserName();
                                    int year = otherUser.getGraduationYear();
                                    userCard.add(new UserCardMode(name, year, image, otherUserUid));
                                    arrayAdapter.notifyDataSetChanged();
                                    refreshCount++;
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

    }

    /*
        Functionality after swiping right
        pre-condition: the user swipe right on a user
        post-condition: 1. add the uid to user's like list in userSwipe
                        2. check if the other user also likes current user
                                if yes, add both user to both user's match list and prompt a toast.
     */
    private void swipeRight(final String otherUid) {
        //add user to current user swipe like list
        userSwipe.getLiked().put(otherUid, otherUid);

        //update userSwipe in database
        databaseReference.child("USERSWIPE").child(userUID).child("liked").setValue(userSwipe.getLiked());

        //check if other also likes current user
        databaseReference.child("USERSWIPE").child(otherUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserSwipe otherUserSwipe = dataSnapshot.getValue(UserSwipe.class);

                //maybe at this point other user's swipe has not be set up yet
                if(otherUserSwipe != null) {

                    //check if you are in other's liked history
                    if(otherUserSwipe.getLiked().containsKey(userUID)) {
                        //add other to your matchList
                        userSwipe.getMatchList().put(otherUid, otherUid);

                        //add you to the other's match list
                        String key = databaseReference.child("USERSWIPE").child(otherUid).child("matchList").push().getKey();
                        databaseReference.child("USERSWIPE").child(otherUid).child("matchList").child(key).setValue(userUID);

                        //add other to your match list
                        userSwipe.getMatchList().put(key, otherUid);

                        //update your userSwipe
                        databaseReference.child("USERSWIPE").child(userUID).setValue(userSwipe);

                        //make a toast
                        Toast.makeText(SwipeActivity.this, "Congratulations, you got a new match!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private synchronized void revert() {
        if(preUserCard == null) {
            Toast.makeText(this, "No pre user!", Toast.LENGTH_SHORT).show();
            return;
        }

        int userCardSize = userCard.size();

        UserCardMode tempCardMode = new UserCardMode(preUserCard.getName(), preUserCard.getYear(), preUserCard.getImages(), preUserCard.getUid());
        userCard.add(tempCardMode);

        for(int i = 0 ; i < userCardSize ; i++) {
            UserCardMode tempMode = userCard.get(i);
            userCard.add(new UserCardMode(tempMode.getName(), tempMode.getYear(), tempMode.getImages(), tempMode.getUid()));
        }

        for(int i = 0 ; i < userCardSize ; i++) {
            flingContainer.getTopCardListener().selectLeft();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                arrayAdapter.notifyDataSetChanged();
            }
        });

        preUserCard = null;
    }

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }
}