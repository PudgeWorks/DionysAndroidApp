package com.example.h8951.android_http_request_test;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    private EditText urlText;
    private TextView textView;
    String stringUrl = "http://dionys-rest.azurewebsites.net/api/users";
    http_request.DownloadWebpageTask DLWebPageTask;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.myText);

        //ensin tehdään ulommasta luokasta olio (HTTPRequest)
        //sitten sisemmästä luokasta olio
        // kutsumalla new ulomman olion alta.

        http_request HTTPRequest = new http_request(this);
        http_request.DownloadWebpageTask DLWebPageTask = HTTPRequest.new DownloadWebpageTask();

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("Nettiyhteys löytyy.", "tööt");
            DLWebPageTask.execute(stringUrl);
        } else {
            textView.setText("No network connection available.");
        }
    }
}