package andrewfurniss.com.iluvulan.Web;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Drew on 2/28/2016.
 */
public class AsyncScraper extends AsyncTask<String, Integer, String> {
    public ScrapeListener listener;
    @Override
    protected String doInBackground(String... params) {
        Document doc = null;
        String title = null;
        try {
            doc = Jsoup.connect(params[0]).get();
            title = doc.title();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return title;
    }
    @Override
    protected void onPostExecute(String result)
    {
        listener.onFinish(result);
    }
}
