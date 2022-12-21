package com.example.esboco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditarTextoActivity extends AppCompatActivity {

    Texto textoRecebido;

    EditText edTituloTexto, edNomeAutor, edTexto, edGenero;
    Button btnSTA;
    RadioGroup rgTipo;
    RadioButton rbD, rbA, rbI, rbN, rbE;
    String id;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_texto);

        textoRecebido = (Texto) getIntent().getSerializableExtra("texto");

        edTituloTexto = findViewById(R.id.editTextTituloAlterar);
        edNomeAutor = findViewById(R.id.editTextTextAutorNomeAlterar);
        edTexto = findViewById(R.id.editTextTextoAlterar);
        edGenero = findViewById(R.id.editTextGeneroAlterar);
        btnSTA = findViewById(R.id.btnSalvarTextoAlterar);
        rgTipo  = findViewById(R.id.radioGroupTipoAlterar);
        rbA = findViewById(R.id.radioButtonArgumentativoAlterar);
        rbE = findViewById(R.id.radioButtonExpositivoAlterar);
        rbI = findViewById(R.id.radioButtonInjuntivoAlterar);
        rbN = findViewById(R.id.radioButtonNarrativoAlterar);
        rbD = findViewById(R.id.radioButtonDescritivoAlterar);
        iniciarFirebase();

        edTituloTexto.setText(textoRecebido.getTitulo());
        edNomeAutor.setText(textoRecebido.getAutor());
        edTexto.setText(textoRecebido.getTexto());
        edGenero.setText(textoRecebido.getGenero());
        id = textoRecebido.getId();

        if(textoRecebido.getTipologia().equals("Descritivo")){
            rbD.setChecked(true);
        }
        if(textoRecebido.getTipologia().equals("Narrativo")){
            rbN.setChecked(true);
        }
        if(textoRecebido.getTipologia().equals("Expositivo")){
            rbE.setChecked(true);
        }
        if(textoRecebido.getTipologia().equals("Argumentativo")){
            rbA.setChecked(true);
        }
        if(textoRecebido.getTipologia().equals("Injuntivo")){
            rbI.setChecked(true);
        }

        btnSTA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Texto textoAlterado = new Texto();
                textoAlterado.setId(id);
                textoAlterado.setTitulo(edTituloTexto.getText().toString());
                textoAlterado.setAutor(edNomeAutor.getText().toString());
                textoAlterado.setTexto(edTexto.getText().toString());
                textoAlterado.setGenero(edGenero.getText().toString());

                if(rgTipo.getCheckedRadioButtonId()==R.id.radioButtonDescritivoAlterar){
                    textoAlterado.setTipologia("Descritivo");
                }
                if(rgTipo.getCheckedRadioButtonId()==R.id.radioButtonNarrativoAlterar){
                    textoAlterado.setTipologia("Narrativo");
                }
                if(rgTipo.getCheckedRadioButtonId()==R.id.radioButtonExpositivoAlterar){
                    textoAlterado.setTipologia("Expositivo");
                }
                if(rgTipo.getCheckedRadioButtonId()==R.id.radioButtonArgumentativoAlterar){
                    textoAlterado.setTipologia("Argumentativo");
                }
                if(rgTipo.getCheckedRadioButtonId()==R.id.radioButtonInjuntivoAlterar){
                    textoAlterado.setTipologia("Injuntivo");
                }

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                databaseReference.
                        child("Textos").
                        child(textoRecebido.getId()).
                        setValue(textoAlterado);
                Toast.makeText(EditarTextoActivity.this, "Alterado com sucesso", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(EditarTextoActivity.this, MainAcitivityRecyclerView.class);
                startActivity(i);
            }
        });
    }

    private void iniciarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EditarTextoActivity.this, MainAcitivityRecyclerView.class));
        super.onBackPressed();
    }
}