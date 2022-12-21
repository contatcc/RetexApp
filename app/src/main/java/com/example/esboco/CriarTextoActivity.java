package com.example.esboco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class CriarTextoActivity extends AppCompatActivity {

    EditText edTituloTexto, edNomeAutor, edTexto, edGenero;
    Button btnST;
    RadioGroup rgTipo;
    ProgressBar pb;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_texto);

        Texto texto = new Texto();

        edTituloTexto = findViewById(R.id.editTextTitulo);
        edNomeAutor = findViewById(R.id.editTextTextAutorNome);
        edTexto = findViewById(R.id.editTextTexto);
        btnST = findViewById(R.id.btnSalvarTexto);
        rgTipo  = findViewById(R.id.radioGroupTipo);
        edGenero = findViewById(R.id.editTextGenero);
        pb = findViewById(R.id.progressBarC);

        pb.setVisibility(View.GONE);
        iniciarFirebase();

        btnST.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 //ideia: se não tiver vazio, cadastar, exibir toast de sucesso ou erro, levar para outra tela
                if(validar()){
                    texto.setId(UUID.randomUUID().toString());
                    texto.setTitulo(edTituloTexto.getText().toString());
                    texto.setAutor(edNomeAutor.getText().toString());
                    texto.setTexto(edTexto.getText().toString());
                    texto.setGenero(edGenero.getText().toString());

                    if(rgTipo.getCheckedRadioButtonId()==R.id.radioButtonDescritivo){
                        texto.setTipologia("Descritivo");
                    }
                    if(rgTipo.getCheckedRadioButtonId()==R.id.radioButtonNarrativo){
                        texto.setTipologia("Narrativo");
                    }
                    if(rgTipo.getCheckedRadioButtonId()==R.id.radioButtonExpositivo){
                        texto.setTipologia("Expositivo");
                    }
                    if(rgTipo.getCheckedRadioButtonId()==R.id.radioButtonArgumentativo){
                        texto.setTipologia("Argumentativo");
                    }
                    if(rgTipo.getCheckedRadioButtonId()==R.id.radioButtonInjuntivo){
                        texto.setTipologia("Injuntivo");
                    }

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    databaseReference.
                            child("Textos").
                            child(texto.getId()).
                            setValue(texto);

                    pb.setVisibility(View.VISIBLE);
                    btnST.setEnabled(false);

                    Toast.makeText(CriarTextoActivity.this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(CriarTextoActivity.this, TelaProfessorActivity.class);
                    startActivity(i);
                }
             }
         });
    }

    private boolean validar() {
        if (edTituloTexto.getText().toString().trim().equals("")) {
            edTituloTexto.setError("Informe um título");
            return false;
        }
        if (edNomeAutor.getText().toString().trim().equals("")) {
            edNomeAutor.setError("Informe um nome");
            return false;
        }
        if(edGenero.getText().toString().trim().equals("")){
            edTexto.setError("Informe um gênero");
            return false;
        }
        if(edTexto.getText().toString().trim().equals("")){
            edTexto.setError("Informe um texto");
            return false;
        }
        return true;
    }

    private void iniciarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CriarTextoActivity.this, TelaProfessorActivity.class));
        super.onBackPressed();
    }
}