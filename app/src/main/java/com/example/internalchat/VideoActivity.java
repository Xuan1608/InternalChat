package com.example.internalchat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class VideoActivity extends AppCompatActivity {
    private EditText editTitle;
    private VideoView videoView;
    private Button button;
    private FloatingActionButton floatingActionButton;

    private static final int VIDEO_PICK_GALLERY_CODE = 100;
    private static final int VIDEO_PICK_CAMERA_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    private String[] cameraPermissions;
    private ProgressDialog progressDialog;

    private Uri videoUri = null;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Vui lòng chờ ");
        progressDialog.setMessage("Uploading Video ");
        progressDialog.setCanceledOnTouchOutside(false);
        editTitle = findViewById(R.id.title_video);
        videoView = findViewById(R.id.video);
        button = findViewById(R.id.upload_video);

        floatingActionButton = findViewById(R.id.addVideo1);
        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTitle.getText().toString().trim();
                if(TextUtils.isEmpty(title)){
                    Toast.makeText(VideoActivity.this,"Vui lòng nhập Title !!!",Toast.LENGTH_SHORT).show();
                }else if (videoUri==null){

                    Toast.makeText(VideoActivity.this,"Vui lòng chọn Video !!!",Toast.LENGTH_SHORT).show();
                }else {
                    UploadVideoFirebase();
                }

            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoPickDialog();
            }
        });

    }

    private void UploadVideoFirebase() {
        progressDialog.show();
        String timestamp = ""+System.currentTimeMillis();
        String filePathName= "Video/"+"video"+timestamp;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathName);
        storageReference.putFile(videoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()){
                            Uri downloadUri = uriTask.getResult();
                            if(uriTask.isSuccessful()){
                                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("title",""+title);
                                hashMap.put("timestamp",""+ timestamp);
                                hashMap.put("VideoUrl",""+ downloadUri);
                                hashMap.put("UserId",firebaseAuth.getUid());
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Videos");
                                databaseReference
                                        .setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                Toast.makeText(VideoActivity.this,"Đăng video thành công !!",Toast.LENGTH_SHORT).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(VideoActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(VideoActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void VideoPickDialog() {
        String[] options = {"Camera","Gallery"};

        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("Pick Video From")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (i==0){
                            if (!checkCameraPermission()){
                                requestCameraPermission();
                            }else {
                                videoPickCamera();
                            }
                        }
                        else if (i==1){
                            videoPickGallery();
                        }
                    }
                })
                .show();
    }
    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST_CODE);

    }
    private boolean checkCameraPermission(){
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK)== PackageManager.PERMISSION_GRANTED;

        return result1&&result2;
    }
    private void videoPickGallery(){
        Intent intent = new Intent();
        intent.setType("Video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Chọn Videos"),VIDEO_PICK_GALLERY_CODE);
    }
    private void videoPickCamera(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent,VIDEO_PICK_CAMERA_CODE);
    }
    private void SetVideo() {
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);

        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                videoView.pause();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if (grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                           videoPickCamera();
                    }else {
                        Toast.makeText(this,"Camera & Storage permission required !!",Toast.LENGTH_SHORT).show();
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==RESULT_OK) {
            if (requestCode == VIDEO_PICK_GALLERY_CODE) {
                videoUri = data.getData();
                SetVideo();
            } else if (requestCode == VIDEO_PICK_CAMERA_CODE) {
                videoUri = data.getData();
                SetVideo();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}