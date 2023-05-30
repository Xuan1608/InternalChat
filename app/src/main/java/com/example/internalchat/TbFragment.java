package com.example.internalchat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

    public class TbFragment extends Fragment {
        private Toolbar toolbar_tb;
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_tb, container, false);
            toolbar_tb = view.findViewById(R.id.toolbar_tb);
            toolbar_tb.setTitle("Thông Báo");
            return view;

        }

}
