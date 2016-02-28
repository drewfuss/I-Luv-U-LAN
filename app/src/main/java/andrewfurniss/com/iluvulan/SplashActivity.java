package andrewfurniss.com.iluvulan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import andrewfurniss.com.iluvulan.Utils.LanUtils;


public class SplashActivity extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "bKUTQJvhvhPfB3T8Ebs52go9B ";
    private static final String TWITTER_SECRET = "qCEg3JKsMLRLuzFFt6Ue0rbCgl2zZFqE0IQ82MIn1v1vmZdhUS ";


    private AnimatedCircleLoadingView loadingView;
    private BufferedReader reader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_splash);
        final Handler handler = new Handler();
        final Runnable exit = new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        };
        Runnable r = new Runnable() {
            @Override
            public void run() {
                loadingView.stopOk();
                handler.postDelayed(exit, 3000);
            }
        };

        loadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
        loadingView.startIndeterminate();

        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("school_list.txt")));
            String school_rep = reader.readLine();
            if(school_rep != null)
            {
                LanUtils.schools = school_rep.split(",");
                if(LanUtils.schools != null)
                {
                    handler.postDelayed(r, 3000);
                }
                else {
                    loadingView.stopFailure();
                }
            }
            else{
                loadingView.stopFailure();
            }
        } catch (IOException e) {
            loadingView.stopFailure();
        }
    }


}
