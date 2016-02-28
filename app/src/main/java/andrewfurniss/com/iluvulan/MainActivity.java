package andrewfurniss.com.iluvulan;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import andrewfurniss.com.iluvulan.Utils.LanUtils;
import andrewfurniss.com.iluvulan.Web.AsyncScraper;
import andrewfurniss.com.iluvulan.Web.ScrapeListener;

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

public class MainActivity extends Activity implements View.OnClickListener, ScrapeListener{

    private RadioButton rad_School, rad_Home;
    private EditText etxt_Speed;
    private AutoCompleteTextView etxt_Provider, etxt_School;
    private ActionProcessButton btn_Submit;
    private ViewSwitcher v_Switch;
    private TextView txt_Status;
    private boolean button_pressed;
    private String connectType, result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rad_School = (RadioButton) findViewById(R.id.rad_School);
        rad_Home = (RadioButton) findViewById(R.id.rad_Home);
        etxt_Provider = (AutoCompleteTextView) findViewById(R.id.etxt_Provider);
        etxt_School = (AutoCompleteTextView) findViewById(R.id.etxt_School);
        etxt_Speed = (EditText) findViewById(R.id.etxt_Speed);
        v_Switch = (ViewSwitcher) findViewById(R.id.v_Switch);
        btn_Submit = (ActionProcessButton) findViewById(R.id.btn_Submit);
        txt_Status = (TextView) findViewById(R.id.txt_Status);



        v_Switch.setVisibility(View.INVISIBLE);
        btn_Submit.setVisibility(View.INVISIBLE);
        txt_Status.setVisibility(View.INVISIBLE);

        rad_School.setOnClickListener(this);
        rad_Home.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, LanUtils.schools);
        etxt_School.setAdapter(adapter);
        
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
                AsyncScraper scraper = new AsyncScraper();
                scraper.listener = this;
                scraper.execute("https://www.google.com/#q=ramapo+college+twitter");

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
    public void onFinish(String result) {
        this.result = result;
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
    }
}
