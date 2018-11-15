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

    private ArrayList<UserCardMode> userCard;
    private UserCardAdapter arrayAdapter;
    private SwipeFlingAdapterView flingContainer;


    private List<List<String>> imageList;
    private Button btnDislike, btnLike, btnProfile, btnChat;

    //course information
    String currentCourse;
    List<String> courseTaking;

    //course class
    Course courseObject;

    //user swipe information
    UserSwipe userSwipe;



    //Progress Bar
    private ProgressDialog progressDialog;

    //Firebase
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

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

        //retrieve

        //fake course object
        this.courseObject = new Course();

        //initialize the current course
        //TODO
        currentCourse = "CSE110-Gary";


        // add an OnItemClickListener to define what to do after item being clicked
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                makeToast(SwipeActivity.this, "You Clicked Gary!!");
                startActivity(new Intent(SwipeActivity.this, OtherUserActivity.class));
            }
        });

        btnDislike = (Button) findViewById(R.id.buttons_button_dislike);
        btnLike = (Button) findViewById(R.id.buttons_button_like);
        btnProfile  = (Button) findViewById(R.id.buttons_button_profile);
        btnChat = (Button) findViewById(R.id.buttons_button_chat);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SwipeActivity.this, ProfileActivity.class));
            }
        });
        btnDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                left();
            }
        });
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                right();
            }
        });
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SwipeActivity.this, MatchListActivity.class));
            }
        });

//        // Hide swipe cards
//        flingContainer.setVisibility(View.INVISIBLE);
//        // Set up loading animation
//        ImageView iv_loading = (ImageView) findViewById(R.id.iv_loading);
//        AnimationDrawable loadingDrawable = (AnimationDrawable) iv_loading.getDrawable();
//        // Start loading animation
//        loadingDrawable.start();

    }

    private void setCourseTaking() {
        //should use flash this point instead of progressdialog
        progressDialog.setMessage("Please wait while we are preparing...");
        progressDialog.show();
        String uid = firebaseAuth.getCurrentUser().getUid();

        databaseReference.child("USER").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                //get the courses that the user is taking and get the default course
                courseTaking = user.getCourseTaking();
                currentCourse = courseTaking.get(0);
                progressDialog.setMessage("25% done...");

                //retrieving user swipe information
                setUserSwipe();
                //set the swipe card
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeToast(SwipeActivity.this, "Failed to get user information");
                progressDialog.dismiss();
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

                //update the progress Dialog
                progressDialog.setMessage("50% done...");

                //set up the course object
                setCourseObject();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //close the progressDialog
                progressDialog.dismiss();

                //make a toast
                Toast.makeText(SwipeActivity.this, "Failed to refresh user swipe card", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setCourseObject() {

        //get the list of students who are taking that course
        databaseReference.child("COURSE").child(currentCourse).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //set the courseObject
                courseObject = dataSnapshot.getValue(Course.class);

                //update the progress dialog
                progressDialog.setMessage("75% done...");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //close the progressDialog
                progressDialog.dismiss();

                //make a toast
                Toast.makeText(SwipeActivity.this, "Failed to set courseObject, please check your internet",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshUserCard() {
        //get user uid
        String uid = firebaseAuth.getCurrentUser().getUid();

        //query courseObject to get list of users
        List<String> uidList = courseObject.getStudentList(uid, 10);

        //for each uid, query the database to get the result
        for(String otherUserUid : uidList) {
            //if current user has already liked this user before, ignore this user
            if(userSwipe.getLiked().containsKey(otherUserUid)) {
                continue;
            }

            databaseReference.child("USER").child(otherUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //get the user
                    User user = dataSnapshot.getValue(User.class);
                    String name = user.getUserName();
                    int year = user.getGraduationYear();
                    List<String> image = imageList.get(1);

                    //check if current user has already liked this user
                    userCard.add(new UserCardMode(name, year, image));
                    arrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }


    private void setSwipeCard() {
        //initialize the imageList of size ten
        imageList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<String> s = new ArrayList<>();
            s.add(imageUrls[0]);
            imageList.add(s);
        }

        userCard = new ArrayList<>();

        userCard.add(new UserCardMode("Gary1", 21, imageList.get(1)));
        userCard.add(new UserCardMode("Gary2", 21, imageList.get(1)));
        userCard.add(new UserCardMode("Gary3", 21, imageList.get(1)));
        userCard.add(new UserCardMode("Gary4", 21, imageList.get(1)));
        userCard.add(new UserCardMode("Professor Gary", 21, imageList.get(1)));
        userCard.add(new UserCardMode("Gary Gary", 21, imageList.get(1)));
        userCard.add(new UserCardMode("Gary 教授", 21, imageList.get(1)));

        arrayAdapter = new UserCardAdapter(this, R.layout.item, R.id.item_textView_user, userCard);

        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
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
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
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
                refreshUserCard();
                makeToast(SwipeActivity.this, "Refreshing");
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
    }

    /*
        Functionality after swiping right
        pre-condition: the user swipe right on a user
        post-condition: 1. add the uid to user's like list in userSwipe
                        2. check if the other user also likes current user
                                if yes, add both user to each other's match list and prompt a toast.
     */
    private void swipeRight(final String uid) {
        //update userSwipe Information
        this.userSwipe.getLiked().get(this.currentCourse).add(uid);

        //get other user's swipe information
        databaseReference.child("USERSWIPE").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get current user uid
                String currentUserUid = firebaseAuth.getCurrentUser().getUid();

                //check if other user also liked current user
                UserSwipe otherUserSwipe = dataSnapshot.getValue(UserSwipe.class);
                if(otherUserSwipe.getLiked().containsKey(currentUserUid)) {
                    //make a toast to remind current user
                    Toast.makeText(SwipeActivity.this, "Congratulations, you got a new match!", Toast.LENGTH_SHORT).show();

                    //update other user's match List
                    otherUserSwipe.getMatchList().add(currentUserUid);
                    databaseReference.child("USERSWIPE").child(uid).setValue(otherUserSwipe);

                    //update current user's match List
                    userSwipe.getMatchList().add(uid);
                    databaseReference.child("USERSWIPE").child(firebaseAuth.getCurrentUser()
                            .getUid()).setValue(userSwipe);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //update current user's match List
                databaseReference.child("USERSWIPE").child(firebaseAuth.getCurrentUser()
                        .getUid()).setValue(userSwipe);
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
