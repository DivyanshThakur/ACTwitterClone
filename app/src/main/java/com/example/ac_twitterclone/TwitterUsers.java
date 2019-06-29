package com.example.ac_twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<String> tusers;
    ArrayAdapter arrayAdapter;
    private String followedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);

        setTitle("Twitter Clone");

        // ui initializing
        listView = findViewById(R.id.listView);
        followedUser = "";
        tusers = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(TwitterUsers.this,android.R.layout.simple_list_item_checked,tusers);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(TwitterUsers.this);

        Toasty.success(TwitterUsers.this, "Welcome " + ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_LONG, true).show();

        final TextView txtShowUsers = findViewById(R.id.txtShowUsers);
try {
    ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
    parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

    parseQuery.findInBackground(new FindCallback<ParseUser>() {
        @Override
        public void done(List<ParseUser> users, ParseException e) {
            if (e == null) {
                if (users.size() > 0) {
                    for (ParseUser user : users) {
                        tusers.add(user.getUsername());
                    }

                    listView.setAdapter(arrayAdapter);

                    // checking the followed users at app startup
                    for (String twitterUser : tusers) {

                        if (ParseUser.getCurrentUser().getList("fanOf") != null) {
                            if (ParseUser.getCurrentUser().getList("fanOf").contains(twitterUser)) {
                                followedUser += "\n" + twitterUser;
                                listView.setItemChecked(tusers.indexOf(twitterUser), true);
                                Toasty.info(TwitterUsers.this,ParseUser.getCurrentUser().getUsername() + " is following :" + followedUser,Toasty.LENGTH_LONG,true).show();
                            }
                        }
                    }

                    txtShowUsers.animate().alpha(0).setDuration(1000);
                    listView.setVisibility(View.VISIBLE);
                }
            }
        }
    });
} catch (Exception e) {
    e.printStackTrace();
}

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logoutUserItem :
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Intent intent = new Intent(TwitterUsers.this,SignUp.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                break;
            case R.id.tweetUserItem :
                Intent intent =  new Intent(TwitterUsers.this, SendTweetActivity.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        CheckedTextView checkedTextView = (CheckedTextView) view;

        if (checkedTextView.isChecked()) {
            ParseUser.getCurrentUser().add("fanOf", tusers.get(i));
            Toasty.info(TwitterUsers.this,tusers.get(i) + " is now Followed!",Toasty.LENGTH_SHORT).show();
        } else {
            //removing unfollowed user and adding the followed user list again
            ParseUser.getCurrentUser().getList("fanOf").remove(tusers.get(i));
            List currentUserFanOf = ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf",currentUserFanOf);

            Toasty.info(TwitterUsers.this,tusers.get(i) + " is now Unfollowed!",Toasty.LENGTH_SHORT).show();

        }
        ParseUser.getCurrentUser().saveInBackground();

    }
}
