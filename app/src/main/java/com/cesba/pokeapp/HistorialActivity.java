package com.cesba.pokeapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cesba.pokeapp.adapters.HistorialBatallaAdapter;
import com.cesba.pokeapp.models.HistorialBatalla;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class HistorialActivity extends AppCompatActivity {

    private ImageButton btnRegresarHistorial;

    private RecyclerView recyclerHistorial;

    private ProgressBar progresoHistorial;

    private TextView tvHistorialVacio;

    private final List<HistorialBatalla> listaBatallas =
            new ArrayList<>();

    private HistorialBatallaAdapter adapter;

    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_historial
        );

        // VISTAS
        btnRegresarHistorial =
                findViewById(
                        R.id.btnRegresarHistorial
                );

        recyclerHistorial =
                findViewById(
                        R.id.recyclerHistorial
                );

        progresoHistorial =
                findViewById(
                        R.id.progresoHistorial
                );

        tvHistorialVacio =
                findViewById(
                        R.id.tvHistorialVacio
                );

        // FIREBASE
        mAuth =
                FirebaseAuth.getInstance();

        db =
                FirebaseFirestore.getInstance();

        // RECYCLER VIEW
        recyclerHistorial.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter =
                new HistorialBatallaAdapter(
                        this,
                        listaBatallas
                );

        recyclerHistorial.setAdapter(
                adapter
        );

        // REGRESAR
        btnRegresarHistorial.setOnClickListener(
                v -> finish()
        );

        // CARGAR FIRESTORE
        cargarHistorial();
    }

    private void cargarHistorial() {

        FirebaseUser usuario =
                mAuth.getCurrentUser();

        if (usuario == null) {

            Toast.makeText(
                    this,
                    "No hay un usuario autenticado",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }

        progresoHistorial.setVisibility(
                View.VISIBLE
        );

        recyclerHistorial.setVisibility(
                View.GONE
        );

        tvHistorialVacio.setVisibility(
                View.GONE
        );

        db.collection("usuarios")

                .document(
                        usuario.getUid()
                )

                .collection(
                        "historial_batallas"
                )

                .orderBy(
                        "fecha",
                        Query.Direction.DESCENDING
                )

                .get()

                .addOnSuccessListener(
                        queryDocumentSnapshots -> {

                            progresoHistorial.setVisibility(
                                    View.GONE
                            );

                            listaBatallas.clear();

                            for (DocumentSnapshot documento
                                    : queryDocumentSnapshots.getDocuments()) {

                                HistorialBatalla batalla =
                                        documento.toObject(
                                                HistorialBatalla.class
                                        );

                                if (batalla != null) {

                                    batalla.setId(
                                            documento.getId()
                                    );

                                    listaBatallas.add(
                                            batalla
                                    );
                                }
                            }

                            adapter.notifyDataSetChanged();

                            if (listaBatallas.isEmpty()) {

                                recyclerHistorial.setVisibility(
                                        View.GONE
                                );

                                tvHistorialVacio.setVisibility(
                                        View.VISIBLE
                                );

                            } else {

                                tvHistorialVacio.setVisibility(
                                        View.GONE
                                );

                                recyclerHistorial.setVisibility(
                                        View.VISIBLE
                                );
                            }
                        }
                )

                .addOnFailureListener(
                        e -> {

                            progresoHistorial.setVisibility(
                                    View.GONE
                            );

                            recyclerHistorial.setVisibility(
                                    View.GONE
                            );

                            tvHistorialVacio.setVisibility(
                                    View.VISIBLE
                            );

                            tvHistorialVacio.setText(
                                    "No se pudo cargar el historial."
                            );

                            Toast.makeText(
                                    HistorialActivity.this,
                                    "Error al consultar Firestore",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                );
    }
}