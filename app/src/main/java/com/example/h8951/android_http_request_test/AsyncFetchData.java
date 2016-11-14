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
    public class AsyncFetchData extends AsyncTask< String, Void, String> {

        private String debugString = "nada";
        private String debugString2 = "nudu";
        private String debugString3 = "nidi";
        private String debugString4 = "nodo";
        private String debugString5 = "doro";
        boolean isVenues;
        Exception ex;
        private JSONConverter converter = new JSONConverter();
        private String JSONData;
        private TextView myView;
        DownloadUrl testi = new DownloadUrl();
        private List<Venue> localVenues = new ArrayList<>();
        private List<User> localUsers = new ArrayList<>();
        JSONResponse connectorToMainActivity;
        protected MainActivity context;


        public AsyncFetchData(MainActivity context){

            connectorToMainActivity = context;
            this.context = context;
            myView =(TextView) context.findViewById(R.id.myText);
        }

        @Override
        protected String doInBackground(String... params) {
            // params[0] is the url, params[1] is the conversion type, user/venue
            // params comes from the execute() call: params[0] is the url.
            try {
                JSONData = testi.downloadUrl(params[0]);
                debugString = JSONData;

                if(params[1] == "venues"){
                    isVenues = true;
                    debugString3 = "Calling converto to Venues from doinbackground...";
                    localVenues = converter.convertJSONToVenue(JSONData);
                } else if (params[1] == "users"){
                    isVenues = false;
                    debugString3 = "Calling converto to Users from doinbackground...";
                    localUsers = converter.convertJSONToUser(JSONData);
                }

                debugString2 = Integer.toString(localVenues.size());
                debugString4 = Integer.toString(localUsers.size());

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
            Log.d("localUsersin koko: ", debugString4 );
            Log.d("value of isVenues: ", "" + isVenues);

            if(localVenues == null){
                Log.d("ERROR:", "local venues is empty before interface invocation!");
            } else {
                Log.d("SUCCESS:", "local venues is filled and going to interface invocation!");
            }

            if(isVenues) {
                connectorToMainActivity.VenuesResponse(localVenues);
            } else {
                connectorToMainActivity.UsersResponse(localUsers);
            }
            context.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    myView.setText(result);

                }
            });
        }
    }

