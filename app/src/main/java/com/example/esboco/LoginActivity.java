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
    TextView recSenha, tvC;
    LinearLayout llC;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.inputEmail);
        etSenha = findViewById(R.id.inputPassword);
        recSenha = findViewById(R.id.textViewEsqueceuSenha);
        btnL = findViewById(R.id.buttonLogin);
        llC = findViewById(R.id.linearLayoutCadastro);

        mAuth = FirebaseAuth.getInstance();

        llC.setOnClickListener(this);
        recSenha.setOnClickListener(this);
        btnL.setOnClickListener(this);

        @Override
        public void onClick(View view){
            switch (view.getId()) {
                case R.id.buttonLogin:
                    logar();
                    break;
                case R.id.inputPassword:
                    Intent i = new Intent(LoginActivity.this, EsqueceuSenhaActivity.class);
                    startActivity(i);
                    break;
                case R.id.linearLayoutCadastro:
                    i = new Intent(LoginActivity.this, CadastroActivity.class);
                    startActivity(i);
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
                        //verificar cadastro via email (link para clicar e ativar cadastro)
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user.isEmailVerified()) {
                            //se já verificou via email, redireciona login
                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(i);
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Verifique sua conta via email", Toast.LENGTH_SHORT).show();
                            user.sendEmailVerification();
                        }
                    }
                    else 
                        Toast.makeText(LoginActivity.this, "Erro ao logar", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }


        /*TextView btn = findViewById(R.id.textViewCadastro);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
            }
        });

        TextView tvES = findViewById(R.id.textViewEsqueceuSenha);
        tvES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, EsqueceuSenhaActivity.class));
            }
        });
    }*/
}