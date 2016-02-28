package andrewfurniss.com.iluvulan.Web;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * Created by Drew on 2/28/2016.
 */
public class AsyncScraper extends AsyncTask<String, Integer, String> {
    public ScrapeListener listener;
    @Override
    protected String doInBackground(String... params) {
        String title = null;
        Document doc = null;

        try {
            InputStream is = new URL(params[0]).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);

            return jsonText;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    @Override
    public void onPostExecute(String result)
    {
        listener.onFinish(result);
    }

}
