package com.mapache.pokedex20.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtil {

    private static final String POKE_API_BASE_URL = "https://pokeapi.co/api/v2/pokemon/?offset=0&limit=964";

    private static final String TAG = NetworkUtil.class.getSimpleName();

    public static URL buildUrl() {
        Uri builtUri = Uri.parse(POKE_API_BASE_URL).buildUpon().build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static URL crearUrls(String url){
        Uri uri = Uri.parse(url).buildUpon().build();
        try {
            URL u = new URL(uri.toString());
            return u;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

