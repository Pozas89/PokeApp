package com.cesba.pokeapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cesba.pokeapp.R;
import com.cesba.pokeapp.models.HistorialBatalla;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class HistorialBatallaAdapter
        extends RecyclerView.Adapter<HistorialBatallaAdapter.BatallaViewHolder> {

    private final Context context;
    private final List<HistorialBatalla> listaBatallas;

    public HistorialBatallaAdapter(
            Context context,
            List<HistorialBatalla> listaBatallas
    ) {
        this.context = context;
        this.listaBatallas = listaBatallas;
    }

    @NonNull
    @Override
    public BatallaViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.item_historial_batalla,
                        parent,
                        false
                );

        return new BatallaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull BatallaViewHolder holder,
            int position
    ) {

        HistorialBatalla batalla =
                listaBatallas.get(position);

        // NOMBRES
        holder.tvPokemonJugador.setText(
                batalla.getPokemonJugador() != null
                        ? batalla.getPokemonJugador().toUpperCase(Locale.ROOT)
                        : "POKÉMON"
        );

        holder.tvPokemonCpu.setText(
                batalla.getPokemonCpu() != null
                        ? batalla.getPokemonCpu().toUpperCase(Locale.ROOT)
                        : "POKÉMON"
        );

        // GANADOR
        String ganador =
                batalla.getGanador() != null
                        ? batalla.getGanador().toUpperCase(Locale.ROOT)
                        : "DESCONOCIDO";

        holder.tvGanador.setText(
                "🏆 " + ganador + " GANÓ"
        );

        // HP
        holder.tvHp.setText(
                "HP restante: "
                        + batalla.getHpRestante()
                        + " / "
                        + batalla.getHpMaxGanador()
        );

        holder.tvPorcentaje.setText(
                "Vida restante: "
                        + batalla.getPorcentajeHpRestante()
                        + "%"
        );

        holder.tvTurnos.setText(
                "Turnos: "
                        + batalla.getTurnos()
        );

        // FECHA
        if (batalla.getFecha() != null) {

            SimpleDateFormat formato =
                    new SimpleDateFormat(
                            "dd/MM/yyyy • h:mm a",
                            Locale.getDefault()
                    );

            holder.tvFecha.setText(
                    formato.format(
                            batalla.getFecha().toDate()
                    )
            );

        } else {

            holder.tvFecha.setText(
                    "Fecha no disponible"
            );
        }

        // SPRITE JUGADOR
        if (batalla.getImagenJugador() != null
                && !batalla.getImagenJugador().isEmpty()) {

            Glide.with(context)
                    .load(batalla.getImagenJugador())
                    .into(holder.imgJugador);

        } else {

            holder.imgJugador.setImageDrawable(null);
        }

        // SPRITE CPU
        if (batalla.getImagenCpu() != null
                && !batalla.getImagenCpu().isEmpty()) {

            Glide.with(context)
                    .load(batalla.getImagenCpu())
                    .into(holder.imgCpu);

        } else {

            holder.imgCpu.setImageDrawable(null);
        }

        // REGISTRO COMPLETO
        holder.btnVerRegistro.setOnClickListener(v ->
                mostrarRegistro(batalla)
        );
    }

    private void mostrarRegistro(
            HistorialBatalla batalla
    ) {

        ScrollView scrollView =
                new ScrollView(context);

        TextView texto =
                new TextView(context);

        int padding =
                (int) (
                        20
                                * context
                                .getResources()
                                .getDisplayMetrics()
                                .density
                );

        texto.setPadding(
                padding,
                padding,
                padding,
                padding
        );

        texto.setTextSize(14);

        texto.setTypeface(
                Typeface.MONOSPACE
        );

        String registro =
                batalla.getRegistro();

        if (registro == null
                || registro.trim().isEmpty()) {

            registro =
                    "No hay registro disponible para esta batalla.";
        }

        texto.setText(registro);

        scrollView.addView(texto);

        String titulo =
                batalla.getPokemonJugador()
                        + " VS "
                        + batalla.getPokemonCpu();

        new AlertDialog.Builder(context)
                .setTitle(titulo)
                .setView(scrollView)
                .setPositiveButton(
                        "CERRAR",
                        null
                )
                .show();
    }

    @Override
    public int getItemCount() {
        return listaBatallas.size();
    }

    static class BatallaViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgJugador;
        ImageView imgCpu;

        TextView tvPokemonJugador;
        TextView tvPokemonCpu;

        TextView tvGanador;
        TextView tvHp;
        TextView tvPorcentaje;
        TextView tvTurnos;
        TextView tvFecha;

        Button btnVerRegistro;

        public BatallaViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            imgJugador =
                    itemView.findViewById(
                            R.id.imgHistorialJugador
                    );

            imgCpu =
                    itemView.findViewById(
                            R.id.imgHistorialCpu
                    );

            tvPokemonJugador =
                    itemView.findViewById(
                            R.id.tvPokemonJugadorHistorial
                    );

            tvPokemonCpu =
                    itemView.findViewById(
                            R.id.tvPokemonCpuHistorial
                    );

            tvGanador =
                    itemView.findViewById(
                            R.id.tvGanadorHistorial
                    );

            tvHp =
                    itemView.findViewById(
                            R.id.tvHpHistorial
                    );

            tvPorcentaje =
                    itemView.findViewById(
                            R.id.tvPorcentajeHistorial
                    );

            tvTurnos =
                    itemView.findViewById(
                            R.id.tvTurnosHistorial
                    );

            tvFecha =
                    itemView.findViewById(
                            R.id.tvFechaHistorial
                    );

            btnVerRegistro =
                    itemView.findViewById(
                            R.id.btnVerRegistro
                    );
        }
    }
}