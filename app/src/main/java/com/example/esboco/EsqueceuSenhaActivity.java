package com.example.esboco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class EsqueceuSenhaActivity extends AppCompatActivity {

    FirebaseAuth mAuthRec;
    EditText recSenha;
    Button btnE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu_senha);

        recSenha = findViewById(R.id.inputEmail);
        btnE = findViewById(R.id.buttonEnviar);
        mAuthRec = FirebaseAuth.getInstance();

        btnE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = recSenha.getText().toString();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.equals("")) {
                    recSenha.setError("Preencha corretamente");
                    return;
                }

                mAuthRec.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EsqueceuSenhaActivity.this, "Email enviado.", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(EsqueceuSenhaActivity.this, LoginActivity.class);
                            startActivity(i);
                        }
                        else
                            Toast.makeText(EsqueceuSenhaActivity.this, "Erro.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}