package com.example.esboco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CadastroActivity extends AppCompatActivity {

    FirebaseAuth mAuthCria;
    EditText usuario, senha, email;
    Button btnC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        btnC = findViewById(R.id.buttonCadastro);
        usuario = findViewById(R.id.inputName);
        senha = findViewById(R.id.inputPassword);
        email =  findViewById(R.id.inputEmail);

        mAuthCria = FirebaseAuth.getInstance();

        btnC.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String email_ = email.getText().toString();
                String senha_ = senha.getText().toString();


                mAuthCria.createUserWithEmailAndPassword(email_, senha_).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Usuário criado com sucesso", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(CadastroActivity.this, MainActivity.class));
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Erro ao criar usuário", Toast.LENGTH_LONG).show();
                    }
                });






            }
        });
    }
}