package com.cookandroid.suwonrandomfood;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class review extends AppCompatActivity {
    Button cancelButton;
    Button okButton;
    AlertDialog dialog;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseUser user;
    int StoreNum;

    EditText titleEdit;
    EditText reviewEdit;
    TextView reviewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review);
        this.getSupportActionBar().hide();

        Intent intent = getIntent();
        StoreNum = intent.getIntExtra("StoreNum", 0);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        cancelButton = findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        user = FirebaseAuth.getInstance().getCurrentUser();
        reviewEdit = findViewById(R.id.reviewEdit);
        titleEdit = findViewById(R.id.titleEdit);
        reviewText = findViewById(R.id.reviewText);

        //확인버튼 클릭시
        okButton = findViewById(R.id.okButton);
        String so = "Store";
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("Store").child(so+Integer.toString(StoreNum)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("reviewlist").exists()){
                            long count = snapshot.child("reviewlist").getChildrenCount();
                            for(int i=0;i<count;i++){
                                if(!snapshot.child("reviewlist").child(String.valueOf(i)).exists()){
                                    databaseReference.child("Store").child(so+String.valueOf(StoreNum)).child("reviewlist")
                                            .child(String.valueOf(i)).child("content").setValue(reviewEdit.getText().toString());
                                    databaseReference.child("Store").child(so+String.valueOf(StoreNum)).child("reviewlist")
                                            .child(String.valueOf(i)).child("title").setValue(titleEdit.getText().toString());
                                    databaseReference.child("Store").child(so+String.valueOf(StoreNum)).child("reviewlist")
                                            .child(String.valueOf(i)).child("id").setValue(user.getUid());
                                }else if(!snapshot.child("reviewlist").child(String.valueOf(count)).exists()){
                                    databaseReference.child("Store").child(so+String.valueOf(StoreNum)).child("reviewlist")
                                            .child(String.valueOf(count)).child("content").setValue(reviewEdit.getText().toString());
                                    databaseReference.child("Store").child(so+String.valueOf(StoreNum)).child("reviewlist")
                                            .child(String.valueOf(count)).child("title").setValue(titleEdit.getText().toString());
                                    databaseReference.child("Store").child(so+String.valueOf(StoreNum)).child("reviewlist")
                                            .child(String.valueOf(count)).child("id").setValue(user.getUid());
                                    continue;
                                }
                            }
                        }else if(!snapshot.child("reviewlist").exists()){
                            databaseReference.child("Store").child(so+String.valueOf(StoreNum)).child("reviewlist")
                                    .child(String.valueOf(0)).child("content").setValue(reviewEdit.getText().toString());
                            databaseReference.child("Store").child(so+String.valueOf(StoreNum)).child("reviewlist")
                                    .child(String.valueOf(0)).child("title").setValue(titleEdit.getText().toString());
                            databaseReference.child("Store").child(so+String.valueOf(StoreNum)).child("reviewlist")
                                    .child(String.valueOf(0)).child("id").setValue(user.getUid());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


                AlertDialog.Builder builder = new AlertDialog.Builder(review.this);
                dialog = builder.setMessage("리뷰작성이 완료되었습니다.")
                        .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create();
                dialog.show();
            }
        });
    }
}

