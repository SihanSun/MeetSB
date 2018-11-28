package cse110.com.meetsb;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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
    int offset;
    int readOffset;
    int userOffset = -1;

    private long backPressedTime;

    private ArrayList<UserCardMode> userCard;
    private UserCardAdapter arrayAdapter;
    private SwipeFlingAdapterView flingContainer;


    private List<List<String>> imageList;
    private Spinner courseChoosing;
    private Button btnDislike, btnLike, btnProfile, btnChat, btnAddClass;

    //animation
    ImageView iv_loading;
    AnimationDrawable loadingDrawable;
    boolean annimatioOn;

    //user uid
    String userUid;

    //course information
    User user;
    List<String> courseTaking;
    String currentCourse;
    int currentCourseIndex;
    int selectedCourseIndex;
    List<String> studentInThisCourse;

    //user swipe information
    HashSet<String> liked;
    HashSet<String> matchSet;

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

        //get the current course selected if applicable
        selectedCourseIndex = -1;
        String indexString = getIntent().getStringExtra("courseChoosing");
        if(indexString != null) {
            selectedCourseIndex = Integer.parseInt(indexString);
        }

        //initialize the Firebase Relevant
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        //get the user's UID
        userUid = firebaseAuth.getCurrentUser().getUid();

        //set the swipe infomation
        matchSet = new HashSet<>();
        liked = new HashSet<>();

        //set the student in the course
        studentInThisCourse = new ArrayList<>();

        //catch all the btns
        btnDislike = (Button) findViewById(R.id.buttons_button_dislike);
        btnLike = (Button) findViewById(R.id.buttons_button_like);
        btnProfile  = (Button) findViewById(R.id.classInfo_button_profile);
        btnChat = (Button) findViewById(R.id.classInfo_button_chat);
        btnAddClass = findViewById(R.id.classInfo_button_add);

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
                increaseUserOffset();
                UserCardMode userCardMode = (UserCardMode) dataObject;
                makeToast(SwipeActivity.this, "Dislike " + userCardMode.getName());
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                //Do something on the right!
                increaseUserOffset();
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
                UserCardMode tempCardMode = (UserCardMode) dataObject;
                String otherUserUid = tempCardMode.getUid();
                Intent intent = new Intent(SwipeActivity.this, OtherUserActivity.class);
                intent.putExtra("otherUser", otherUserUid);
                startActivity(intent);
            }
        });

        //set the button
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SwipeActivity.this, ProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        btnAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SwipeActivity.this, AddClassActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
                        Thread.sleep(500);
                    } catch (Exception e) {
                        System.out.println("Failed to sleep");
                    }

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
        databaseReference.child("USER").child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                courseTaking = new ArrayList<>();
                //get the courses that the user is taking and get the default course
                for(String courseName : user.getCourseTakingOffsetMap().keySet()) {
                    courseTaking.add(courseName);
                }

                //set the spinner view
                courseChoosing = (Spinner)findViewById(R.id.classInfo_spinner_class);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, courseTaking);
                courseChoosing.setAdapter(adapter);

                //set current course
                if(courseTaking.size() == 0) {
                    currentCourse = null;
                    offset = -1;
                    readOffset = -1;
                } else {
                    if (selectedCourseIndex == -1) {
                        currentCourse = courseTaking.get(0);
                        currentCourseIndex = 0;
                        selectedCourseIndex = 0;
                    } else {
                        currentCourseIndex = selectedCourseIndex;
                        currentCourse = courseTaking.get(currentCourseIndex);
                    }
                    courseChoosing.setSelection(selectedCourseIndex);
                    courseChoosing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            selectedCourseIndex = i;
                            if(selectedCourseIndex != currentCourseIndex) {
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                Intent changeCourseIntent = new Intent(SwipeActivity.this, SwipeActivity.class);
                                changeCourseIntent.putExtra("courseChoosing", Integer.toString(selectedCourseIndex));
                                finish();
                                startActivity(changeCourseIntent);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    offset = user.getCourseTakingOffsetMap().get(currentCourse);
                    readOffset = offset;
                }


                //update the matchList

                databaseReference.child("USERSWIPE").child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserSwipe userSwipe = dataSnapshot.getValue(UserSwipe.class);
                        if(userSwipe == null) {
                            return;
                        }
                        HashMap<String, String> matchList = userSwipe.getMatchList();
                        for(String key : matchList.keySet()) {
                            String otherUserUid = matchList.get(key);
                            matchSet.add(otherUserUid);
                        }
                        HashMap<String, String> likedList = userSwipe.getLiked();
                        for(String key : likedList.keySet()) {
                            String otherUserUid = matchList.get(key);
                            liked.add(otherUserUid);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //attch a lister to the match list
                databaseReference.child("USERSWIPE").child(userUid).child("matchList").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String key = dataSnapshot.getKey();
                        String value = dataSnapshot.getValue(String.class);
                        if(!matchSet.contains(value)) {
                            Toast.makeText(SwipeActivity.this, "Congratulations, you got a new match!", Toast.LENGTH_LONG).show();
                        }
                        matchSet.add(value);
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
                });

                //attach a listener for the students in the course (order by key)
                if(currentCourse != null) {
                    databaseReference.child("COURSE")
                            .child(currentCourse)
                            .child("studentsInTheCourse")
                            .orderByKey()
                            .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            String key = dataSnapshot.getKey();
                            String value = dataSnapshot.getValue(String.class);
                            studentInThisCourse.add(value);
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
                    });
                }
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

    /*
        Refresh Card Controller
     */
    private synchronized void refreshUserCard() {
        if(currentCourse == null || userCard.size() > 0 || offset >= studentInThisCourse.size()) {
            return;
        }

        for(int i = 0 ; i < refreshSize ; i++) {
            if(offset >= studentInThisCourse.size()) {
                break;
            }
            final String otherUserUid = studentInThisCourse.get(offset);
            if(otherUserUid.equals(userUid)) {
                userOffset = i;
            } else {
                databaseReference.child("USER").child(otherUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final User otherUser = dataSnapshot.getValue(User.class);

                        if(otherUser == null) {
                            return;
                        }

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
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
            }
            offset ++;
        }

    }

    private void swipeRight(final String otherUid) {
        //add user to current user swipe like list

        if(liked.contains(otherUid)) {
            return;
        }

        //update the liked list
        liked.add(otherUid);
        String key = databaseReference.child("USERSWIPE").child(userUid).child("liked").push().getKey();
        databaseReference.child("USERSWIPE").child(userUid).child("liked").child(key).setValue(otherUid);

        //check if other also likes current user
        databaseReference.child("USERSWIPE").child(otherUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserSwipe otherUserSwipe = dataSnapshot.getValue(UserSwipe.class);

                //maybe at this point other user's swipe has not be set up yet
                if(otherUserSwipe != null) {

                    HashMap<String, String> otherLikedList = otherUserSwipe.getLiked();
                    if(otherLikedList.containsKey(userUid)) {
                        String tempKey = databaseReference.child("USERSWIPE").child(otherUid).child("matchList").push().getKey();
                        databaseReference.child("USERSWIPE").child(otherUid).child("matchList").child(tempKey).setValue(userUid);

                        //update your matchList
                        matchSet.add(otherUid);
                        tempKey = databaseReference.child("USERSWIPE").child(userUid).child("matchList").push().getKey();
                        databaseReference.child("USERSWIPE").child(userUid).child("matchList").child(tempKey).setValue(otherUid);

                        //make a toast
                        Toast.makeText(SwipeActivity.this, "Congratulations, you got a new match!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private synchronized void increaseUserOffset() {
        if(readOffset == userOffset) {
            readOffset += 2;
        } else {
            readOffset += 1;
        }
        databaseReference.child("USER").child(userUid).child("courseTakingOffsetMap").child(currentCourse).setValue(readOffset);
    }

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {


        if(backPressedTime + 2000 > System.currentTimeMillis()){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }
        else{
            Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }

}