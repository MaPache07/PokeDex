package com.mapache.pokedex20;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mapache.pokedex20.models.ListPokemon;
import com.mapache.pokedex20.models.Pokemon;
import com.mapache.pokedex20.utilities.AppConstants;
import com.mapache.pokedex20.utilities.NetworkUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText mPokemonId;
    private Button mSearchPokemon;
    private RecyclerView recycler;
    private TextView mPokemonName;
    private ArrayList<Pokemon> pokeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();

        new FetchPokemonTask().execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public class InitRecyclerAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length == 0) {
                return null;
            }

            String pokemonInfo = strings[0];
            Gson g = new Gson();
            ListPokemon listPoke = g.fromJson(pokemonInfo, ListPokemon.class);

            for(int i = 0; i < listPoke.getResults().size(); i++){
                pokeList.add(new Pokemon(listPoke.getResults().get(i).getName(), listPoke.getResults().get(i).getUrl()));
            }
            return null;
        }

        @Override
        protected void onPostExecute(String pokemonInfo) {
            initRecycler();
        }
    }

    public void initRecycler(){
        recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        PokemonAdapter adapter = new PokemonAdapter(pokeList) {
            @Override
            public void onSendData(String pokemonURL) {
                Intent mIntent = new Intent(MainActivity.this, PokeActivity.class);
                mIntent.putExtra(AppConstants.TEXT_KEY, pokemonURL);
                MainActivity.this.startActivity(mIntent);
            }
        };
        recycler.setAdapter(adapter);
    }

    public void bindView(){
        mPokemonId = findViewById(R.id.idp);
        mSearchPokemon = findViewById(R.id.search);
        recycler = findViewById(R.id.pokemon_list);
        mPokemonName = findViewById(R.id.pokemon_name);

        mSearchPokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mPokemonId.getText().toString().trim();

                if(id.isEmpty()){
                    //mResultText.setText(R.string.nothing);
                }
                else{
                    new FetchPokemonTask().execute(id);
                }
            }
        });
    }

    private class FetchPokemonTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... pokemonNumbers) {

            URL pokeAPI = NetworkUtil.buildUrl();
            try {
                String pokemonInfo = NetworkUtil.getResponseFromHttpUrl(pokeAPI);
                return pokemonInfo;
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String pokemonInfo) {
            new InitRecyclerAsync().execute(pokemonInfo);
        }
    }
}
