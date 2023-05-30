package com.example.internalchat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register, forgotPassword;
    private EditText loginEmail, loginPassword;
    private Button buttonLogin;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register = (TextView) findViewById(R.id.textCreate);
        register.setOnClickListener(this);

        forgotPassword =(TextView) findViewById(R.id.forget_password) ;
        forgotPassword.setOnClickListener(this);

        buttonLogin = (Button) findViewById(R.id.login_button);
        buttonLogin.setOnClickListener(this);

        loginEmail = (EditText) findViewById(R.id.login_email);
        loginPassword = (EditText) findViewById(R.id.login_password);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textCreate:
                startActivity(new Intent(this, RegisterActivity.class));
                break;

            case R.id.login_button:
                 UserLogin();
                 break;

            case R.id.forget_password:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
        }
    }

    private void UserLogin() {
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        if (email.isEmpty()) {
            loginEmail.setError("Email is required!");
            loginEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.setError("Please enter a valid email!");
            loginEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            loginPassword.setError("Password is required!");
            loginPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            loginPassword.setError("Min password length should be 6 characters!");
            loginPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.isEmailVerified()) {
                        Toast.makeText(LoginActivity.this,"Log in successfully!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else {
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this,"Check your email to verify your account!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
    }
}