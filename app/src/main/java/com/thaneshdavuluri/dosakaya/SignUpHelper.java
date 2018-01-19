package com.thaneshdavuluri.dosakaya;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by thanesh.davuluri on 5/29/2017.
 */

public class SignUpHelper {
    private DatabaseHelper mDatabaseHelper;
    Context mCurrentContext;

    public SignUpHelper(Context context) {
        mDatabaseHelper = new DatabaseHelper(context, 1);
        mCurrentContext = context;
    }

    public boolean validate(View view) {
        View parentView = view.getRootView();
        boolean isValid = true;
        TextInputLayout textInputLayout;
        EditText name = (EditText) parentView.findViewById(R.id.sign_up_name);
        String nameEntered = name.getText().toString().trim();
        EditText email = (EditText) parentView.findViewById(R.id.sign_up_email);
        String emailEntered = email.getText().toString().trim();
        EditText password = (EditText) parentView.findViewById(R.id.sign_up_password);
        String passwordEntered = password.getText().toString().trim();
        EditText reEnteredPassword = (EditText) parentView.findViewById(R.id.sign_up_password);
        String passwordReEntered = reEnteredPassword.getText().toString().trim();

        if (nameEntered.length() == 0) {
            textInputLayout = (TextInputLayout) parentView.findViewById(R.id.textInputLayoutName);
            textInputLayout.setError("Invalid Name");
            isValid = false;
        }
        if (!isValidEmail(emailEntered)) {
            textInputLayout = (TextInputLayout) parentView.findViewById(R.id.textInputLayoutEmail);
            textInputLayout.setError("Invalid Email Id");
            isValid = false;

        }
        if (isValidPassword(passwordEntered) != null) {
            textInputLayout = (TextInputLayout) parentView.findViewById(R.id.textInputLayoutPassword);
            textInputLayout.setError(isValidPassword(passwordEntered));
            isValid = false;
        }
        if (!passwordReEntered.equals(passwordEntered)) {
            textInputLayout = (TextInputLayout) parentView.findViewById(R.id.textInputLayoutConfirmPassword);
            textInputLayout.setError("Password didn't match");
            isValid = false;
        }
        return isValid;
    }

    public boolean signUp(UserModel userModel) {
        if (mDatabaseHelper.userExists(userModel.getEmail())) {
            Toast.makeText(mCurrentContext, "User Already Exists!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            mDatabaseHelper.createUser(userModel);
            return true;
        }
    }

    String isValidPassword(String password) {
        if (password == null) {
            return "Password Empty";
        } else {
            if (password.length() < 8) {
                return "Password should at least be 8 characters long";
            }
        }
        return null;
    }

    boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}