package com.example.h8951.android_http_request_test;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Eurybus on 12.11.2016.
 */

public class PutUploadUrl {


    /**
     *
     * @param myurl RESTin controllerin url
     * @param nick Avain: Käyttäjän nimimerkki
     * @param nick_actual Avaimen arvo
     * @param key Muutettavan arvon avain
     * @param value Muutettava arvo
     * @throws IOException
     */
    public void uploadUrl(String myurl, String nick, String nick_actual, String key, String value) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("PUT");

            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-type", "application/json");

            String json ="";

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(nick,nick_actual);
            jsonObject.put(key,value);
            json = jsonObject.toString();

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(json);
            out.close();
            connection.getInputStream();


        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (ProtocolException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        } finally {
            // Varmistetaan, että InputStream suljetaan
            if (is != null) {
                is.close();
            }
        }
    }

}
