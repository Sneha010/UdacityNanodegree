package com.udacity.myappportfolio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.udacity.myappportfolio.util.MyUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
    }

    @OnClick(R.id.btn_spotifyStreamer)
    public void spotifyStreamerClicked(){
        MyUtil.displayCustomToast(MainActivity.this , getResources().getString(R.string.landing_button1_toast));
    }

    @OnClick(R.id.btn_scoresApp)
    public void scoresAppClicked(){
        MyUtil.displayCustomToast(MainActivity.this , getResources().getString(R.string.landing_button2_toast));
    }

    @OnClick(R.id.btn_libraryApp)
    public void libraryAppClicked(){
        MyUtil.displayCustomToast(MainActivity.this , getResources().getString(R.string.landing_button3_toast));
    }

    @OnClick(R.id.btn_buildItBigger)
    public void buildItBiggerClicked(){
        MyUtil.displayCustomToast(MainActivity.this , getResources().getString(R.string.landing_button4_toast));
    }

    @OnClick(R.id.btn_xyzReader)
    public void xyzReaderClicked(){
        MyUtil.displayCustomToast(MainActivity.this , getResources().getString(R.string.landing_button5_toast));
    }

    @OnClick(R.id.btn_capstone)
    public void capstoneClicked(){
        MyUtil.displayCustomToast(MainActivity.this , getResources().getString(R.string.landing_button6_toast));
    }




}
