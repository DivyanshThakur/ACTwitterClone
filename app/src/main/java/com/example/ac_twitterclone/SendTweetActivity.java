package com.example.ac_twitterclone;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class SendTweetActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtSendTweet;
    private FloatingActionButton floatBtnTweet, viewTweetsFbtn;

    private ListView viewTweetsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);
        setTitle("Tweets");

        edtSendTweet = findViewById(R.id.edtSendTweet);
        floatBtnTweet = findViewById(R.id.floatingBtnTweet);
        viewTweetsFbtn = findViewById(R.id.viewTweetFbtn);
        viewTweetsListView = findViewById(R.id.viewTweetsListView);

        viewTweetsFbtn.setOnClickListener(SendTweetActivity.this);

    }

    public void sendTweet(View view) {

        //receiving the tweet from editText
        ParseObject parseObject = new ParseObject("MyTweet");
        parseObject.put("tweet", edtSendTweet.getText().toString());
        parseObject.put("user", ParseUser.getCurrentUser().getUsername());

        // showing the progress dialog
        final ProgressDialog dialog = new ProgressDialog(SendTweetActivity.this);
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
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        final ArrayList<HashMap<String, String>> tweetList = new ArrayList<>();
        final SimpleAdapter adapter = new SimpleAdapter(SendTweetActivity.this,tweetList,android.R.layout.simple_list_item_2, new String[] {"tweetUserName", "tweetValue"} , new int[] {android.R.id.text1, android.R.id.text2});

        try {

            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweet");
            parseQuery.whereContainedIn("user", ParseUser.getCurrentUser().getList("fanOf"));

            // adding Progress dialog
            final ProgressDialog dialog = new ProgressDialog(SendTweetActivity.this);
            dialog.setMessage("Loading...");
            dialog.show();

            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() >0 && e == null) {
                        for (ParseObject tweetObject : objects) {
                            HashMap<String, String> userTweet = new HashMap<>();
                            userTweet.put("tweetUserName", tweetObject.getString("user"));
                            userTweet.put("tweetValue", tweetObject.getString("tweet"));
                            tweetList.add(userTweet);
                        }

                        viewTweetsListView.setAdapter(adapter);
                    }
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
