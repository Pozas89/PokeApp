package com.cesba.pokeapp;



import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegistroActivity extends AppCompatActivity {

    EditText etNombre, etCorreoRegistro;
    EditText etContrasenaRegistro, etConfirmarContrasena;

    Button btnRegistrar, btnVolverLogin;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etNombre = findViewById(R.id.etNombre);
        etCorreoRegistro = findViewById(R.id.etCorreoRegistro);
        etContrasenaRegistro =
                findViewById(R.id.etContrasenaRegistro);
        etConfirmarContrasena =
                findViewById(R.id.etConfirmarContrasena);

        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnVolverLogin = findViewById(R.id.btnVolverLogin);

        mAuth = FirebaseAuth.getInstance();

        btnRegistrar.setOnClickListener(v -> {

            String nombre = etNombre.getText().toString().trim();
            String correo = etCorreoRegistro.getText()
                    .toString().trim();
            String contrasena = etContrasenaRegistro.getText()
                    .toString();
            String confirmar = etConfirmarContrasena.getText()
                    .toString();

            // Validar campos vacíos
            if (nombre.isEmpty() || correo.isEmpty()
                    || contrasena.isEmpty() || confirmar.isEmpty()) {

                Toast.makeText(this,
                        "Completa todos los campos",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar contraseñas
            if (!contrasena.equals(confirmar)) {

                etConfirmarContrasena.setError(
                        "Las contraseñas no coinciden");
                return;
            }

            // Firebase requiere al menos 6 caracteres
            if (contrasena.length() < 6) {

                etContrasenaRegistro.setError(
                        "Mínimo 6 caracteres");
                return;
            }

            // Crear cuenta en Firebase
            mAuth.createUserWithEmailAndPassword(
                    correo, contrasena
            ).addOnCompleteListener(this, task -> {

                if (task.isSuccessful()) {

                    FirebaseUser usuario =
                            mAuth.getCurrentUser();

                    if (usuario != null) {

                        UserProfileChangeRequest perfil =
                                new UserProfileChangeRequest.Builder()
                                        .setDisplayName(nombre)
                                        .build();

                        usuario.updateProfile(perfil)
                                .addOnCompleteListener(profileTask -> {

                                    Toast.makeText(
                                            RegistroActivity.this,
                                            "¡Cuenta creada correctamente!",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    Intent intent = new Intent(
                                            RegistroActivity.this,
                                            HomeActivity.class
                                    );

                                    startActivity(intent);
                                    finish();
                                });
                    }

                } else {

                    Toast.makeText(
                            RegistroActivity.this,
                            "No se pudo registrar: "
                                    + task.getException().getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            });
        });

        // Regresar al login
        btnVolverLogin.setOnClickListener(v -> {
            finish();
        });
    }
}