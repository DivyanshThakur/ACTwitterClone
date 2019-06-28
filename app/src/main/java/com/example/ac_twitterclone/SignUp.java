package com.example.ac_twitterclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import es.dmoral.toasty.Toasty;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    // Ui components declaring
    private EditText edtEmailSA, edtUsernameSA, edtPasswordSA;
    private Button btnSignUpSA, btnLogInSA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("Sign Up");

        //Logging in current user
        if (ParseUser.getCurrentUser() != null) {
            transitionToTwitterUsersActivity();
        }

        // Ui components initializing
        edtEmailSA = findViewById(R.id.edtEmailSA);
        edtUsernameSA = findViewById(R.id.edtUsernameSA);
        edtPasswordSA = findViewById(R.id.edtPasswordSA);
        btnSignUpSA = findViewById(R.id.btnSignUpSA);
        btnLogInSA  = findViewById(R.id.btnLogInSA);

        // Adding onKeyListener for Password editText
        edtPasswordSA.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(btnSignUpSA);
                }

                return false;
            }
        });

        // Adding OnClickListener
        btnSignUpSA.setOnClickListener(this);
        btnLogInSA.setOnClickListener(this);

    }

    @Override
    public void onClick(View buttonView) {

        switch (buttonView.getId()) {
            case R.id.btnSignUpSA :

                // Signing up new users...
                final ParseUser appUser = new ParseUser();
                appUser.setEmail(edtEmailSA.getText().toString());
                appUser.setUsername(edtUsernameSA.getText().toString());
                appUser.setPassword(edtPasswordSA.getText().toString());

                if (edtEmailSA.getText().toString().equals("") || edtUsernameSA.getText().toString().equals("") || edtPasswordSA.getText().toString().equals("")) {
                    Toasty.error(SignUp.this, "Please fill the above details", Toast.LENGTH_LONG, true).show();

                } else {

                    // Adding Progress Dialog
                    final ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
                    progressDialog.setMessage("Signing Up " + appUser.getUsername());
                    progressDialog.show();


                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                transitionToTwitterUsersActivity();

                            } else {
                                Toasty.error(SignUp.this, "Error:" + e.getMessage(), Toast.LENGTH_LONG, true).show();
                            }
                            // removing progress dialog
                            progressDialog.dismiss();

                        }
                    });
                }

                break;
            case R.id.btnLogInSA :

                Intent intent = new Intent(SignUp.this,LogIn.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void rootLayoutTapped(View view) {
        try {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void transitionToTwitterUsersActivity() {

        Intent intent = new Intent(SignUp.this,TwitterUsers.class);
        startActivity(intent);
        finish();
    }
}
