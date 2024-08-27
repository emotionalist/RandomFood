package com.cookandroid.suwonrandomfood;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class listmain extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<reviewlist> arrayList;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    int StoreNum;
    Button review_write;
    ImageButton list_back;
    String so = "Store";
    String reviewlist_ = "reviewlist";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listmain);
        this.getSupportActionBar().hide();
        review_write = findViewById(R.id.review_write);
        list_back = findViewById(R.id.list_back);

        list_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        review_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(listmain.this, review.class);
                startActivity(intent);
            }
        });
        onStart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        StoreNum = intent.getIntExtra("StoreNum", 0);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        recyclerView = findViewById(R.id.reviewRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        databaseReference.child("Store").child(so+Integer.toString(StoreNum)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(reviewlist_).exists()) {
                    arrayList.clear();
                    long count = snapshot.child(reviewlist_).getChildrenCount();
                    for (DataSnapshot sn : snapshot.child("reviewlist").getChildren()) {
                        reviewlist re = sn.getValue(reviewlist.class);
                        arrayList.add(re);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                DatabaseError databaseError = null;
                Log.e("listmain", String.valueOf(databaseError.toException()));

            }
        });
        adapter = new CustomAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);
    }
}
