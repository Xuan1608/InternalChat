package com.example.internalchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TbFragment extends Fragment {

        FloatingActionButton AddVideo;
        private Toolbar toolbar_tb;
        private RecyclerView rcvVideo;
        private ArrayList<ModelVideo> videoArrayList;
        private VideoAdapter videoAdapter;
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_tb, container, false);
            toolbar_tb = view.findViewById(R.id.toolbar_tb);
            toolbar_tb.setTitle("Videos");


           AddVideo = view.findViewById(R.id.addVideo);
           rcvVideo = view.findViewById(R.id.videosRcv);
           LoatVideoFromFirebase();
           AddVideo.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(getContext(),VideoActivity.class);
                   startActivity(intent);
               }
           });

            return view;

        }

    private void LoatVideoFromFirebase() {
            videoArrayList =new ArrayList<>();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Videos");
        databaseReference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            ModelVideo modelVideo = ds.getValue(ModelVideo.class);

                            videoArrayList.add(modelVideo);
                        }
                        videoAdapter = new VideoAdapter(getContext(),videoArrayList);
                        rcvVideo.setAdapter(videoAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}
