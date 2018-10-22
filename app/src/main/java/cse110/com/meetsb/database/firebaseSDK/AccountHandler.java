package cse110.com.meetsb.database.firebaseSDK;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.CursorAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cse110.com.meetsb.MainActivity;

public class AccountHandler {

    private FirebaseAuth firebaseAuth;

    private FirebaseUser currentUser;

    private boolean isSignedIn;

    public AccountHandler() {
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
    }

    public boolean signUpWith(String emailAddress, String password, final Context context) {
        if (currentUser != null) {
            Toast.makeText(context,
                    "User already signed in",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(emailAddress)) {
            Toast.makeText(context,
                    "Email address cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context,
                    "Password cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return false;
        }


        firebaseAuth.createUserWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            firebaseAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                Toast.makeText(context,
                                                        "Send email verification successfully. Please check your email",
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, task.getException().getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                                currentUser.delete();
                                            }
                                        }
                                    });

                        } else {
                            Toast.makeText(context, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        if (currentUser != null) {
            Toast.makeText(context,
                    "Failed to register",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
