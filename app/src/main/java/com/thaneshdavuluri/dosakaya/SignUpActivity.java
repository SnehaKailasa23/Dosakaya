package com.thaneshdavuluri.dosakaya;

import android.*;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int GET_FROM_GALLERY = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 4000;
    private SignUpHelper mSignUpHelper;
    private Uri mProfileImage;
    EditText mName;
    EditText mEmail ;
    EditText mPassword ;
    EditText mReEnteredPassword ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        FloatingActionButton addImageButton=(FloatingActionButton) findViewById(R.id.add_image);
        Button signupButton=(Button)findViewById(R.id.sign_up_button);
        mName = (EditText) findViewById(R.id.sign_up_name);
        mEmail = (EditText) findViewById(R.id.sign_up_email);
        mPassword = (EditText) findViewById(R.id.sign_up_password);
        mReEnteredPassword = (EditText) findViewById(R.id.sign_up_password);
        mSignUpHelper=new SignUpHelper(this);
        addImageButton.setOnClickListener(this);
        signupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        //View parentView;
        switch (id){
            case R.id.add_image:
                if(checkStoragePermissions()){
                    getImage();
                }
                else{
                    requestLocationPermissions(v);
                    if(checkStoragePermissions()){
                        getImage();
                    }
                }
                break;
            case R.id.sign_up_button:
                Log.d("sign up","in sign up button");
                if(mSignUpHelper.validate(v)) {
                    UserModel user =getUserModel();
                    if(mSignUpHelper.signUp(user)){
                        startActivity(new Intent(this,LoginActivity.class));
                    }
                }
                break;
        }
    }
    boolean checkStoragePermissions() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
    void requestLocationPermissions(View view) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            final Activity currentActivity=this;
            Snackbar.make(view, "give internet permissions?", Snackbar.LENGTH_LONG).setAction("ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(currentActivity, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
                }
            }).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
        }
    }
    UserModel getUserModel(){
        String passwordHash=new String(Hex.encodeHex(DigestUtils.md5(mPassword.getText().toString())));
        Log.d("HASH",passwordHash);
        UserModel user= new UserModel();
        user.setName(mName.getText().toString());
        user.setEmail(mEmail.getText().toString());
        if(mProfileImage!=null){
        user.setImageUri(mProfileImage);
        }
        else{
            user.setImageUri(
                    Uri.parse("android.resource://com.thaneshdavuluri.dosakaya/drawable/ic_user.png")
            );
        }
        user.setPassword(passwordHash);
        user.setCurrentLoginMethod(UserModel.LoginMethod.DEFAULT);
        return user;
    }
    void getImage(){
        startActivityForResult(
                new Intent(Intent.ACTION_PICK, android.provider.MediaStore
                        .Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case GET_FROM_GALLERY:
                if(resultCode== Activity.RESULT_OK){
                    mProfileImage=data.getData();
                    CircularImageView profilePicture=(CircularImageView)findViewById(R.id.profile_picture);
                    Log.d("uri","file:/"+mProfileImage.getPath());
                    Glide.with(this).load(mProfileImage).error(R.drawable.ic_plus).into(profilePicture);
                }
                else if(resultCode==Activity.RESULT_CANCELED){
                    Toast.makeText(this, "Image Selection Cancelled", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
