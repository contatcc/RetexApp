package com.example.esboco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExibirTextoActivity extends AppCompatActivity {

    TextView tvEt, tvEtit, tvEa, tvEtp, tvEGn;
    ImageButton IEdit, IExcl, Ifav, Iback;
    ProgressBar pbEx;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String id = "";
    String texto ="" ;
    String titulo ="";
    String autor ="";
    String tipologia ="";
    String genero ="";
    Texto textoObj;
    Texto texto2 = new Texto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibir_texto);

        tvEt = findViewById(R.id.textViewExibeTexto);
        tvEtit = findViewById(R.id.textViewExibeTitulo);
        tvEa = findViewById(R.id.textViewExibeA);
        tvEtp = findViewById(R.id.textViewExibeTipoT);
        tvEGn = findViewById(R.id.textViewQuemSomos);
        IEdit = findViewById(R.id.imageButtonE);
        IExcl = findViewById(R.id.imageButtonEx);
        Ifav = findViewById(R.id.imageButtonFav);
        Iback = findViewById(R.id.imageButtonBack);

        pbEx = findViewById(R.id.progressBarQs);

        Intent intent = getIntent();

         id = (String) intent.getSerializableExtra("Id");
         texto = (String) intent.getSerializableExtra("Texto");
         titulo = (String) intent.getSerializableExtra("Título");
         autor = (String) intent.getSerializableExtra("Autor");
         tipologia = (String) intent.getSerializableExtra("Tipologia");
         genero = (String) intent.getSerializableExtra("Genero");
         textoObj = (Texto) intent.getSerializableExtra("TextoObj");

        tvEt.setText(texto);
        tvEtit.setText(titulo);
        tvEa.setText(autor);
        tvEtp.setText(tipologia);
        tvEGn.setText(genero);

        pbEx.setVisibility(View.GONE);

        iniciarFirebase();

        alterarIcone();

        Iback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExibirTextoActivity.this, MainAcitivityRecyclerView.class);
                startActivity(intent);
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getEmail().contains("@estudante.ifms.edu.br")) {
            IEdit.setVisibility(View.GONE);
            IExcl.setVisibility(View.GONE);

            Ifav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pbEx.setVisibility(View.VISIBLE);
                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().
                            child("TextosFavoritos").
                            child(user.getUid()).
                            child(titulo);

                    rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                //para verificar se o nó informado no reference existe.
                                // Caso exista: remover, caso não exista, salvar.
                                Ifav.setImageResource(R.drawable.ic_naofavorito);
                                rootRef.removeValue();
                                Toast.makeText(ExibirTextoActivity.this, "Texto removido dos favoritos", Toast.LENGTH_SHORT).show();

                            } else {
                                rootRef.setValue(textoObj);
                                Ifav.setImageResource(R.drawable.ic_favoritar);
                                Toast.makeText(ExibirTextoActivity.this, "Texto favoritado!", Toast.LENGTH_SHORT).show();
                            }
                            
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    finish();
                    pbEx.setVisibility(View.GONE);
                    Ifav.setEnabled(false);
                }
            });

        } else {
            Ifav.setVisibility(View.GONE);
            IEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pbEx.setVisibility(View.VISIBLE);
                    Texto text = new Texto(id, titulo, autor, tipologia, genero, texto);
                    Intent intent = new Intent(ExibirTextoActivity.this, EditarTextoActivity.class);
                    intent.putExtra("texto", text);
                    startActivity(intent);

                    finish();
                    pbEx.setVisibility(View.GONE);
                    IEdit.setEnabled(false);
                    IExcl.setEnabled(false);
                }
            });

            IExcl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder janela = new AlertDialog.Builder(ExibirTextoActivity.this);

                    janela.setTitle("Excluir texto");
                    janela.setMessage("Deseja realmente excluir este texto?");


                    janela.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            pbEx.setVisibility(View.VISIBLE);
                            databaseReference.
                                    child("Textos").
                                    child(textoObj.getId()).
                                    removeValue();

                            databaseReference.
                                    child("TextosFavoritos").
                                    child(user.getUid()).
                                    child(textoObj.getId()).
                                    removeValue();

                            Toast.makeText(ExibirTextoActivity.this, "Removido com sucesso!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ExibirTextoActivity.this, MainAcitivityRecyclerView.class);
                            startActivity(intent);

                            finish();
                            pbEx.setVisibility(View.GONE);
                            IEdit.setEnabled(false);
                            IExcl.setEnabled(false);
                        }
                    });

                    janela.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //TODO dar ação para este elemento
                        }
                    });

                    janela.show();


                }
            });

        }

    }

    private void alterarIcone() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().
                child("TextosFavoritos").
                child(user.getUid()).
                child(titulo);

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                   Ifav.setImageResource(R.drawable.ic_naofavorito);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void iniciarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ExibirTextoActivity.this, MainAcitivityRecyclerView.class));
        super.onBackPressed();
    }
}