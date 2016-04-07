package com.fit.vedads.appstore.helper.url_connection;

import android.util.Log;

import com.fit.vedads.appstore.helper.MyGson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Created by vedad on 29.3.2015.
 */
public class HttpManager {
    public static <T> MojApiRezultat<T> get(String url,Class<T> outputType,NameValuePair... inputParams)
    {
        String urlParam = URLEncodedUtils.format(Arrays.asList(inputParams),"utf-8");
        HttpGet httpGet = new HttpGet(url + "?" +urlParam);

        DefaultHttpClient client = new DefaultHttpClient();
        final MojApiRezultat<T> rezultat = new MojApiRezultat<>();
        try {
            HttpResponse response = client.execute(httpGet);
            InputStream stream = response.getEntity().getContent();

            String strJson = convertStreamToString(stream);


            T x = MyGson.build().fromJson(strJson, outputType);
            rezultat.isError = false;
            rezultat.value = x;

        }catch (IOException e)
        {
            Log.e("HttpManager", e.getMessage());
            rezultat.isError = true;
            rezultat.value = null;
            rezultat.errorMessage = e.getMessage();
        }
        return rezultat;
    }


    public static <T> MojApiRezultat<T> post(String urlStr, Class<T> outputType, final Object inputObject)
    {
        String strJsonInput = MyGson.build().toJson(inputObject);

        MojApiRezultat<T> rezultat = new MojApiRezultat<>();
        try
        {
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");

            //add request header
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Content-type", "application/json");
            urlConnection.setRequestProperty("Accept-Encoding", "gzip");

            // Send post request
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.writeBytes(strJsonInput);
            wr.flush();
            wr.close();

            // call
            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpStatus.SC_OK)
            {
                InputStream InputStream = new BufferedInputStream(urlConnection.getInputStream());

                String sHeaderValue = urlConnection.getHeaderField("Content-Encoding");
                if (sHeaderValue != null && sHeaderValue.equalsIgnoreCase("gzip"))
                {
                    InputStream = new GZIPInputStream(InputStream);
                }

                String strJson = convertStreamToString(InputStream);
                T t = MyGson.build().fromJson(strJson, outputType);
                rezultat.value = t;

                Log.v("HttpManager", strJson);

            }else{
                Log.w("HttpManager", "Response code:"+ responseCode);
            }
            urlConnection.disconnect();
        }
        catch (Exception e)
        {
            Log.e("HttpManager", e.getMessage());
            rezultat.errorMessage = e.getMessage();
            rezultat.isError = true;
        }
        return rezultat;
    }

    public static String convertStreamToString(InputStream inputStream) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;

        while((line = bufferedReader.readLine()) != null)
        {
            stringBuilder.append(line + "\n");

        }
        return stringBuilder.toString();
    }
}
