package com.example.esboco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuthException;

public class CadastroActivity extends AppCompatActivity {

    FirebaseAuth mAuthCria;
    EditText usuario, senha, email;
    Button btnC;

    ProgressBar pb;
    LinearLayout llL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        btnC = findViewById(R.id.buttonCadastro);
        usuario = findViewById(R.id.inputName);
        senha = findViewById(R.id.inputPassword);
        email = findViewById(R.id.inputEmail);
        pb = findViewById(R.id.progressBarC);
        llL = findViewById(R.id.linearLayoutLogin);

        pb.setVisibility(View.INVISIBLE);

        mAuthCria = FirebaseAuth.getInstance();
        Log.e("mAuth", mAuthCria.toString());

        llL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CadastroActivity.this, LoginActivity.class);
                startActivity(i);

                pb.setVisibility(View.VISIBLE);
                llL.setEnabled(false);
                btnC.setEnabled(false);
            }
        });

        btnC.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String email_ = email.getText().toString();
                String senha_ = senha.getText().toString();

                if (validaEmail(email_)) {
                    mAuthCria.createUserWithEmailAndPassword(email_, senha_).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Usuário criado com sucesso", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(CadastroActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), "Erro ao criar usuário", Toast.LENGTH_LONG).show();
                                gerenciarErro(task);
                            }
                        }
                    });
                }

                pb.setVisibility(View.VISIBLE);
                btnC.setEnabled(false);

            }
        });
    }

    private boolean validaEmail(String email_) {
        if (email_.contains("@ifms.edu.br") || email_.contains("@estudante.ifms.edu.br")) {
            return true;
        } else {
            email.setError("Informe e-mail institucional.");
            return false;
        }
    }

    private void gerenciarErro(Task<AuthResult> task) {
        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

        switch (errorCode) {
            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(CadastroActivity.this, "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(CadastroActivity.this, "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(CadastroActivity.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(CadastroActivity.this, "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                email.setError("The email address is badly formatted.");
                email.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(CadastroActivity.this, "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                senha.setError("password is incorrect ");
                senha.requestFocus();
                senha.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(CadastroActivity.this, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(CadastroActivity.this, "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(CadastroActivity.this, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(CadastroActivity.this, "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                email.setError("The email address is already in use by another account.");
                email.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(CadastroActivity.this, "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(CadastroActivity.this, "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(CadastroActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(CadastroActivity.this, "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(CadastroActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(CadastroActivity.this, "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(CadastroActivity.this, "The given password is invalid.", Toast.LENGTH_LONG).show();
                senha.setError("The password is invalid it must 6 characters at least");
                senha.requestFocus();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CadastroActivity.this, MainActivity.class));
        super.onBackPressed();
    }
}