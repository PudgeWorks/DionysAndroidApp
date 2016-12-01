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
    // an InputStream. Finally, the InputStream is converted into a JSON and parsed in to a local collection of objects.
    public class AsyncPutData extends AsyncTask< String, Void, String> {

        String result = "";
        protected MainActivity context;


        public AsyncPutData(MainActivity context){
            this.context = context;

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                PutUploadUrl upload = new PutUploadUrl();
                upload.uploadUrl(params[0],params[1],params[2],params[3],params[4]);
            }catch (IOException ex){
                result = ex.getMessage();
            }
            finally {
                result = "PUT was succesful";
            }
            return result;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(final String result) {
            Log.d("AsyncPutData", result);
        }
    }

