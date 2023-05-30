package com.example.internalchat;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Edit_ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText edit_name, edit_birthday, edit_gioi_tinh, edit_phone, edit_location;
    private ProgressBar progressBar;
    private Button button;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        button = (Button) findViewById(R.id.edit_button);
        button.setOnClickListener(this);

        edit_birthday = (EditText) findViewById(R.id.edit_age);
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_gioi_tinh = (EditText) findViewById(R.id.edit_password);
        edit_phone = (EditText) findViewById(R.id.edit_phone_number);
        edit_location = (EditText) findViewById(R.id.edit_location);


        progressBar = (ProgressBar) findViewById(R.id.progressbar);

    }

    @Override
    public void onClick(View v) {
        button();
    }

    private void button() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        progressBar.setVisibility(View.VISIBLE);

        FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid()).child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email = dataSnapshot.getValue(String.class);



                String gioi_tinh = edit_gioi_tinh.getText().toString();
                String fullName = edit_name.getText().toString();
                String birthday = edit_birthday.getText().toString();
                String phone = edit_phone.getText().toString();
                String address = edit_location.getText().toString();


                if (!fullName.isEmpty()) {
                    mDatabase.getReference("Users").child(currentUser.getUid()).child("fullName").setValue(fullName);;
                }
                if (!birthday.isEmpty()) {
                    mDatabase.getReference("Users").child(currentUser.getUid()).child("birthday").setValue(birthday);
                }
                if (!phone.isEmpty()) {
                    mDatabase.getReference("Users").child(currentUser.getUid()).child("phone").setValue(phone);
                }
                if (!address.isEmpty()) {
                    mDatabase.getReference("Users").child(currentUser.getUid()).child("address").setValue(address);
                }
                if (!gioi_tinh.isEmpty()) {
                    mDatabase.getReference("Users").child(currentUser.getUid()).child("gioi tinh").setValue(gioi_tinh);
                }
                FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid()).child("email").setValue(email);
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(Edit_ProfileActivity.this,profileActivity.class));
                Toast.makeText(Edit_ProfileActivity.this,"User has been Edit Profile successfully!", Toast.LENGTH_LONG).show();                                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
//
//
        });
    }
}