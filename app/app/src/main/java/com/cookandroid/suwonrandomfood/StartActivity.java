package com.cookandroid.suwonrandomfood;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StartActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigationview);
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new RecyclerViewFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.allList:
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new RecyclerViewFragment()).commit();
                        return true;
                    case R.id.map:
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new MapFragment()).commit();
                        return true;
                    case R.id.random:
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new RandomFragment()).commit();
                        return true;
                    case R.id.mypage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new MyPageFragment()).commit();
                        return true;
                }
                return false;
            }
        });
    }
}
