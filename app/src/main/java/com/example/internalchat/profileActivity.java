package com.example.internalchat;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class profileActivity extends AppCompatActivity {
    Button buttonEdit;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage reference;
    private FirebaseAuth mAuth;
    private String userId;
    private String userIdF;

    private TextView text_name;
    private TextView text_location, text_phone, text_email, text_birthday;
    private TextView text_gioi_tinh, back;
    private ImageView imageView;
    private ImageButton chooseImageButton;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Toolbar toolbar_2;
    private Uri imageUri;
    private GridView gridViewF;
    private ArrayList<DataClass> dataModalArrayListF;
    String timeItem;
    DataClass data;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar_2 = (Toolbar) findViewById (R.id.toolbar_profile);
        setSupportActionBar(toolbar_2);
        getSupportActionBar().setTitle("Trang cá nhân");

        back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profileActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        text_gioi_tinh= (TextView) findViewById(R.id.gioi_tinh_1);
        text_name = (TextView) findViewById(R.id.text_name);
        text_birthday = (TextView) findViewById(R.id.birday_profile);
        text_location = (TextView) findViewById(R.id.location_profile);
        text_phone = (TextView) findViewById(R.id.phone_profile);
        text_email = (TextView) findViewById(R.id.email_profile);

        gridViewF = (GridView) findViewById (R.id.grid_viewF);
        dataModalArrayListF = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userId = currentUser.getUid();

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
                                        loadDatainGridViewF(postIdF);}
                                    }
                                }
                            }
                        });


        profile();
        buttonEdit = (Button) findViewById(R.id.edit_profile);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profileActivity.this, Edit_ProfileActivity.class);
                startActivity(intent);
            }
        });
        chooseImageButton = (ImageButton) findViewById(R.id.them_anh);
        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        imageView = (ImageView) findViewById(R.id.image_account);
        FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid()).child("avatar").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String avatarUrl = snapshot.getValue(String.class);

                // Sử dụng Glide để tải ảnh avatar từ đường dẫn URL và hiển thị nó trong ImageView
                Glide.with(profileActivity.this).load(avatarUrl).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi khi không lấy được đường dẫn URL của ảnh avatar từ Firebase Realtime Database
            }
        });
    }

    private void loadDatainGridViewF(String id) {
        Log.d(TAG,userId+"......");
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
                                    dataModalArrayListF.add(data);
                                    MyAdapter adapter = new MyAdapter(profileActivity.this, dataModalArrayListF);
                                    gridViewF.setAdapter(adapter);
                                }
                            }
                    });

        }


    private void profile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        firebaseDatabase.getReference("Users")
                .child((currentUser.getUid()))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String fullname = snapshot.child("fullName").getValue(String.class);
                        text_name.setText(fullname);

                        String gioi_tinh = snapshot.child("gioi tinh").getValue(String.class);
                        text_gioi_tinh.setText(gioi_tinh);

                        String birthday = snapshot.child("birthday").getValue(String.class);
                        text_birthday.setText(birthday);
                        String location = snapshot.child("address").getValue(String.class);
                        text_location.setText(location);
                        String phone = snapshot.child("phone").getValue(String.class);
                        text_phone.setText(phone);
                        String email = snapshot.child("email").getValue(String.class);
                        text_email.setText(email);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadAvatar(imageUri);
            

        }
    }

    private void uploadAvatar(Uri uri) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("AvatarUser/").child(currentUser.getUid()).child("avatar"+".jpg");
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            Log.d(url, "DocumentSnapshot successfully written!...........");

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
                            databaseReference.child("avatar").setValue(url);
                            Toast.makeText(profileActivity.this,"Upload Successful !!",Toast.LENGTH_SHORT).show();
                            Glide.with(profileActivity.this)
                                    .load(url)
                                    .into(imageView);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(profileActivity.this,"Upload Failed !!",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                }
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
//             uploadtoFirebase(imageUri);
//            }
//        });
//    }
//
//    private void uploadtoFirebase(Uri uri) {
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        StorageReference fileRef = reference.getReference("AvatarUser").child(currentUser.getUid());
//        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//             fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                 @Override
//                 public void onSuccess(Uri uri) {
//                     Model model = new Model(uri.toString());
//                     firebaseDatabase.getReference("Users").child(currentUser.getUid()).setValue(model);
//
//                     Toast.makeText(profileActivity.this,"Upload Successful !!",Toast.LENGTH_SHORT).show();
//                 }
//             });
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//                Toast.makeText(profileActivity.this,"Upload Failed !!",Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private String getFileExtension(Uri muri) {
//        ContentResolver contentResolver= getContentResolver();
//        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(muri));
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==2 && resultCode == RESULT_OK && data != null){
//            imageUri = data.getData();
//            imageView.setImageURI(imageUri);
//
//        }
//   }
