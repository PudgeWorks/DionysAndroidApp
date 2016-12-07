package com.example.h8951.android_http_request_test;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eurybus on 12.11.2016.
 */

/**
 * Threadi tietojen lähetykseen RESTille
 */
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

