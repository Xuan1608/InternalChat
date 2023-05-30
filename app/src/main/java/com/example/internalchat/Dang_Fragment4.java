package com.example.internalchat;


import static com.example.internalchat.DangFragment.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Dang_Fragment4 extends Fragment {

    private EditText title,sdt,description;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dang4,container,false);
        title = view.findViewById(R.id.edit_title);
        sdt = view.findViewById(R.id.edit_sdt);
        description = view.findViewById(R.id.edit_description);
        TextWatcher addressWatcher = new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String getTitle = title.getText().toString();
                String getSdt = sdt.getText().toString();
                String getDes = description.getText().toString();
                home.setTitle(getTitle);
                home.setPhone(getSdt);
                home.setDescription(getDes);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        title.addTextChangedListener(addressWatcher);
        sdt.addTextChangedListener(addressWatcher);
        description.addTextChangedListener(addressWatcher);

        return view;
    }
}
