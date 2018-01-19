package com.thaneshdavuluri.dosakaya;

import android.net.Uri;

import java.io.Serializable;
import java.net.URI;

/**
 * Created by thanesh.davuluri on 5/25/2017.
 */

public class UserModel implements Serializable {
    public enum LoginMethod implements Serializable{
        GOOGLE,
        DEFAULT,
        //for future use
        FACEBOOK
    }
    private String mEmail;
    private String mPassword;
    private String mName;
    private Uri mImageUri;
    //private byte[] mImageAttachment;
    private LoginMethod mCurrentLoginMethod;

    public UserModel(String mEmail,String mPassword,String mName,Uri mImageUri,
                     UserModel.LoginMethod loginMethod)
    {
        this.mEmail=mEmail;
        this.mPassword=mPassword;
        this.mName=mName;
        this.mImageUri=mImageUri;
        this.mCurrentLoginMethod=loginMethod;
    }
    public UserModel(){}
    //setters
    public void setEmail(String email){
        mEmail=email;
    }
    public void setPassword(String password){
        mPassword=password;
    }
    public void setName(String name){mName=name;}
    //public void setImageAttachment(byte[] imageAttachment){mImageAttachment=imageAttachment;}
    public void setImageUri(Uri uri){mImageUri=uri;}
    public void setCurrentLoginMethod(LoginMethod mCurrentLoginMethod) {
        this.mCurrentLoginMethod = mCurrentLoginMethod;
    }

    //getters
    public String getEmail(){
        return mEmail;
    }
    public String getPassword(){
        return  mPassword;
    }
    public String getName(){return mName;}
    //public byte[] getImageAttachment(){return mImageAttachment;}
    public Uri getImageUri() {return mImageUri;}
    public LoginMethod getCurrentLoginMethod(){return mCurrentLoginMethod;}
}
