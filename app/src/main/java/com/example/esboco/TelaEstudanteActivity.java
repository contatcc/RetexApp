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

public class TelaEstudanteActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton logout;
    CardView cv1, cv2;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_estudante);

        cv1 = findViewById(R.id.cardviewConsulta);
        cv2 = findViewById(R.id.cardviewSobre2);
        pb = findViewById(R.id.progressBarTe);

        pb.setVisibility(View.GONE);
        logout = findViewById(R.id.imageButtonLogoutE);

        cv1.setOnClickListener((View.OnClickListener) this);
        cv2.setOnClickListener((View.OnClickListener) this);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance().signOut();

                Toast.makeText(TelaEstudanteActivity.this, "Usuário deslogado com sucesso", Toast.LENGTH_LONG).show();

                startActivity(new Intent(TelaEstudanteActivity.this, LoginActivity.class));
                finish();
                pb.setVisibility(View.GONE);
                cv1.setEnabled(false);
                cv2.setEnabled(false);
                logout.setEnabled(false);
            }
        });

    }

    @Override
    public void onClick(View v){
        Intent i;
        switch (v.getId()){
            case R.id.cardviewConsulta:
                pb.setVisibility(View.VISIBLE);
                i = new Intent(this, MainAcitivityRecyclerView.class);
                startActivity(i);
                finish();
                pb.setVisibility(View.GONE);
                cv1.setEnabled(false);
                cv2.setEnabled(false);
                logout.setEnabled(false);
                break;
            case R.id.cardviewSobre2:
                pb.setVisibility(View.VISIBLE);
                i = new Intent(this, QuemSomosActivity.class);
                startActivity(i);
                finish();
                pb.setVisibility(View.GONE);
                cv1.setEnabled(false);
                cv2.setEnabled(false);
                logout.setEnabled(false);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TelaEstudanteActivity.this, LoginActivity.class));
        super.onBackPressed();
    }
}