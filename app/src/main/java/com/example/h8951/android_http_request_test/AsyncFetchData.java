package com.example.h8951.android_http_request_test;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aleksi on 12.11.2016.
 */
    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    public class AsyncFetchData extends AsyncTask<String, Void, String> {

        private String debugString = "nada";
        private String debugString2 = "nudu";
        Exception ex;
        private JSONConverter converter = new JSONConverter();
        private String JSONData;
        private TextView myView;
        DownloadUrl testi = new DownloadUrl();
        private List<Venue> localVenues = new ArrayList<>();
        VenuesResponse connectorToMainActivity;
        protected MainActivity context;


        public AsyncFetchData(MainActivity context){

            connectorToMainActivity = context;
            this.context = context;
            myView =(TextView) context.findViewById(R.id.myText);
        }

        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                JSONData = testi.downloadUrl(urls[0]);
                debugString = JSONData;
                localVenues = converter.convertJSONToVenue(JSONData);
                debugString2 = Integer.toString(localVenues.size());

                return "JSON converted successfully";

            } catch (IOException e) {

                return "Unable to retrieve web page. URL may be invalid.";

            } catch (JSONException e) {

                return "JSON fumbled in conversion.";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(final String result) {

            Log.d("JSON: ", debugString );
            Log.d("localVenuesin koko: ", debugString2 );

            if(localVenues == null){
                Log.d("ERROR:", "local venues is empty before interface invocation!");
            } else {
                Log.d("SUCCESS:", "local venues is filled and going to interface invocation!");
            }

            connectorToMainActivity.VenuesResponse(localVenues);
            context.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    myView.setText(result);

                }
            });
        }
    }

