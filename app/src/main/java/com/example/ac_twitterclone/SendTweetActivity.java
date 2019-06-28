package com.example.ac_twitterclone;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import es.dmoral.toasty.Toasty;

public class SendTweetActivity extends AppCompatActivity {

    private EditText edtSendTweet;
    private FloatingActionButton floatBtnTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);

        edtSendTweet = findViewById(R.id.edtSendTweet);
        floatBtnTweet = findViewById(R.id.floatingBtnTweet);
    }

    public void sendTweet(View view) {

        //receiving the tweet from editText
        ParseObject parseObject = new ParseObject("myTweet");
        parseObject.put("tweet", edtSendTweet.getText().toString());
        parseObject.put("username", ParseUser.getCurrentUser().getUsername());

        // showing the progress dialog
        ProgressDialog dialog = new ProgressDialog(SendTweetActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();

        // sending data to parse server
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toasty.success(SendTweetActivity.this,ParseUser.getCurrentUser().getUsername() +"'s Tweet (" + edtSendTweet.getText().toString() + ") is saved!!!",Toasty.LENGTH_LONG,true).show();
                } else {
                    Toasty.error(SendTweetActivity.this,"Error : " + e.getMessage(),Toasty.LENGTH_LONG,true).show();
                }
            }
        });


    }
}
