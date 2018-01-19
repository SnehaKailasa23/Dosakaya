package com.thaneshdavuluri.dosakaya;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by thanesh.davuluri on 5/27/2017.
 */

public class LoginHelper {
    DatabaseHelper mDatabaseHelper;
    Context mCurrentContext;
    SessionManager mSessionManager;

    public LoginHelper(Context context){
        mDatabaseHelper=new DatabaseHelper(context,1);
        mCurrentContext=context;
        mSessionManager=new SessionManager(mCurrentContext);
    }
    public void login(String userEmail,String userPassword){
        if(mDatabaseHelper.userExists(userEmail)){
            //check their credentials
            if(mDatabaseHelper.checkCredentials(userEmail, userPassword)){
                UserModel currentUserModel=mDatabaseHelper.getUserByEmail(userEmail);
                saveUserLogin(currentUserModel);
                Intent loggedInActivity=new Intent(mCurrentContext,MainActivity.class);
                mCurrentContext.startActivity(loggedInActivity);
            }
            else{
                //invalid credentials
                Toast.makeText(mCurrentContext, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            //no such user
            Toast.makeText(mCurrentContext, "User Doesn't Exist", Toast.LENGTH_SHORT).show();
        }
    }
    public void logout(UserModel user){
        mSessionManager.logoutUser();
        // After logout redirect user to Loing Activity
        Intent i = new Intent(mCurrentContext, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        mCurrentContext.startActivity(i);
    }
    //maintaining state by saving user
    public void saveUserLogin(UserModel user){
        if(!mDatabaseHelper.userExists(user.getEmail())){
            mDatabaseHelper.createUser(user);
        }
        mSessionManager.createLoginSession(
                user.getName(),
                user.getEmail(),
                user.getCurrentLoginMethod()
        );
    }
}
