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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Dang_Fragment2 extends Fragment {
    private TextView textView2, back2;
    private EditText etAddress1, etAddress2, etAddress3, etAddress4, etFullAddress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dang2, container, false);
        etAddress1 = view.findViewById(R.id.etAddress1);
        etAddress2 = view.findViewById(R.id.etAddress2);
        etAddress3 = view.findViewById(R.id.etAddress3);
        etAddress4 = view.findViewById(R.id.etAddress4);
        etFullAddress = view.findViewById(R.id.etFullAddress);

        TextWatcher addressWatcher = new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String address1 = etAddress1.getText().toString().trim();
                String address2 = etAddress2.getText().toString().trim();
                String address3 = etAddress3.getText().toString().trim();
                String address4 = etAddress4.getText().toString().trim();
                String fullAddress = address4 + ", " + address3 + ", " + address2 + ", " + address1;
                etFullAddress.setText(fullAddress);
                String address = etFullAddress.getText().toString();
                home.setAddress(address);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        etAddress1.addTextChangedListener(addressWatcher);
        etAddress2.addTextChangedListener(addressWatcher);
        etAddress3.addTextChangedListener(addressWatcher);
        etAddress4.addTextChangedListener(addressWatcher);
        
        return view;
    }
}
