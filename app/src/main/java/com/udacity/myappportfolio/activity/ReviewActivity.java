package com.udacity.myappportfolio.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.udacity.myappportfolio.R;

public class ReviewActivity extends AppCompatActivity {

    private String movieName, reviewContent, reviewAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_activity_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent() != null && getIntent().hasExtra("movieName"))
            movieName = getIntent().getExtras().getString("movieName");

        if (getIntent() != null && getIntent().hasExtra("authorName"))
            reviewAuthor = getIntent().getExtras().getString("authorName");

        if (getIntent() != null && getIntent().hasExtra("reviewContent"))
            reviewContent = getIntent().getExtras().getString("reviewContent");


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(movieName);

        TextView tvAuthor = (TextView) findViewById(R.id.tv_Author);
        TextView tvReview = (TextView) findViewById(R.id.tv_Review);

        tvAuthor.setText(reviewAuthor);
        tvReview.setText(reviewContent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
