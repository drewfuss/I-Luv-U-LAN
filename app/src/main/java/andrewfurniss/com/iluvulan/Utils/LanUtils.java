package andrewfurniss.com.iluvulan.Utils;

import android.util.Log;
import android.widget.Toast;

import andrewfurniss.com.iluvulan.Web.DownloadListener;

/**
 * Created by Drew on 2/27/2016.
 */
public class LanUtils  {

    public static String[] schools;
    public static String GOOGLE_KEY = "AIzaSyDnU4S3rH_O1VtOseojxnu6f5EUu-KslG0";
    public static float DOWN_SPEED;
    public DownloadListener listener;
    public static boolean DOWN_SET = false;
    public static int PROGRESS = 0;

    public static void setSpeed()
    {
        DOWN_SET = true;
    }
}
