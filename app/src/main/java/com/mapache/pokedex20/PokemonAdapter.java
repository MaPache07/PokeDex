package com.mapache.pokedex20;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mapache.pokedex20.models.Pokemon;

import java.util.ArrayList;

public abstract class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.ViewHolderObj> {

    private ArrayList<Pokemon> listPokemon;

    public PokemonAdapter(ArrayList<Pokemon> listaDatos) {
        this.listPokemon = listaDatos;
    }

    @Override
    public ViewHolderObj onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_poke,viewGroup,false);
        return new ViewHolderObj(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderObj viewHolderObj, final int i) {
        viewHolderObj.asign(listPokemon.get(i));
        viewHolderObj.pokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendData(listPokemon.get(i).getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPokemon.size();
    }

    class ViewHolderObj extends RecyclerView.ViewHolder {

        public Button pokeButton;

        public ViewHolderObj(View itemView) {
            super(itemView);
            pokeButton = itemView.findViewById(R.id.pokemon_name);
        }

        public void asign(final Pokemon s) {
            pokeButton.setText(s.getName());
        }
    }

    public abstract void onSendData(String pokemonData);
}