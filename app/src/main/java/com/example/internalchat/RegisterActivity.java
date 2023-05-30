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
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private TextView textView;
    private EditText register_name, register_birthday, register_mail, register_password,register_phone_number;
      private ProgressBar progressBar;
    private Button button;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth =FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();


        textView =(TextView) findViewById(R.id.register_text);
        textView.setOnClickListener(this);
        button= (Button) findViewById(R.id.register_button);
        button.setOnClickListener(this);

        register_name=(EditText) findViewById(R.id.register_name);
                register_mail=(EditText) findViewById(R.id.register_email);
        register_password=(EditText) findViewById(R.id.register_password);
        register_phone_number=(EditText) findViewById(R.id.register_phone_number);


        progressBar =(ProgressBar) findViewById(R.id.progressbar);

    }

    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.register_text:
            startActivity(new Intent(this,LoginActivity.class));
            break;
        case R.id.register_button:
            button();
            break;
    }
    }

    private void button() {
        String email = register_mail.getText().toString().trim();
        String password = register_password.getText().toString().trim();
        String fullName =register_name.getText().toString().trim();
        String phone = register_phone_number.getText().toString().trim();

        if(fullName.isEmpty()){
            register_name.setError("Full name is required");
            register_name.requestFocus();
            return;
        }
        if(email.isEmpty()) {
            register_mail.setError("Email is required");
            register_mail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            register_mail.setError("Please provide valid email!");
            register_mail.requestFocus();
            return;

        }
        if(phone.isEmpty()) {
            register_phone_number.setError("Phone number is required");
            register_phone_number.requestFocus();
            return;
        }
        if(password.isEmpty()){
            register_password.setError("Password is required");
            register_password.requestFocus();
            return;
        }
        if(password.length()<6){
            register_password.setError("Min password length should be 6 characters!");
            register_password.requestFocus();
            return;
        }
        FirebaseUser currentUser = mAuth.getCurrentUser();
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(fullName,email,phone);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(currentUser.getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                                Toast.makeText(RegisterActivity.this,"User has been register successfully!",Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);

                                                }
                                            }

                                    });
                        }

                    }
                });

    }
}