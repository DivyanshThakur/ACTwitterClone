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

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import es.dmoral.toasty.Toasty;

public class LogIn extends AppCompatActivity implements View.OnClickListener {

    // Ui components declaring
    private EditText edtUsernameLA, edtPasswordLA;
    private Button btnSignUpLA, btnLogInLA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        setTitle("Log In");

        //Logging in current user
        if (ParseUser.getCurrentUser() != null) {
            transitionToTwitterUsersActivity();
        }

        // Ui components initializing
        edtUsernameLA  = findViewById(R.id.edtUsernameLA);
        edtPasswordLA = findViewById(R.id.edtPasswordLA);
        btnSignUpLA = findViewById(R.id.btnSignUpLA);
        btnLogInLA = findViewById(R.id.btnLogInLA);

        // Adding onKeyListener
        edtPasswordLA.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(btnLogInLA);
                }

                return false;
            }
        });

        // Adding onClickListener
        btnSignUpLA.setOnClickListener(this);
        btnLogInLA.setOnClickListener(this);

    }

    @Override
    public void onClick(View buttonView) {

        switch (buttonView.getId()) {
            case R.id.btnSignUpLA :
                Intent intent = new Intent(LogIn.this,SignUp.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnLogInLA :

                if (edtUsernameLA.getText().toString().equals("") || edtPasswordLA.getText().toString().equals("")) {
                    Toasty.error(LogIn.this, "Please enter Username/Password", Toast.LENGTH_LONG, true).show();

                } else {
                // showing progress Dialog
                final ProgressDialog dialog = new ProgressDialog(LogIn.this);
                dialog.setMessage("Logging In " + edtUsernameLA.getText().toString());
                dialog.show();

                ParseUser.logInInBackground(edtUsernameLA.getText().toString(), edtPasswordLA.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null && e == null) {
                            transitionToTwitterUsersActivity();
                        } else {
                            Toasty.error(LogIn.this, "Error:" + e.getMessage(), Toast.LENGTH_LONG, true).show();
                        }
                        dialog.dismiss();
                    }
                });
                }

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

        Intent intent = new Intent(LogIn.this,TwitterUsers.class);
        startActivity(intent);
        finish();
    }
}
