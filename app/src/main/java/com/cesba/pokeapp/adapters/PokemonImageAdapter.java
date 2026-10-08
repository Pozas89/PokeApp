package com.cesba.pokeapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cesba.pokeapp.R;

import java.util.List;

public class PokemonImageAdapter
        extends RecyclerView.Adapter<PokemonImageAdapter.ViewHolder> {

    private final List<String> imagenes;

    public PokemonImageAdapter(List<String> imagenes) {
        this.imagenes = imagenes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_pokemon_image,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Glide.with(holder.itemView.getContext())
                .load(imagenes.get(position))
                .into(holder.imgPokemon);
    }

    @Override
    public int getItemCount() {
        return imagenes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPokemon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPokemon =
                    itemView.findViewById(
                            R.id.imgPokemonPagina
                    );
        }
    }
}