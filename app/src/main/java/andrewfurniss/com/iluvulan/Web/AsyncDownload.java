package andrewfurniss.com.iluvulan.Web;

import android.os.AsyncTask;

import fr.bmartel.speedtest.SpeedTestSocket;


/**
 * Created by Drew on 2/28/2016.
 */

public class AsyncDownload extends AsyncTask<SpeedTestSocket, Integer, String> {



    @Override
    protected String doInBackground(SpeedTestSocket... params) {
        params[0].startDownload("ipv4.intuxication.testdebit.info", 80,"/fichiers/10Mo.dat");
        return "finished";
    }

}
