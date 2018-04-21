package com.example.nouran.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    /**
     * URL to query the USGS dataset for earthquake information
     */
    private static final String USGS_REQUEST_URL =
            "http://samples.openweathermap.org/data/2.5/forecast/daily?zip=11728&appid=7a90b1e7fc078221a88efe3d486c2f21";
    ImageView imglocation;
    TextView location, upday, temperature;
    ArrayList<Event> newdata = new ArrayList<>();
    MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();

        imglocation = findViewById(R.id.imglocation);
        location = findViewById(R.id.location);

        upday = findViewById(R.id.upday);
        upday.setText(today.month + "");
        temperature = findViewById(R.id.temperature);

        RecyclerView myrecycle = findViewById(R.id.myrecycle);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this,
                LinearLayoutManager.HORIZONTAL, false);
        myrecycle.setLayoutManager(layoutManager);

        adapter = new MyRecyclerViewAdapter(newdata);
        myrecycle.setAdapter(adapter);
        // Kick off an {@link AsyncTask} to perform the network request
        TsunamiAsyncTask task = new TsunamiAsyncTask();
        task.execute();

    }

    //
//    /**
//     * Update the screen to display information from the given {@link Event}.
//     */
    private void updateUi(ArrayList<Event> earthquake) {


        newdata.addAll(earthquake);
        adapter.notifyDataSetChanged();
    }

    /**
     * Returns a formatted date and time string for when the earthquake happened.
     */
    private String getDateString(long timeInMilliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy 'at' HH:mm:ss z");
        return formatter.format(timeInMilliseconds);

    }

    /**
     * Return the display string for whether or not there was a tsunami alert for an earthquake.
     */


    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the first earthquake in the response.
     */
    private class TsunamiAsyncTask extends AsyncTask<URL, Void, ArrayList<Event>> {

        @Override
        protected ArrayList<Event> doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(USGS_REQUEST_URL);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                // TODO Handle the IOException
            }

            // Extract relevant fields from the JSON response and create an {@link Event} object
            ArrayList<Event> earthquake = extractFeatureFromJson(jsonResponse);

            // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
            return earthquake;
        }

        /**
         * Update the screen with the given earthquake (which was the result of the
         * {@link TsunamiAsyncTask}).
         */
        @Override
        protected void onPostExecute(ArrayList<Event> earthquake) {
            if (earthquake == null) {
                return;
            }

            updateUi(earthquake);
        }

        /**
         * Returns new URL object from the given string URL.
         */
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } catch (IOException e) {
                // TODO: Handle the exception
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        /**
         * Return an {@link Event} object by parsing out information
         * about the first earthquake from the input earthquakeJSON string.
         */

        private ArrayList<Event> extractFeatureFromJson(String earthquakeJSON) {
//            Log.d("hazem",earthquakeJSON);
            try {
                //the main object
                JSONObject res = new JSONObject(earthquakeJSON);


                JSONObject city = res.getJSONObject("city");
                String name = city.getString("name");


                JSONArray list = res.getJSONArray("list");

                ArrayList<Event> data = new ArrayList<>();
                for (int i = 0; i < list.length(); i++) {
                    JSONObject listObj = list.getJSONObject(i);

                    long date = listObj.getLong("dt");


                    JSONObject temp = listObj.getJSONObject("temp");

                    int min = temp.getInt("min");


                    int max = temp.getInt("max");


                    double day = temp.getDouble("day");

                    JSONArray weather = listObj.getJSONArray("weather");

                    JSONObject weatherObj = weather.getJSONObject(0);

                    String main = weatherObj.getString("main");

                    int icon = weatherObj.getInt("icon");


                    Event event = new Event(date, min, max, main, name, icon, day);
                    event.getMax();
                    event.getMin();


                    data.add(event);


                }
                return data;


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


    }
}

//}
