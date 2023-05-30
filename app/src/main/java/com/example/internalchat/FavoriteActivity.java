package com.example.internalchat;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FavoriteActivity extends AppCompatActivity {
    private TextView back, title;
    private GridView gridView_Favorite;
    private Toolbar toolbar;
    private FirebaseAuth Auth;
    String timeItem;
    FirebaseFirestore db;
    DataClass data;
    private ArrayList<DataClass> dataModalArrayListFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Phòng yêu thích");

        Auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        dataModalArrayListFavorite = new ArrayList<>();
        back = (TextView) findViewById(R.id.back_favorite);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoriteActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        gridView_Favorite = (GridView) findViewById(R.id.grid_view_Favorite);

        AddListFavorite();


    }

    private void AddListFavorite() {
        FirebaseUser currentUser = Auth.getCurrentUser();
        ArrayList<String> postIds = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Users")
                .child(currentUser.getUid())
                .child("listLike")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            // Lấy postid từ child và thêm nó vào ArrayList
                            String postIdF = postSnapshot.getKey();

                            AddPostFireStore(postIdF);
                            postIds.add(postIdF);
                            Log.d(TAG,postIds+".........");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        Array listPostId = snapshot.child("listLike").getValue(Array.class);
//                        Log.d(TAG,listPostId+".........");
//                    }

    }

    private void AddPostFireStore(String postIdF) {
        db.collection("roomify")
                .document(postIdF)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            List<String> images = (List<String>) document.get("image");
                            String imageURL = images.get(0);
//                              String imageURL = document.getData().get("image").toString();
                            String location = document.getData().get("address").toString();
                            DecimalFormat decimalFormat = new DecimalFormat("#.#");
                            String price = document.getData().get("price").toString();
                            double priceInMillions = Double.parseDouble(String.valueOf(price)) / 1000000;
                            String formattedPrice = decimalFormat.format(priceInMillions);

                            Timestamp timestamp = document.getTimestamp("postingTime");


                            if (timestamp != null) {
                                // Chuyển đổi timestamp thành Date hoặc Calendar
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(timestamp.toDate().getTime());

                                // Tính khoảng cách thời gian giữa thời điểm hiện tại và thời điểm đăng
                                long currentTime = System.currentTimeMillis();
                                long postTime = calendar.getTimeInMillis();
                                long timeDiff = currentTime - postTime;

                                // Chuyển đổi khoảng cách thời gian thành đơn vị đo thời gian
                                long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDiff);
                                long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff);
                                long hours = TimeUnit.MILLISECONDS.toHours(timeDiff);
                                long days = TimeUnit.MILLISECONDS.toDays(timeDiff);

                                // Hiển thị đơn vị thời gian trên TextView
                                if (days > 0) {
                                    timeItem = days + " ngày trước";
                                } else if (hours > 0) {
                                    timeItem = hours + " giờ trước";
                                } else if (minutes > 0) {
                                    timeItem = minutes + " phút trước";
                                } else {
                                    timeItem = seconds + " giây trước";
                                }
                            }


                            String title = document.getData().get("title").toString();
                            String postId = document.getData().get("postId").toString();
//                                    Log.d(postId, "No such document!!!!!!!");
                            data = new DataClass(imageURL, title, formattedPrice, timeItem, location, postId);
                            dataModalArrayListFavorite.add(data);
                            MyAdapter adapter = new MyAdapter(FavoriteActivity.this, dataModalArrayListFavorite);
                            gridView_Favorite.setAdapter(adapter);
                        }
                    }
                });

    }
}