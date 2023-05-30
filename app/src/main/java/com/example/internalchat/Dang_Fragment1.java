package com.example.internalchat;

import static com.example.internalchat.DangFragment.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.firestore.FirebaseFirestore;

public class Dang_Fragment1 extends Fragment {

   private TextView textView1;
   private TextView textView;
   private FirebaseFirestore firebaseFirestore;
   private Toolbar toolbar;
   private ViewPager viewPager;
   public  EditText dien_tich, price_1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dang1,container,false);
        dien_tich = view.findViewById(R.id.edit_dientich);
        price_1 = view.findViewById(R.id.edit_gia);
        TextWatcher addressWatcher = new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String dt = dien_tich.getText().toString().trim();
                String price = price_1.getText().toString().trim();
                home.setPrice(price);
                home.setAcreage(dt);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        dien_tich.addTextChangedListener(addressWatcher);
        price_1.addTextChangedListener(addressWatcher);
        return view;
    }



}
