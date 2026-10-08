package com.cesba.pokeapp;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText etCorreo, etContrasena;
    Button btnLogin, btnIrRegistro;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCorreo = findViewById(R.id.etCorreo);
        etContrasena = findViewById(R.id.etContrasena);

        btnLogin = findViewById(R.id.btnLogin);
        btnIrRegistro = findViewById(R.id.btnIrRegistro);

        mAuth = FirebaseAuth.getInstance();

        // Iniciar sesión
        btnLogin.setOnClickListener(v -> {

            String correo = etCorreo.getText().toString().trim();
            String contrasena = etContrasena.getText().toString();

            if (correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this,
                        "Completa todos los campos",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(correo, contrasena)
                    .addOnCompleteListener(this, task -> {

                        if (task.isSuccessful()) {

                            Toast.makeText(this,
                                    "¡Bienvenido, entrenador Pokémon!",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(
                                    MainActivity.this,
                                    HomeActivity.class
                            );

                            startActivity(intent);
                            finish();

                        } else {

                            Toast.makeText(this,
                                    "Error al iniciar sesión: "
                                            + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Ir al registro
        btnIrRegistro.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    RegistroActivity.class
            );

            startActivity(intent);
        });
    }
}