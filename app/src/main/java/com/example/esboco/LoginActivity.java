package com.example.esboco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etEmail, etSenha;
    Button btnL;
    TextView recSenha;
    LinearLayout llC;
    FirebaseAuth mAuth;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.inputEmail);
        etSenha = findViewById(R.id.inputPassword);
        recSenha = findViewById(R.id.textViewEsqueceuSenha);
        btnL = findViewById(R.id.buttonLogin);
        llC = findViewById(R.id.linearLayoutCadastro);
        pb = findViewById(R.id.progressBarL);

        pb.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        llC.setOnClickListener(this);
        recSenha.setOnClickListener(this);
        btnL.setOnClickListener(this);

        }

    @Override
    public void onClick(View view) {
            switch (view.getId()) {
                case R.id.buttonLogin:
                    logar();

                    break;
                case R.id.textViewEsqueceuSenha:
                    pb.setVisibility(View.VISIBLE);
                    Intent i = new Intent(LoginActivity.this, EsqueceuSenhaActivity.class);
                    startActivity(i);
                    finish();

                    pb.setVisibility(View.GONE);
                    llC.setEnabled(false);
                    btnL.setEnabled(false);
                    recSenha.setEnabled(false);

                    break;
                case R.id.linearLayoutCadastro:
                    i = new Intent(LoginActivity.this, CadastroActivity.class);
                    startActivity(i);

                    pb.setVisibility(View.VISIBLE);
                    llC.setEnabled(false);
                    recSenha.setEnabled(false);
                    btnL.setEnabled(false);
                    pb.setVisibility(View.GONE);
                    break;
            }
    }

    private void logar() {
            String email = etEmail.getText().toString();
            String senha = etSenha.getText().toString();

            if (email.equals("") || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Email não correspondido");
                etEmail.requestFocus();
                return;
            }

            if (senha.equals("")) {
                etSenha.setError("Senha inválida");
                etSenha.requestFocus();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        pb.setVisibility(View.VISIBLE);
                        btnL.setEnabled(false);
                        llC.setEnabled(false);
                        recSenha.setEnabled(false);
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user.isEmailVerified()) {
                            if (email.contains("@estudante.ifms.edu.br")) {

                                startActivity(new Intent(LoginActivity.this, TelaEstudanteActivity.class));
                                finish();
                            }
                            else if (email.contains("@ifms.edu.br")) {
                                startActivity(new Intent(LoginActivity.this, TelaProfessorActivity.class));
                                finish();
                            }
                            else {
                                pb.setVisibility(View.GONE);
                                btnL.setEnabled(true);
                                Toast.makeText(LoginActivity.this, "Tipo de e-mail inválido, tente com a institucional", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            pb.setVisibility(View.GONE);
                            btnL.setEnabled(true);
                            Toast.makeText(LoginActivity.this, "Verifique sua conta via e-mail, na caixa de spam", Toast.LENGTH_SHORT).show();
                            user.sendEmailVerification();
                        }
                    }
                    else
                        Toast.makeText(LoginActivity.this, "Erro ao logar", Toast.LENGTH_LONG).show();
                }
            });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        super.onBackPressed();
    }
}