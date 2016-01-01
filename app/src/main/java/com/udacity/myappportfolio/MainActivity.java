package com.udacity.myappportfolio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.udacity.myappportfolio.util.MyUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

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

    public void onlandingButtonClick(View v){

        switch (v.getId()){
            case R.id.btn_spotifyStreamer:
                displayToast(getResources().getString(R.string.landing_button1_toast));
                break;

            case R.id.btn_scoresApp:
                displayToast(getResources().getString(R.string.landing_button2_toast));
                break;

            case R.id.btn_libraryApp:
                displayToast(getResources().getString(R.string.landing_button3_toast));
                break;

            case R.id.btn_buildItBigger:
                displayToast(getResources().getString(R.string.landing_button4_toast));

                break;

            case R.id.btn_xyzReader:
                displayToast(getResources().getString(R.string.landing_button5_toast));
                break;


            case R.id.btn_capstone:
                displayToast(getResources().getString(R.string.landing_button6_toast));
                break;

            default:

        }

    }


    public void displayToast(String message){
        MyUtil.displayCustomToast(MainActivity.this , message);
    }


}
