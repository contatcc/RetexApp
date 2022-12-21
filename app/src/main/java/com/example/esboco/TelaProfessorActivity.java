package com.example.esboco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TelaProfessorActivity extends AppCompatActivity implements  View.OnClickListener{

    ImageButton btnLogout;
    ProgressBar pb;
    CardView c1, c2, c3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_professor);

        c1= findViewById(R.id.cardviewCriarTexto);
        c2 = findViewById(R.id.cardviewSobre);
        c3 = findViewById(R.id.cardviewConsultaProf);
        pb = findViewById(R.id.progressBarTp);

        pb.setVisibility(View.GONE);

        btnLogout =findViewById(R.id.imageButtonLogout);

        c1.setOnClickListener((View.OnClickListener) this);
        c2.setOnClickListener((View.OnClickListener) this);
        c3.setOnClickListener((View.OnClickListener) this);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance().signOut();

                Toast.makeText(TelaProfessorActivity.this, "Usuário deslogado com sucesso", Toast.LENGTH_LONG).show();

                startActivity(new Intent(TelaProfessorActivity.this, LoginActivity.class));
                finish();
                pb.setVisibility(View.GONE);
                c1.setEnabled(false);
                c2.setEnabled(false);
                c3.setEnabled(false);
                btnLogout.setEnabled(false);

            }
        });

    }

    @Override
    public void onClick(View v){
        Intent i;
        switch (v.getId()){
            case R.id.cardviewCriarTexto:
                pb.setVisibility(View.VISIBLE);
                i = new Intent(TelaProfessorActivity.this, CriarTextoActivity.class);
                startActivity(i);
                finish();
                pb.setVisibility(View.GONE);
                c1.setEnabled(false);
                c2.setEnabled(false);
                c3.setEnabled(false);
                btnLogout.setEnabled(false);
                break;
            case R.id.cardviewConsultaProf:
                pb.setVisibility(View.VISIBLE);
                i = new Intent(TelaProfessorActivity.this, MainAcitivityRecyclerView.class);
                startActivity(i);
                finish();
                pb.setVisibility(View.GONE);
                c1.setEnabled(false);
                c2.setEnabled(false);
                c3.setEnabled(false);
                btnLogout.setEnabled(false);
                break;
            case R.id.cardviewSobre:
                pb.setVisibility(View.VISIBLE);
                i = new Intent(TelaProfessorActivity.this, QuemSomosActivity.class);
                startActivity(i);
                finish();
                pb.setVisibility(View.GONE);
                c1.setEnabled(false);
                c2.setEnabled(false);
                c3.setEnabled(false);
                btnLogout.setEnabled(false);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TelaProfessorActivity.this, LoginActivity.class));
        super.onBackPressed();
    }
}