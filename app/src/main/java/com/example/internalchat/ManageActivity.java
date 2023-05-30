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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ManageActivity extends AppCompatActivity {
    TextView textView_manage;
    Toolbar toolbar_1;
    private FirebaseAuth mAuth;
    String userId;
    private String userIdF;
    private FirebaseFirestore db;
    private FirebaseStorage reference;
    String timeItem;
    private GridView gridView_manage;
    private DataClass data;
    private ArrayList<DataClass> dataModalArrayList;

    private FirebaseDatabase firebaseDatabase;

    TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userId = currentUser.getUid();

        gridView_manage= (GridView) findViewById(R.id.grid_view_manage);
        dataModalArrayList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        toolbar_1 = (Toolbar) findViewById(R.id.toolbar_manage);
        toolbar_1.setTitle("Quản lý đăng bài");

        back = (TextView) findViewById(R.id.back_manager);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        reference = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        db.collection("roomify")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userIdF = document.get("userID").toString();
                                String postIdF = document.getData().get("postId").toString();
                                if(userIdF.equals(userId)){
                                    Log.d(userIdF, "No such document!!!!!!!2");
                                    addDataDang(postIdF);}
                            }
                        }
                    }
                });


    }

    private void addDataDang(String id) {
        Log.d(TAG, id + "......");
        db.collection("roomify")
                .document(id)
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
                            dataModalArrayList.add(data);
                            MyAdapter_Manage adapter_1 = new MyAdapter_Manage(ManageActivity.this, dataModalArrayList);
                            gridView_manage.setAdapter(adapter_1);
                        }
                    }
                });

    }
}
