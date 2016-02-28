package andrewfurniss.com.iluvulan;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ViewSwitcher;


public class MainActivity extends Activity implements View.OnClickListener{

    private RadioButton rad_School, rad_Home;
    private EditText etxt_Provider, etxt_Speed, etxt_School;
    private ViewSwitcher v_Switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rad_School = (RadioButton) findViewById(R.id.rad_School);
        rad_Home = (RadioButton) findViewById(R.id.rad_Home);
        etxt_Provider = (EditText) findViewById(R.id.etxt_Provider);
        etxt_School = (EditText) findViewById(R.id.etxt_School);
        etxt_Speed = (EditText) findViewById(R.id.etxt_Speed);
        v_Switch = (ViewSwitcher) findViewById(R.id.v_Switch);

        v_Switch.setVisibility(View.INVISIBLE);

        rad_School.setOnClickListener(this);
        rad_Home.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v_Switch.getVisibility() == View.INVISIBLE)
        {
            v_Switch.setVisibility(View.VISIBLE);
        }

        if(rad_Home.isChecked())
        {
            v_Switch.setDisplayedChild(1);
        } else
        {
            v_Switch.setDisplayedChild(0);
        }
    }

    private void crossfade() {

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        mContentView.setAlpha(0f);
        mContentView.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        mContentView.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        mLoadingView.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoadingView.setVisibility(View.GONE);
                    }
                });
    }
}
