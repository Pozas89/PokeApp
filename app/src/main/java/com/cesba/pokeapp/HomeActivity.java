package com.cesba.pokeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private TextView tvBienvenida;

    private TextView btnPokedex;
    private TextView btnBattleEmulator;
    private TextView btnHistorial;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);


        // =====================================================
        // CONECTAR ELEMENTOS DEL XML
        // =====================================================

        tvBienvenida =
                findViewById(R.id.tvBienvenida);

        btnPokedex =
                findViewById(R.id.btnPokedex);

        btnBattleEmulator =
                findViewById(R.id.btnBattleEmulator);

        btnHistorial =
                findViewById(R.id.btnHistorial);


        // =====================================================
        // FIREBASE
        // =====================================================

        mAuth =
                FirebaseAuth.getInstance();

        FirebaseUser usuario =
                mAuth.getCurrentUser();


        // =====================================================
        // VERIFICAR SESIÓN
        // =====================================================

        if (usuario != null) {

            String nombre =
                    usuario.getDisplayName();

            if (nombre != null
                    && !nombre.isEmpty()) {

                tvBienvenida.setText(
                        "¡Hola, " + nombre + "!"
                );

            } else {

                tvBienvenida.setText(
                        "¡Hola, entrenador Pokémon!"
                );
            }

        } else {

            irAlLogin();

            return;
        }


        // =====================================================
        // BOTÓN POKÉDEX
        // =====================================================

        btnPokedex.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            PokedexActivity.class
                    );

            startActivity(intent);
        });


        // =====================================================
        // BOTÓN EMULADOR DE BATALLA
        // =====================================================

        btnBattleEmulator.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            SeleccionBatallaActivity.class
                    );

            startActivity(intent);
        });


        // =====================================================
        // BOTÓN HISTORIAL DE BATALLAS
        // =====================================================

        btnHistorial.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            HistorialActivity.class
                    );

            startActivity(intent);
        });
    }


    // =========================================================
    // REGRESAR AL LOGIN SI NO EXISTE SESIÓN
    // =========================================================

    private void irAlLogin() {

        Intent intent =
                new Intent(
                        HomeActivity.this,
                        MainActivity.class
                );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);

        finish();
    }
}