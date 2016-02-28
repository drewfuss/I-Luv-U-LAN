package andrewfurniss.com.iluvulan;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.dd.processbutton.iml.ActionProcessButton;
import com.github.lzyzsd.circleprogress.CircleProgress;



import andrewfurniss.com.iluvulan.Utils.LanUtils;
import andrewfurniss.com.iluvulan.Web.AsyncDownload;
import andrewfurniss.com.iluvulan.Web.DownloadListener;
import fr.bmartel.speedtest.ISpeedTestListener;
import fr.bmartel.speedtest.SpeedTestSocket;
import io.fabric.sdk.android.Fabric;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.TwitterAuthConfig;



    /*
    The MIT License (MIT)

    Copyright (c) 2014 Danylyk Dmytro

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
     */

public class MainActivity extends Activity implements View.OnClickListener, DownloadListener{

    private RadioButton rad_School, rad_Home;
    private EditText etxt_Speed;
    private AutoCompleteTextView etxt_Provider, etxt_School;
    private ActionProcessButton btn_Submit, btn_Complain;
    private ViewSwitcher v_Switch, v_SwitchLoading;
    private TextView txt_Status, txt_Down;
    private boolean button_pressed, finished_loading;
    private String connectType, result;
    private CircleProgress circle_Progress;
    private Handler handler;
    private Runnable r, receiver;
    private static final String TWITTER_KEY = "bKUTQJvhvhPfB3T8Ebs52go9B";
    private static final String TWITTER_SECRET = "qCEg3JKsMLRLuzFFt6Ue0rbCgl2zZFqE0IQ82MIn1v1vmZdhUS";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());


        rad_School = (RadioButton) findViewById(R.id.rad_School);
        rad_Home = (RadioButton) findViewById(R.id.rad_Home);
        etxt_Provider = (AutoCompleteTextView) findViewById(R.id.etxt_Provider);
        etxt_School = (AutoCompleteTextView) findViewById(R.id.etxt_School);
        etxt_Speed = (EditText) findViewById(R.id.etxt_Speed);
        v_Switch = (ViewSwitcher) findViewById(R.id.v_Switch);
        v_SwitchLoading = (ViewSwitcher) findViewById(R.id.v_SwitchLoading);
        btn_Submit = (ActionProcessButton) findViewById(R.id.btn_Submit);
        btn_Complain = (ActionProcessButton) findViewById(R.id.btn_Tweet);
        txt_Status = (TextView) findViewById(R.id.txt_Status);
        circle_Progress = (CircleProgress) findViewById(R.id.circle_progress);
        txt_Down = (TextView) findViewById(R.id.txt_Down);

        btn_Complain.setVisibility(View.INVISIBLE);
        circle_Progress.setVisibility(View.INVISIBLE);
        circle_Progress.setProgress(0);
        circle_Progress.setTextColor(Color.parseColor("#ffc107"));
        circle_Progress.setUnfinishedColor(Color.parseColor("#ffc107"));
        circle_Progress.setFinishedColor(Color.parseColor("#283339"));

        handler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                if(circle_Progress.getProgress() < 75)
                {
                    circle_Progress.setProgress(circle_Progress.getProgress()+1);
                    handler.postDelayed(this, 50);
                }
                else if(circle_Progress.getProgress() == 100)
                {
                    v_SwitchLoading.showNext();
                    handler.post(receiver);
                }
                else if(finished_loading)
                {
                    finished_loading = false;
                    circle_Progress.setProgress(100);
                    handler.postDelayed(this, 50);
                }

            }
        };
        receiver = new Runnable() {
            @Override
            public void run() {
                if(LanUtils.DOWN_SET)
                {
                    txt_Down.setText(LanUtils.DOWN_SPEED + " mbps");
                    btn_Complain.setVisibility(View.VISIBLE);
                }
                else
                {
                    handler.postDelayed(this, 1000);
                }
            }
        };


        v_Switch.setVisibility(View.INVISIBLE);
        btn_Submit.setVisibility(View.INVISIBLE);
        txt_Status.setVisibility(View.INVISIBLE);

        rad_School.setOnClickListener(this);
        rad_Home.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, LanUtils.schools);
        etxt_School.setAdapter(adapter);

        btn_Complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send tweet
                TweetComposer.Builder builder = new TweetComposer.Builder(MainActivity.this).text("How bout this LAN, right?");
                builder.show();
            }
        });
        
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_pressed = true;
                hideKeyboard();
                btn_Submit.setProgress(10);
                if(rad_Home.isChecked()) {
                    if(etxt_Provider.getText() != null && etxt_Speed.getText() != null)
                    {
                        if(!etxt_Provider.getText().toString().equals("") && !etxt_Speed.getText().toString().equals(""))
                        {
                            connect();
                        } else {
                            btn_Submit.setProgress(0);
                            Toast.makeText(getApplicationContext(), "Please enter test criteria", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        btn_Submit.setProgress(0);
                        Toast.makeText(getApplicationContext(), "Please enter test criteria", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(rad_School.isChecked())
                {
                      if(etxt_School.getText() != null)
                      {
                          if(!etxt_School.getText().toString().equals(""))
                          {
                              connect();
                          } else{
                              btn_Submit.setProgress(0);
                              Toast.makeText(getApplicationContext(), "Please enter test criteria", Toast.LENGTH_SHORT).show();

                          }
                      } else {
                          btn_Submit.setProgress(0);
                          Toast.makeText(getApplicationContext(), "Please enter test criteria", Toast.LENGTH_SHORT).show();
                      }
                }

            }
        });

    }

    @Override
    public void onClick(View v) {

        if(v_Switch.getVisibility() == View.INVISIBLE)
        {
            v_Switch.setVisibility(View.VISIBLE);
            btn_Submit.setVisibility(View.VISIBLE);
        }

        if(rad_Home.isChecked())
        {
            etxt_Provider.setText(null);
            etxt_Speed.setText(null);
            v_Switch.setDisplayedChild(1);
        } else
        {
            etxt_School.setText(null);
            v_Switch.setDisplayedChild(0);
        }
    }

    @Override
    public void onBackPressed()
    {
        if(button_pressed)
        {
            btn_Submit.setProgress(0);
            txt_Status.setVisibility(View.INVISIBLE);
            button_pressed = false;
        }
        else
        {
            super.onBackPressed();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
         if(activeNetworkInfo != null && activeNetworkInfo.isConnected())
         {
             NetworkInfo wifiNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
             NetworkInfo mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
             if (mobileNetwork != null && mobileNetwork.isConnected())
             {
                 connectType = "mobile";
             }
             else if (wifiNetwork != null && wifiNetwork.isConnected())
             {
                 connectType = "wifi";
             }
             return true;
         }
        return false;
    }
    private void connect()
    {
        if(isNetworkAvailable())
        {
            btn_Submit.setProgress(50);
            try {
                Thread.sleep(2000);
                btn_Submit.setProgress(100);
                txt_Status.setVisibility(View.VISIBLE);
                if(connectType.equals("wifi"))
                {
                    //AsyncScraper scraper = new AsyncScraper();
                    //scraper.listener = this;
                   // scraper.execute("https://www.google.com/#q=ramapo+college+twitter");
                      AsyncDownload dl = new AsyncDownload();
                      SpeedTestSocket socket = new SpeedTestSocket();
                      dl.listener = this;
                      socket.addSpeedTestListener(new ISpeedTestListener() {
                          @Override
                          public void onDownloadPacketsReceived(int i, float v, float v2) {
                              float total = v/1048576;
                              LanUtils.DOWN_SPEED = total;
                              LanUtils.setSpeed();
                          }

                          @Override
                          public void onDownloadProgress(int i) {

                          }

                          @Override
                          public void onDownloadError(int i, String s) {

                          }

                          @Override
                          public void onUploadPacketsReceived(int i, float v, float v2) {

                          }

                          @Override
                          public void onUploadError(int i, String s) {

                          }

                          @Override
                          public void onUploadProgress(int i) {

                          }
                      });
                      circle_Progress.setVisibility(View.VISIBLE);
                      circle_Progress.animate();
                      handler.postDelayed(r, 200);
                      dl.execute(socket);
                } else
                {
                    Toast.makeText(getApplicationContext(), "Please connect to wifi", Toast.LENGTH_SHORT).show();
                }

            } catch (InterruptedException e) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            btn_Submit.setProgress(-1);
        }
    }

    private void hideKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public void onFinishDownloading() {
        finished_loading = true;
        txt_Status.setVisibility(View.INVISIBLE);
        txt_Down.setText(LanUtils.DOWN_SPEED + " mbps");
    }

}
