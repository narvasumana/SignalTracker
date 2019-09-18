package com.stechnologies.signaltracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText registerEmail,registerPassword,registerPhoneNumber,statusPassword;
    Button registerButton;
    ToggleButton accountStatus;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        registerEmail = (EditText) findViewById(R.id.registerEmail);
        registerPassword = (EditText) findViewById(R.id.registerPassword);
        registerPhoneNumber = (EditText)findViewById(R.id.registerPhone);
        registerButton = (Button) findViewById(R.id.registerButton);
        statusPassword = (EditText)findViewById(R.id.statusPassword);
        accountStatus = (ToggleButton) findViewById(R.id.userStatus);
        accountStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    statusPassword.setVisibility(View.VISIBLE);
                }
                else{
                    statusPassword.setVisibility(View.INVISIBLE);
                }
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailId = registerEmail.getText().toString();
                String password = registerPassword.getText().toString();
                String phoneNumber = registerPhoneNumber.getText().toString();
                if(emailId != null && password != null && phoneNumber != null){
                    if(password.length()>=6 && TextUtils.isDigitsOnly(phoneNumber) && phoneNumber.length() == 10){
                        if(accountStatus.isChecked()){
                            if(statusPassword.getText().toString() != null && statusPassword.getText().toString() !=""){
                                createAccount(emailId,password);
                            }
                        }else{
                            createAccount(emailId,password);
                        }

                        //TODO update phone number in json as profile
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Enter valid iputs",Toast.LENGTH_LONG).show();
                }
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    //TODO navigate to loginactivity
                    Toast.makeText(getApplicationContext(),"Registration Successfull",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);

                    String statusPass=statusPassword.getText().toString();

                    Profile p = new Profile(user.getEmail(),accountStatus.isChecked(),statusPass);
                    mDatabase.child("profile").child(registerPhoneNumber.getText().toString()).setValue(p);

                    Log.d("user", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Toast.makeText(getApplicationContext(),"Enter valid details",Toast.LENGTH_LONG).show();
                    Log.d("user", "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]

                // [END_EXCLUDE]
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void createAccount(String email, String password) {
        Log.d("in function", "createAccount:" + email);
        if (!validateForm()) {
            return;
        }


        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("SignUp Complete", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Enter Valid Details",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "Hello ", Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]

                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = registerEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            registerEmail.setError("Required.");
            valid = false;
        } else {
            registerEmail.setError(null);
        }

        String password = registerPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            registerPassword.setError("Required.");
            valid = false;
        } else {
            registerPassword.setError(null);
        }

        return valid;
    }
}
