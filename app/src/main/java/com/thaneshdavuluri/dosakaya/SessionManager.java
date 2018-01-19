package com.thaneshdavuluri.dosakaya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by thanesh.davuluri on 5/25/2017.
 */

public class SessionManager {
    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;
    Context mContext;
    private static final int PRIVATE_MODE = 0;
    private static final String PREF_FILE_NAME = "LoginSamplePreferences";
    private static final String KEY_LOGGED_IN = "IsLoggedIn";
    private static final String KEY_LOGIN_METHOD="LoginMethod";
    public static final String KEY_NAME = "Name";
    public static final String KEY_EMAIL = "Email";

    public SessionManager(Context context) {
        mContext = context;
        mPreferences = mContext.getSharedPreferences(PREF_FILE_NAME, PRIVATE_MODE);
        mEditor = mPreferences.edit();
    }

    public void createLoginSession(String name, String email, UserModel.LoginMethod loginMethod) {

        // Storing login value as TRUE
        mEditor.putBoolean(KEY_LOGGED_IN, true);

        // Storing name in pref
        mEditor.putString(KEY_NAME, name);

        // Storing email in pref
        mEditor.putString(KEY_EMAIL, email);

        //Storing login method
        mEditor.putString(KEY_LOGIN_METHOD,loginMethod.name());

        // commit changes
        mEditor.commit();
    }

   /* public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(mContext, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            mContext.startActivity(i);
        }
    }*/

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        mEditor.clear();
        mEditor.commit();
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, mPreferences.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, mPreferences.getString(KEY_EMAIL, null));

        // return user
        return user;
    }

    // Get Login State
    public boolean isLoggedIn() {
        return mPreferences.getBoolean(KEY_LOGGED_IN, false);
    }

}

