package com.cookandroid.suwonrandomfood;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StoreActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String uid = user.getUid();

    HashMap<String, Object> childUpdates;
    Map<String, Object> userValue;
    Store store;
    ImageButton back;
    Button starscore, review;
    ToggleButton exp, wish;
    TextView name, desc;
    ImageView logo;
    RatingBar ratingBar;
    View dlgView;
    ArrayList<Menu> menu = new ArrayList<>();
    ListView listView;

    String sname, sdesc, sstar, simg, resultstr, userStar, dbStar;
    int snum;
    boolean sec, swc;
    double result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storemain);
        this.getSupportActionBar().hide();

        back = findViewById(R.id.back);
        exp = findViewById(R.id.exp);
        wish = findViewById(R.id.wish);
        name = findViewById(R.id.name);
        logo = findViewById(R.id.logo);
        starscore = findViewById(R.id.starscore);
        desc = findViewById(R.id.desc);
        review = findViewById(R.id.review);
        listView = findViewById(R.id.menulist);
        StoreAdapter storeAdapter = new StoreAdapter(this, menu);
        listView.setAdapter(storeAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        snum = intent.getIntExtra("snum", 0);
        sname = intent.getStringExtra("sname");
        sdesc = intent.getStringExtra("sdesc");
        sstar = intent.getStringExtra("sstar");
        simg = intent.getStringExtra("simg");
        sec = intent.getBooleanExtra("ec", false);
        swc = intent.getBooleanExtra("wc", false);

        Intent intent1 = new Intent(getApplicationContext(), com.cookandroid.suwonrandomfood.review.class);
        intent1.putExtra("StoreNum", snum);

        Intent intent2 = new Intent(getApplicationContext(), listmain.class);
        intent2.putExtra("StoreNum", snum);

        dbStar = sstar;
        name.setText(sname);
        desc.setText(sdesc);
        starscore.setText(sstar);
        Glide.with(this)
                .load(simg)
                .placeholder(R.drawable.cloud)
                .error(R.drawable.error)
                .into(logo);

        if(sec == true) exp.setChecked(true);
        else exp.setChecked(false);

        if(swc == true) wish.setChecked(true);
        else wish.setChecked(false);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreActivity.this, listmain.class);
                startActivity(intent);
            }

        });

        starscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlgView = (View) View.inflate(StoreActivity.this, R.layout.stardialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(StoreActivity.this);
                dlg.setCancelable(false);
                dlg.setTitle("별점 주기");
                dlg.setView(dlgView);
                ratingBar = dlgView.findViewById(R.id.ratingBar);

                //사용자가 임의로 별점을 바꿀 수 있도록 함
                ratingBar.setIsIndicator(false);
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        userStar = "" + v;
                    }
                });

                dlg.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result = (Double.parseDouble(userStar) + Double.parseDouble(dbStar)) / 2;
                        resultstr = Double.toString(result);
                        starscore.setText(resultstr);
                        sstar = resultstr;

                        childUpdates = new HashMap<>();
                        store = new Store(snum, sname, sdesc, sstar, simg, sec, swc);
                        userValue = store.toMap();

                        childUpdates.put(uid+"/Store/Store"+snum, userValue);
                        databaseReference.updateChildren(childUpdates);
                    }
                });

                dlg.setNegativeButton("취소", null);

                dlg.show();
            }
        });

        exp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    addData(snum, sname, sdesc, sstar, simg, true, swc);
                    exp.setChecked(true);

                    childUpdates = new HashMap<>();
                    store = new Store(snum, sname, sdesc, sstar, simg, true, swc);
                    userValue = store.toMap();

                    childUpdates.put(uid+"/Store/Store"+snum, userValue);
                    databaseReference.updateChildren(childUpdates);
                }
                else{
                    databaseReference = database.getReference();
                    databaseReference.child(uid).child("Exp").removeValue();
                    exp.setChecked(false);

                    childUpdates = new HashMap<>();
                    store = new Store(snum, sname, sdesc, sstar, simg, false, swc);
                    userValue = store.toMap();
                    childUpdates.put(uid+"/Store/Store"+snum, userValue);
                    databaseReference.updateChildren(childUpdates);
                }
            }
        });

        wish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    addData2(snum, sname, sdesc, sstar, simg, sec, true);
                    wish.setChecked(true);

                    childUpdates = new HashMap<>();
                    store = new Store(snum, sname, sdesc, sstar, simg, sec, true);
                    userValue = store.toMap();

                    childUpdates.put(uid + "/Store/Store" + snum, userValue);
                    databaseReference.updateChildren(childUpdates);
                } else {
                    databaseReference = database.getReference();
                    databaseReference.child(uid).child("Wish").removeValue();
                    wish.setChecked(false);

                    childUpdates = new HashMap<>();
                    store = new Store(snum, sname, sdesc, sstar, simg, sec, false);
                    userValue = store.toMap();
                    childUpdates.put(uid + "/Store/Store" + snum, userValue);
                    databaseReference.updateChildren(childUpdates);
                }
            }
        });
    }

    public void addData(int snum, String sname, String sdesc, String sstar, String simg, boolean sec, boolean swc) {
        Store store = new Store(snum, sname, sdesc, sstar, simg, sec, swc);
        databaseReference.child(uid).child("Exp").child(sname).setValue(store);
    }

    public void addData2(int snum, String sname, String sdesc, String sstar, String simg, boolean sec, boolean swc) {
        Store store = new Store(snum, sname, sdesc, sstar, simg, sec, swc);
        databaseReference.child(uid).child("Wish").child(sname).setValue(store);
    }
}
