package com.example.esboco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;


public class MainActivity extends AppCompatActivity {

    Button btnL, btnC;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnL = findViewById(R.id.buttonLogin);
        btnC = findViewById(R.id.buttonCadastro);
        pb = findViewById(R.id.progressBarM);

        pb.setVisibility(View.GONE);
        btnL.setEnabled(true);
        btnC.setEnabled(true);

        btnL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                Intent i= new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                pb.setVisibility(View.GONE);

                    btnL.setEnabled(false);
                    btnC.setEnabled(false);

            }
        });

        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                startActivity(new Intent(MainActivity.this, CadastroActivity.class));
                finish();
                pb.setVisibility(View.GONE);
                btnC.setEnabled(false);
                btnL.setEnabled(false);
            }
        });
    }
}