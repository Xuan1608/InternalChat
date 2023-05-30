package com.example.internalchat;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.internalchat.widget.CustomViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigationView;
    private CustomViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);

        viewPager=(CustomViewPager) findViewById(R.id.View_pager);
        viewPager.setPagingEnabled(false);
        setUpViewPager();
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home:
                        Toast.makeText(MainActivity.this, "Trang chủ",Toast.LENGTH_SHORT).show();
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.action_chat:
                        Toast.makeText(MainActivity.this, "Chat",Toast.LENGTH_SHORT).show();
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.action_dang:
                        Toast.makeText(MainActivity.this, "Đăng bài",Toast.LENGTH_SHORT).show();
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.action_tb:
                        Toast.makeText(MainActivity.this, "Thông báo",Toast.LENGTH_SHORT).show();
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.action_account:
                        Toast.makeText(MainActivity.this, "Tài khoản",Toast.LENGTH_SHORT).show();
                        viewPager.setCurrentItem(4);
                        break;
                }
                return true;
            }
        });

    }
    private void setUpViewPager(){
    ViewPagerAdapter viewPagerAdapter =new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    viewPager.setAdapter(viewPagerAdapter);

    }
}