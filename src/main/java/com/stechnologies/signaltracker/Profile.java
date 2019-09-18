package com.stechnologies.signaltracker;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by KH9216 on 3/17/2017.
 */

public class Profile {

    String userName;
    boolean userStatus;
    String password;

    public Profile(){

    }

    public Profile(String phoneNumber, boolean userStatus,String password){
        this.userStatus = userStatus;
        this.userName = phoneNumber;
        this.password = password;
    }
}
