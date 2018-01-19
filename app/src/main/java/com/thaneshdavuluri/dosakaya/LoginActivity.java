package com.thaneshdavuluri.dosakaya;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 9001;
    GoogleApiClient mGoogleApiClient;
    LoginHelper mLoginHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // setting up login helper
        mLoginHelper=new LoginHelper(this);
        Button loginButton=(Button)findViewById(R.id.login_button);
        TextView signUpText=(TextView)findViewById(R.id.sign_up_text);
        Button googleSignupBtn=(Button)findViewById(R.id.google_sign_in_button);

        //setting up on click listeners
        loginButton.setOnClickListener(this);
        signUpText.setOnClickListener(this);

        setupGoogleOptions();

        googleSignupBtn.setOnClickListener(this);


        //testing db functionality
        DatabaseHelper databaseHelper=new DatabaseHelper(this,1);
        databaseHelper.getAllUsers();

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_button:
                EditText email=(EditText)findViewById(R.id.email);
                EditText password=(EditText)findViewById(R.id.password);
                Log.d("Button","Login clicked");
                //login
                String passwordHash=new String(
                        Hex.encodeHex(DigestUtils.md5(password.getText().toString()))
                );
                mLoginHelper.login(
                        email.getText().toString().trim(),
                        passwordHash
                );
                break;
            case R.id.sign_up_text:
                //signup
                Intent i=new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(i);
                break;
            case R.id.google_sign_in_button:
                signIn();
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RC_SIGN_IN:
                //google sign in result
                Log.d("Status","in oAR");
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
                break;
        }
    }
    //GOOGLE RELATED CODE
    void setupGoogleOptions(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this/* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("sign in result", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.

            GoogleSignInAccount account=result.getSignInAccount();
            UserModel user=new UserModel();
            user.setEmail(account.getEmail());
            Log.d("data",user.getEmail());
            user.setName(account.getDisplayName());
            Log.d("data",user.getName());
            user.setImageUri(account.getPhotoUrl());
            Log.d("data",user.getImageUri().toString());
            user.setCurrentLoginMethod(UserModel.LoginMethod.GOOGLE);
            Log.d("data",user.getCurrentLoginMethod().name());
            mLoginHelper.saveUserLogin(user);
            Intent i=new Intent(this,MainActivity.class);
            startActivity(i);
            Toast.makeText(this, "Sign in successful", Toast.LENGTH_SHORT).show();
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(this, "Sign phailed", Toast.LENGTH_SHORT).show();
        }
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
    }
}
