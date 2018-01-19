package com.thaneshdavuluri.dosakaya;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.mikhaellopez.circularimageview.CircularImageView;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,LoadImageTask.Listener {
    SessionManager mSessionManager;
    LoginHelper mLoginHelper;
    DatabaseHelper mDatabaseHelper;
    UserModel mCurrentUser;
    GoogleApiClient mGoogleApiClient;
    //
    CircularImageView mCircularImageView;
    TextView mName;
    TextView mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        mCircularImageView= (CircularImageView)findViewById(R.id.contact_circle_image);
        mName=(TextView) findViewById(R.id.name);
        mEmail=(TextView)findViewById(R.id.email);
        mName.setText(mCurrentUser.getName());
        mEmail.setText(mCurrentUser.getEmail());
        if(mCurrentUser.getImageUri()!=null){
            new LoadImageTask(this).execute(
                    mCurrentUser.getImageUri().toString()
            );
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                    if(mCurrentUser.getCurrentLoginMethod()== UserModel.LoginMethod.GOOGLE)
                    {
                        googleSignOut();
                    }
                    mLoginHelper.logout(mCurrentUser);
                break;
        }
        return true;
    }
    private void init(){
        setupGoogleOptions();
        mLoginHelper=new LoginHelper(this);
        mSessionManager=new SessionManager(this);
        mDatabaseHelper=new DatabaseHelper(this,1);
        mCurrentUser=getCurrentUser(
                mSessionManager
                        .getUserDetails()
                        .get(SessionManager.KEY_EMAIL)
        );
    }
    UserModel getCurrentUser(String email){
        return mDatabaseHelper
                .getUserByEmail(email);
    }

    //google related code
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
    }
    private void googleSignOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        Toast.makeText(MainActivity.this, "Successfully signed out!", Toast.LENGTH_SHORT).show();
                        // [END_EXCLUDE]
                    }
                });
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) {
        //set image here
        mCircularImageView.setImageBitmap(bitmap);
        //Glide.with(this).load(bitmap).error(R.drawable.ic_add).into(mCircularImageView);
        Toast.makeText(this, "Successfully Downloaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError() {
        //if the image download failed
    }
}
