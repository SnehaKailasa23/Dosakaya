package com.thaneshdavuluri.dosakaya;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thanesh.davuluri on 5/27/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Contacts table name
    private static final String TABLE_USERS = "userTables";

    // Contacts Table Columns names
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PASSWORD = "Password";
    private static final String KEY_NAME="Name";
    //private static final String KEY_IMAGE_ATTACHMENT="ImageAttachment";
    private static final String KEY_IMAGE_URI ="ImageUri";
    private static final String KEY_LOGIN_METHOD ="LoginMethod";

    private SQLiteDatabase mSQLiteDatabase;
    private Cursor mCursor;

    public DatabaseHelper(Context context, int version) {
        super(context, TABLE_USERS, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_EMAIL + " TEXT PRIMARY KEY," + KEY_PASSWORD + " TEXT,"
                + KEY_NAME +" TEXT,"+KEY_IMAGE_URI +" TEXT,"+KEY_LOGIN_METHOD+ " TEXT"
                + ")";
        Log.d("db helper",CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_USERS);
        // Create tables again
        onCreate(db);
    }
    //create operations
    public void createUser(UserModel user) {
        mSQLiteDatabase=this.getWritableDatabase();
        Log.d("db helper","in create user");
        ContentValues contentValues=new ContentValues();
        contentValues.put(KEY_EMAIL,user.getEmail());
        contentValues.put(KEY_PASSWORD,user.getPassword());
        contentValues.put(KEY_NAME,user.getName());
        contentValues.put(KEY_IMAGE_URI,user.getImageUri().toString());
        contentValues.put(KEY_LOGIN_METHOD,user.getCurrentLoginMethod().name());
        mSQLiteDatabase.insert(TABLE_USERS,null,contentValues);
        mSQLiteDatabase.close();
    }
    //read operations
    public List<UserModel> getAllUsers() {
        String[] columns=new String[]{KEY_EMAIL,KEY_IMAGE_URI,KEY_NAME,KEY_LOGIN_METHOD,KEY_PASSWORD/*,KEY_IMAGE_ATTACHMENT*/};
        String sortOrder=KEY_EMAIL+" ASC";
        ArrayList<UserModel> userModels = null;
        mSQLiteDatabase=this.getReadableDatabase();

        mCursor = mSQLiteDatabase.query(TABLE_USERS,columns,null,null,null,null,sortOrder);
        if(mCursor.moveToNext()){
            do{
                if(userModels==null){
                    userModels=new ArrayList<>();
                }
                String userEmail=mCursor.getString(mCursor.getColumnIndex(KEY_EMAIL));
                String userPassword=mCursor.getString(mCursor.getColumnIndex(KEY_PASSWORD));
                String userName=mCursor.getString(mCursor.getColumnIndex(KEY_NAME));
                String userImage=mCursor.getString(mCursor.getColumnIndex(KEY_IMAGE_URI));
                String loggedMethod=mCursor.getString(mCursor.getColumnIndex(KEY_LOGIN_METHOD));
                    if(userPassword!=null)
                    Log.d("data", userPassword);
                    Log.d("data", userEmail);
                    Log.d("data", userName);
                    if (userImage != null)
                        Log.d("data", userImage);
                    Log.d("data", loggedMethod);

                UserModel.LoginMethod loginMethod= UserModel.LoginMethod.valueOf(loggedMethod);
                userModels.add(new UserModel(userEmail,null,userName, Uri.parse(userImage),loginMethod));
            }while(mCursor.moveToNext());
        }
        closeDB();
        return userModels;
    }

    public boolean checkCredentials(String userEmail,String userPassword){
        String[] columns=new String[]{KEY_PASSWORD};
        String whereSelection = KEY_EMAIL+" = ?";
        String[] whereSelectionArgs=new String[]{userEmail};
        mSQLiteDatabase=this.getReadableDatabase();
        mCursor=mSQLiteDatabase.query(TABLE_USERS,columns,whereSelection,whereSelectionArgs,null,
                null,null);
        if(mCursor.moveToNext()){
            Log.d("status","in move to next");
            String recordedPassword=mCursor.getString(mCursor.getColumnIndex(KEY_PASSWORD));
            Log.d("passwords",recordedPassword+"."+userPassword);
            if(recordedPassword.equals(userPassword)) {
                closeDB();
                return true;
            }
        }
        closeDB();
        return false;
    }
    public UserModel getUserByEmail(String userEmail){
        mSQLiteDatabase=this.getReadableDatabase();
        String[] columns=new String[]{KEY_EMAIL,KEY_PASSWORD,KEY_NAME,KEY_IMAGE_URI,KEY_LOGIN_METHOD};
        String whereSelection = KEY_EMAIL+" = ?";
        String[] whereSelectionArgs=new String[]{userEmail};
        mCursor = mSQLiteDatabase.query(TABLE_USERS,columns,whereSelection,whereSelectionArgs,null,
                null,null);
        if(mCursor.moveToNext()){
            String email=mCursor.getString(mCursor.getColumnIndex(KEY_EMAIL));
            String password=mCursor.getString(mCursor.getColumnIndex(KEY_PASSWORD));
            String userName=mCursor.getString(mCursor.getColumnIndex(KEY_NAME));
            String userImage=mCursor.getString(mCursor.getColumnIndex(KEY_IMAGE_URI));
            String loggedMethod=mCursor.getString(mCursor.getColumnIndex(KEY_LOGIN_METHOD));
            UserModel.LoginMethod loginMethod= UserModel.LoginMethod.valueOf(loggedMethod);
            Log.d("type",loginMethod.getClass().toString());
            closeDB();

            return new UserModel(email,password,userName,Uri.parse(userImage),loginMethod);
        }
        closeDB();
        return null;
    }
    public boolean userExists(String userEmail){
        mSQLiteDatabase=this.getReadableDatabase();
        String[] columns=new String[]{KEY_EMAIL,KEY_PASSWORD};
        String whereSelection = KEY_EMAIL+" = ?";
        String[] whereSelectionArgs=new String[]{userEmail};
        mCursor = mSQLiteDatabase.query(
                TABLE_USERS,
                columns,
                whereSelection,
                whereSelectionArgs,
                null,
                null,
                null
        );
        if(mCursor.getCount()>0){
            closeDB();
            return true;
        }
        else{
            closeDB();
            return false;
        }
    }
    //update operations
    public void updateUser(UserModel user) {
        mSQLiteDatabase=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_NAME,user.getName());
        values.put(KEY_PASSWORD,user.getPassword());
        values.put(KEY_IMAGE_URI,user.getImageUri().toString());
        String[] whereClause=new String[]{user.getEmail()};
        values.put(KEY_PASSWORD,user.getPassword());
        mSQLiteDatabase.update(TABLE_USERS, values,KEY_EMAIL+" = ?",whereClause);
        mSQLiteDatabase.close();
    }

    void closeDB(){
        mCursor.close();
        mSQLiteDatabase.close();
    }


}
