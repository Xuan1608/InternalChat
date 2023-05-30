package com.example.internalchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {
    TextView textView_profile;
    TextView textView_logout, text_name;
    TextView textView_favorite, textView_policy, textView_manage;
    ImageView imageView;
    FirebaseDatabase firebaseDatabase;
    Toolbar toolbar_1;
    FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        toolbar_1 = view.findViewById(R.id.toolbar_account);
        toolbar_1.setTitle("Tài Khoản");

        textView_favorite =view.findViewById(R.id.text_Favorite);
        textView_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),FavoriteActivity.class);
                startActivity(intent);
            }
        });
        textView_manage = view.findViewById(R.id.text_manager);
        textView_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ManageActivity.class);
                startActivity(intent);
            }
        });
        textView_policy=view.findViewById(R.id.text_policy);
        textView_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),PolicyActivity.class);
                startActivity(intent);
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        textView_logout = view.findViewById(R.id.text_logout);
        text_name = view.findViewById(R.id.text_name);

        firebaseDatabase.getReference("Users")
                .child((currentUser.getUid())).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String fullname = snapshot.child("fullName").getValue(String.class);
                        text_name.setText(fullname);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        imageView = view.findViewById(R.id.image_account);
        firebaseDatabase.getReference("Users").child(currentUser.getUid()).child("avatar").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String avatarUrl = snapshot.getValue(String.class);

                // Sử dụng Glide để tải ảnh avatar từ đường dẫn URL và hiển thị nó trong ImageView
                Glide.with(getContext()).load(avatarUrl).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi khi không lấy được đường dẫn URL của ảnh avatar từ Firebase Realtime Database
            }
        });
        textView_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                Toast.makeText(getContext(), "Log out successful", Toast.LENGTH_LONG).show();
                getContext().startActivity(intent);
            }
        });


        textView_profile = view.findViewById(R.id.text_profile);
        textView_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), profileActivity.class);
                getContext().startActivity(intent);
            }
        });
        return view;
    }
}
