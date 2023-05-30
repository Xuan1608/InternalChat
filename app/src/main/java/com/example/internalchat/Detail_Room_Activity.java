package com.example.internalchat;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Detail_Room_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView back_detail, Map;
    private TextView Location, acreage, Phone, Price, description, Title, time, favorite;
    private ImageView img;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    String postId;
    private ImageSlider imageSlider;
    private boolean isTextViewSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_room);


        Intent intent = getIntent();
        postId = intent.getStringExtra("id");

        Log.d(TAG, postId + "DocumentSnapshot successfully written!");

        mAuth = FirebaseAuth.getInstance();


        db = FirebaseFirestore.getInstance();
        Title = (TextView) findViewById(R.id.title_detail);
        Location = (TextView) findViewById(R.id.location_item_detail);
        acreage = (TextView) findViewById(R.id.acreage_item_detail);
        Phone = (TextView) findViewById(R.id.phone_item_detail);
        Price = (TextView) findViewById(R.id.price_item_detail);
        time = (TextView) findViewById(R.id.time_item_detail);
        description = (TextView) findViewById(R.id.description_item_detail);

        Map = (TextView) findViewById(R.id.map_detail);
        Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_1 = new Intent(Detail_Room_Activity.this,MapActivity.class);
                intent_1.putExtra("Id",postId);
                startActivity(intent_1);
            }
        });


        imageSlider = (ImageSlider) findViewById(R.id.imageSlider);

        loadDataDetailRoom();
        toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chi tiết phòng");

        back_detail = (TextView) findViewById(R.id.back_detail);
        back_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Detail_Room_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        favorite = (TextView) findViewById(R.id.tym_detail);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTextViewSelected) {
                    // Đổi màu của drawable trở lại màu cũ
                    favorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_outline_favorite_border_24, 0, 0, 0);
                    isTextViewSelected = false;
                    RemoveListFavorite();

                } else {
                    // Thay đổi màu của drawable thành màu mới
                    favorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_favorite_24, 0, 0, 0);
                    isTextViewSelected = true;
                    AddListFavorite();
                }
            }
        });
    }

    private void RemoveListFavorite() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseDatabase.getInstance().getReference("Users")
                .child(currentUser.getUid())
                .child("listLike")
                .child(postId)
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Detail_Room_Activity.this,"Xóa khỏi danh sách Phòng yêu thích thành công !!!",Toast.LENGTH_SHORT).show();
                    }

                });

    }

    private void loadDataDetailRoom() {
        Intent intent = getIntent();
        String postId = intent.getStringExtra("id");

        db.collection("roomify")
                .document(postId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            // Document found in the offline cache
                            DocumentSnapshot document = task.getResult();
//                    String imageURL = document.getData().get("imageURL").toString();
                            String location = document.getData().get("address").toString();
                            Location.setText(location);
                            DecimalFormat decimalFormat = new DecimalFormat("#.#");
                            String price = document.getData().get("price").toString();
                            double priceInMillions = Double.parseDouble(String.valueOf(price)) / 1000000;
                            String formattedPrice = decimalFormat.format(priceInMillions);
                            Price.setText(formattedPrice + " " + "triệu/tháng");
                            String title = document.getData().get("title").toString();
                            Title.setText(title);
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
                                String timeItem;
                                if (days > 0) {

                                    timeItem = days + " ngày trước";
                                } else if (hours > 0) {
                                    timeItem = hours + " giờ trước";
                                } else if (minutes > 0) {
                                    timeItem = minutes + " phút trước";
                                } else {
                                    timeItem = seconds + " giây trước";


                                }
                                time.setText(" " + "Đăng" + " " + timeItem);
                            }
                            String Acreage = document.getData().get("acreage").toString();
                            acreage.setText(Acreage + " " + "m2");
//                    String phone = document.getData().get("").toString();
//                    Phone.setText(phone);
//                    List<String> images = (List<String>) document.get("image");
//                    for (int i = 0; i < 5; i++) {
//                    String imageURL = images.get(i);
//                        imageSlider.setImageList();}
//                    Picasso.get().load(imageURL).into(img);
                            if (document.exists()) {
                                List<String> imageUrls = (List<String>) document.get("image");

                                List<SlideModel> slideModels = new ArrayList<>();
                                for (String imageUrl : imageUrls) {
                                    slideModels.add(new SlideModel(imageUrl, ScaleTypes.FIT));
                                    imageSlider.setImageList(slideModels, ScaleTypes.FIT);
                                    Log.d(TAG, "Cached document data: " + document.getData().get("image").toString());
                                }
                            }

                            String Description = document.getData().get("description").toString();
                            description.setText(Description);
                            Log.d(TAG, "Cached document data: " + document.getData());
                        } else {
                            Log.d(TAG, "Cached get failed: ", task.getException());
                        }
                    }
                });


    }

    private void AddListFavorite() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseDatabase.getInstance().getReference("Users")
                .child(currentUser.getUid())
                .child("listLike")
                .child(postId)
                .setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Detail_Room_Activity.this,"Thêm Phòng yêu thích thành công !!!",Toast.LENGTH_SHORT).show();
                    }

                });
    }
}