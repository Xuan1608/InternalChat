package com.example.internalchat;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String postId = intent.getStringExtra("Id");

        // Khởi tạo MapFragment
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_g);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Lấy dữ liệu từ Firestore và hiển thị lên bản đồ
        fetchAddressesFromFirestore();
    }

    private void fetchAddressesFromFirestore() {
        Intent intent = getIntent();
        String postId = intent.getStringExtra("Id");
        Log.d(TAG,postId+"..............");
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
                            String address = document.getString("address");
                            if (address != null) {
                                convertAddressToLatLng(address);
                            }
                        }
                    }
                });
    }

    private void convertAddressToLatLng(String address) {
        Geocoder geocoder = new Geocoder(getApplicationContext());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && addresses.size() > 0) {
                Address addressResult = addresses.get(0);
                double latitude = addressResult.getLatitude();
                double longitude = addressResult.getLongitude();
                LatLng location = new LatLng(latitude, longitude);
                addMarkerToMap(location, address);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addMarkerToMap(LatLng location, String address) {
        if (googleMap != null) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(location)
                    .title(address);
            Marker marker = googleMap.addMarker(markerOptions);
            marker.showInfoWindow();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18));
        }
    }
}