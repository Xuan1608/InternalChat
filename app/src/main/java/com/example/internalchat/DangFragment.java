package com.example.internalchat;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.internalchat.widget.CustomViewPager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DangFragment extends Fragment {
    Toolbar toolbar;
    private CustomViewPager viewPager;
    private ViewPagerAdapter_1 viewPagerAdapter;
    private FragmentManager fragmentManager;
    private TextView textView;
    private View item;
    private FirebaseAuth firebaseAuth;
    public static HomeRental home = new HomeRental();

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dang, container, false);

        toolbar = view.findViewById(R.id.toolbar_dang);
        textView = view.findViewById(R.id.back1);
        textView.setVisibility(View.GONE);

        item = view.findViewById(R.id.action_text_dang);

        viewPager = view.findViewById(R.id.View_pager_dang);
        viewPager.setPagingEnabled(false);
        ViewPagerAdapter_2 pagerAdapter = new ViewPagerAdapter_2(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position > 0) {
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.GONE);
                }
            }


            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Đăng Bài");
        setHasOptionsMenu(true);
        toolbar.inflateMenu(R.menu.menu_dang_bai);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() > 0) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                }
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dang_bai, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_text_dang) {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem >= 2) {
                item.setTitle("Xác Nhận");
            } else {
                item.setTitle("Tiếp Theo");
            }
            if(currentItem > 2)
            {
                loadData();
            }

            viewPager.setCurrentItem(currentItem + 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void loadData() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> home_add = new HashMap<>();
        home_add.put("image", home.getImage());
        home_add.put("acreage",home.getAcreage());
        home_add.put("address",home.getAddress());
        home_add.put("description",home.getDescription());
        home_add.put("phone",home.getPhone());
        home_add.put("price",home.getPrice());
        home_add.put("title",home.getTitle());
        home_add.put("postId",home.getPostId());
        home_add.put("userID",currentUser.getUid());
        home_add.put("postingTime", new Timestamp(new Date()));

        db.collection("roomify").add(home_add)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        String Post_id = documentReference.getId();
                        Log.d(TAG, "!!!!!!!!: " + Post_id);
                        update(Post_id);
                    }


                    private void update(String id) {
                        DocumentReference washingtonRef = db.collection("roomify").document(id);
                        washingtonRef
                                .update("postId", id)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                        Toast.makeText(getContext(), "Đăng bài thành công !!!", Toast.LENGTH_SHORT).show();
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                // Thực hiện tác vụ sau 1 giây
                                                Intent intent = new Intent(getContext(),MainActivity.class);
                                                getContext().startActivity(intent);
                                            }
                                        }, 4000);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });
                    }

                });
    }



}