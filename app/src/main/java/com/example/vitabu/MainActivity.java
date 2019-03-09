package com.example.vitabu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private String logTag = "MainActivity";
    public static final String EXTRA_MESSAGE = "com.example.vitabu.MESSAGE";
    private LocalUser localUser;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Initialize firebase auth.
        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart(){
        super.onStart();
        //FirebaseUser user = auth.getCurrentUser();
        // Check if already signed in.
//        if (user != null) {
//            Log.i(logTag, "Signed in as: " + user.toString());
//            updateUI(user);
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    /**
     * Sign in user with firebase.
     */
    public void signIn(final String email, final String password) {
        // Attempt to sign in a user with email.
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(logTag, "Successfully signed in with email: " + email);
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            updateUI(firebaseUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(logTag, "Failed to sign in with email: " + email, task.getException());
                            Toast.makeText(MainActivity.this, "Sign In failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }



    /**
     * Signs a firebase user out.
     */
    public void signOut(){
        FirebaseUser usr = auth.getCurrentUser();
        if (usr == null){
            return;
        }
        Log.i("AuthActivity", "Signing out user : " + usr.toString());
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("AuthActivity", "Signed out user successfully!");
                        updateUI(null);
                    }
                });
    }


    public void onPressLogin(View view) {
        Intent intent = new Intent(this, browseBooksActivity.class);
        startActivity(intent);

        // TODO: Validate login details. If valid email/password combo, proceed, otherwise alert user to incorrect login.
//        String email = ((TextView) findViewById(R.id.login_email)).getText().toString();
//        String password = ((TextView) findViewById(R.id.login_password)).getText().toString();
//        signIn(email, password);
        // TODO Launch UI B activity.
    }

    public void onPressRegister(View view) {
        //signUp();
        Intent intent = new Intent(this, registerActivity.class);
        startActivity(intent);
        // Firebase user now created.
        // TODO start activity to finish creating profile. ie. username, picture, default location etc.
    }

    private void updateLocalUser(LocalUser localUser){
        this.localUser = localUser;
    }

    private void updateFirebaseUser(FirebaseUser firebaseUser){
        this.firebaseUser = firebaseUser;
    }

    public void updateUI(FirebaseUser firebaseUser){
        if (firebaseUser == null) {
            // No user signed in.
            return;
        }
        // TODO Update ui here with newly signed in users info.
        String userName = firebaseUser.getDisplayName();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("users").child(userName).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        updateLocalUser(dataSnapshot.getValue(LocalUser.class));
                        Log.d(logTag, "Read owner");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(logTag, "Cancelled");
                    }
                }
        );
        myRef.child("firebaseUsers").child(userName).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        updateFirebaseUser(dataSnapshot.getValue(FirebaseUser.class));
                        Log.d(logTag, "Read owner");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(logTag, "Cancelled");
                    }
                }
        );
        localUser.setFirebaseUser(firebaseUser);
        IntentJson passing = new IntentJson(localUser);
        Intent intent = new Intent(this, browseBooksActivity.class);
        intent.putExtra(EXTRA_MESSAGE, passing.toJson());
        startActivity(intent);
    }
}
