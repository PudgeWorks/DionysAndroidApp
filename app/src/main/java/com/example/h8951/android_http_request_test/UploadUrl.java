package com.example.h8951.android_http_request_test;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Eurybus on 12.11.2016.
 * Legacy luokka ei enään käyttöä
 */

public class UploadUrl {

    /**
     *
     * @param myurl RESTin controllerin url
     * @param key kenttä, jonka arvoa halutaan muokata
     * @param value kentän arvo
     * @return Pitäisi palautta HTTP status code
     * @throws IOException
     */
    public String uploadUrl(String myurl, String key, String value) throws IOException {
        HttpURLConnection conn = null;
        OutputStream outputPost;
        String response = "";
        try {
            URL url = new URL(myurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setRequestProperty(key,value);
            conn.setDoOutput(true);
            // Starts the query
            conn.connect();


            outputPost = new BufferedOutputStream(conn.getOutputStream());
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(outputPost,"UTF-8"));
            //writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            outputPost.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }


        }
        catch (MalformedURLException ex){

        }
        catch (SocketTimeoutException ex){

        }
        catch (IOException ex){

        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return  "ebin";
    }
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}
