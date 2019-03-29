package com.mapache.pokedex20;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mapache.pokedex20.utilities.AppConstants;
import com.mapache.pokedex20.utilities.NetworkUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class PokeActivity extends AppCompatActivity {

    private TextView name_poke, id_poke, type_poke, weight_poke, height_poke;
    private ImageView image_poke;
    private URL link_poke;
    private JsonObject pokeObject, typeObject;
    private JsonParser pokeParser = new JsonParser();
    private JsonArray pokeArray, types;
    private String name, type, id, weight, height, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke);

        bindView();

        Intent pIntent = getIntent();
        String link = pIntent.getStringExtra(AppConstants.TEXT_KEY);

        new FetchPokemon().execute(link);
    }

    private class FetchPokemon extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length == 0) {
                return null;
            }
            link_poke = NetworkUtil.crearUrls(strings[0]);
            try {
                String pokeJson = NetworkUtil.getResponseFromHttpUrl(link_poke);
                pokeJson = "[" + pokeJson + "]";
                return pokeJson;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String pokeJson) {

            pokeArray = pokeParser.parse(pokeJson).getAsJsonArray();
            for(JsonElement pokeElement : pokeArray){
                pokeObject = pokeElement.getAsJsonObject();
                id = Integer.toString(pokeObject.get("id").getAsInt());
                name = pokeObject.get("name").getAsString();
                types = pokeObject.getAsJsonArray("types");
                JsonElement typeElement = types.get(0);
                typeObject = typeElement.getAsJsonObject();
                type = typeObject.get("type").getAsJsonObject().get("name").getAsString();
                weight = pokeObject.get("weight").getAsString();
                height = pokeObject.get("height").getAsString();
                if(pokeObject.get("sprites").getAsJsonObject().get("front_default") == null){
                    image = pokeObject.get("sprites").getAsJsonObject().get("front_female").toString();
                }
                else{
                    image = pokeObject.get("sprites").getAsJsonObject().get("front_default").toString();
                }
            }
            name_poke.setText(name);
            id_poke.setText(id);
            type_poke.setText(type);
            weight_poke.setText(weight);
            height_poke.setText(height);
            new GetImageUrl().execute(image);
        }
    }

    private class GetImageUrl extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap bm = null;
            try {
                URL aURL = new URL(urls[0]);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
            } catch (IOException e) {

            }
            return bm;
        }
        protected void onPostExecute(Bitmap myBitMap) {
            image_poke.setImageBitmap(myBitMap);
        }
    }

    public void bindView(){
        id_poke = findViewById(R.id.idPoke);
        name_poke = findViewById(R.id.namePoke);
        type_poke = findViewById(R.id.typePoke);
        weight_poke = findViewById(R.id.weightPoke);
        image_poke = findViewById(R.id.imagePoke);
        height_poke = findViewById(R.id.heightPoke);
    }
}

