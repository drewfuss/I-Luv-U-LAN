package andrewfurniss.com.iluvulan.Web;

import android.os.AsyncTask;

/**
 * Created by Drew on 2/28/2016.
 */
public class AsyncScraper extends AsyncTask<String, Integer, String> {
    public ScrapeListener listener;
    @Override
    protected String doInBackground(String... params) {
        String title = null;


        title = "hello";

        return title;
    }
    @Override
    protected void onPostExecute(String result)
    {
        listener.onFinish(result);
    }
}
