package com.example.internalchat;

import static android.content.ContentValues.TAG;
import static com.example.internalchat.DangFragment.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.ArrayList;
import java.util.List;


public class Dang_Fragment3 extends Fragment {
    private Button btnSelectImage;
    private RecyclerView rcvPhoto;
    private PhotoAdapter1 photoAdapter1;
    private FirebaseStorage firebaseStorage;
    private TextView textView;
    private ArrayList<Uri> uriArrayList =new ArrayList<>();
    private ArrayList<String> arrayList;
    Uri ImageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dang3, container, false);

        btnSelectImage = view.findViewById(R.id.bottom_chon_anh);
        arrayList = new ArrayList<>();


        rcvPhoto = view.findViewById(R.id.rcv_photo);

        textView = view.findViewById(R.id.total_photo);

        firebaseStorage = FirebaseStorage.getInstance();

        photoAdapter1 = new PhotoAdapter1(uriArrayList);
        rcvPhoto.setLayoutManager(new GridLayoutManager(getActivity(),2));
        rcvPhoto.setAdapter(photoAdapter1);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }

        });


        return view;
    }

    private void requestPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                selectImageFromGallery();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.JELLY_BEAN_MR2){
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&& resultCode== Activity.RESULT_OK){
            if(data.getClipData()!= null){
                int x =data.getClipData().getItemCount();
                for (int i = 0;i<x;i++){
                ImageUri = data.getClipData().getItemAt(i).getUri();
                uriArrayList.add(ImageUri);
                }
                UploadArayyImage();
                photoAdapter1.notifyDataSetChanged();
                textView.setText("Photos ("+uriArrayList.size()+")");
            }else if (data.getData().getPath()!=null){
                String  imageURL = data.getData().getPath();
                uriArrayList.add(Uri.parse(imageURL));
//
            }
        }

    }
    private void UploadArayyImage() {
        for (int i = 0; i < uriArrayList.size(); i++) {
            Uri IndividualImage = uriArrayList.get(i);
            if (IndividualImage != null) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference("Images/").child("User"+":"+IndividualImage.getLastPathSegment()).child("Image" + getId() + ".jpg");
                storageReference.putFile(IndividualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                arrayList.add(String.valueOf(uri));
//                                if(arrayList.size() == uriArrayList.size()){
//                                    for (int i = 0; i < arrayList.size(); i++) {
//                                        Log.d(TAG, arrayList.get(i));
//                                    }
                                    home.setImage(arrayList);
//                                }

//                        home.setImage(url);
//                               Log.d(arrayList, "DocumentSnapshot successfully written!...........");

                            }




                        });
                    }

                });
            }
        }
        Log.d(TAG, "........................"  + uriArrayList.size());
    }


}