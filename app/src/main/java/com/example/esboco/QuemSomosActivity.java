package com.example.esboco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuemSomosActivity extends AppCompatActivity {

    TextView tvQs, tvQsTexto;
    ImageButton Iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quem_somos);

        tvQs = findViewById(R.id.textViewQuemS);
        tvQsTexto = findViewById(R.id.textViewQuemSomos);
        Iv = findViewById(R.id.imageButtonBackQ);

        Iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user.getEmail().contains("@estudante.ifms.edu.br")) {
                    startActivity(new Intent(QuemSomosActivity.this, TelaEstudanteActivity.class));
                }else{
                    startActivity(new Intent(QuemSomosActivity.this, TelaProfessorActivity.class));
                }
                finish();

            }
        });
    }
    @Override
    public void onBackPressed() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getEmail().contains("@estudante.ifms.edu.br")) {
            startActivity(new Intent(QuemSomosActivity.this, TelaEstudanteActivity.class));
        }else{
            startActivity(new Intent(QuemSomosActivity.this, TelaProfessorActivity.class));
        }
        super.onBackPressed();
    }
}