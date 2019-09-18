package com.stechnologies.signaltracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    Button signalButton,trackButton,offerButton,signOutButton,cancelButton,phoneNumberButton,passwordPhone;
    EditText phoneNumberEditText,passwordPhoneNumber;
    String passwordSecurity,username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signalButton = (Button) findViewById(R.id.signalButton);
        trackButton = (Button) findViewById(R.id.trackButton);
        offerButton = (Button) findViewById(R.id.offersButton);
        signOutButton = (Button) findViewById(R.id.signOutButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        phoneNumberButton = (Button)findViewById(R.id.phoneNumberSumitButton);
        passwordPhone = (Button)findViewById(R.id.securityPassword);
        passwordPhoneNumber = (EditText)findViewById(R.id.passwordPhoneNumber);
        phoneNumberEditText = (EditText) findViewById(R.id.searchPhoneNumber);

        passwordPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordPhoneNumber.getText().toString();
                if(passwordSecurity.equals(password)){
                    Intent i = new Intent(MainActivity.this,TrackActivity.class);
                    i.putExtra("number",username);
                    startActivity(i);
                }
                else{
                    Toast.makeText(MainActivity.this,"enter crt details",Toast.LENGTH_LONG).show();
                }
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        signalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SignalStatusActivity.class);
                startActivity(i);
            }
        });

        trackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumberEditText.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
                phoneNumberButton.setVisibility(View.VISIBLE);
                trackButton.setVisibility(View.INVISIBLE);
                signalButton.setVisibility(View.INVISIBLE);
                offerButton.setVisibility(View.INVISIBLE);
                signOutButton.setVisibility(View.INVISIBLE);
            }
        });

        offerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumberEditText.setVisibility(View.INVISIBLE);
                cancelButton.setVisibility(View.INVISIBLE);
                phoneNumberButton.setVisibility(View.INVISIBLE);
                trackButton.setVisibility(View.VISIBLE);
                signalButton.setVisibility(View.VISIBLE);
                offerButton.setVisibility(View.VISIBLE);
                signOutButton.setVisibility(View.VISIBLE);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumberEditText.setVisibility(View.INVISIBLE);
                cancelButton.setVisibility(View.INVISIBLE);
                phoneNumberButton.setVisibility(View.INVISIBLE);
                trackButton.setVisibility(View.VISIBLE);
                signalButton.setVisibility(View.VISIBLE);
                offerButton.setVisibility(View.VISIBLE);
                signOutButton.setVisibility(View.VISIBLE);
            }
        });

        phoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberEditText.getText().toString();
                if(phoneNumber != null && phoneNumber != ""){
                    //TODO get data from firebase
                    DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("profile").child(phoneNumber);
                    dRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterable<DataSnapshot> children=  dataSnapshot.getChildren();
                            boolean b = false;
                            for(DataSnapshot d : children) {
                                if(d.getKey().contains("userStatus")) {
                                    boolean s = (boolean) d.getValue();
                                    b = s;
                                    if (s) {
                                        passwordPhoneNumber.setVisibility(View.VISIBLE);
                                        passwordPhone.setVisibility(View.VISIBLE);
                                    } else {
                                        passwordPhoneNumber.setVisibility(View.INVISIBLE);
                                        passwordPhone.setVisibility(View.INVISIBLE);
                                    }
                                }
                                else if(d.getKey().contains("password")){
                                    passwordSecurity = (String) d.getValue();
                                }
                                else{
                                    username = (String) d.getValue();
                                    if(!b){
                                        Intent i = new Intent(MainActivity.this,TrackActivity.class);
                                        i.putExtra("number",username.split("@")[0]);
                                        startActivity(i);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }
}
