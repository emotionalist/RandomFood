package com.cookandroid.suwonrandomfood;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class RandomFragment extends Fragment {

    Random random;
    FirebaseUser user;
    FirebaseAuth mAuth;
    String uid;
    Button choicebtn;
    RadioGroup rgroup;
    RadioButton rbstore, rbmenu;
    CardView cardview1, cardview2, cardview3;
    ImageView cardimage1, cardimage2, cardimage3;
    TextView cardtitle, carddesc, cardtitle2, carddesc2;
    String storename, storedesc, storestar;
    String storeimg;
    int storenum;
    int num;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_random, container, false);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        random = new Random();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uid = user.getUid();
        choicebtn = rootView.findViewById(R.id.choicebtn);
        rgroup = rootView.findViewById(R.id.rgroup);
        rbmenu = rootView.findViewById(R.id.rbmenu);
        rbstore = rootView.findViewById(R.id.rbstore);
        cardview1 = rootView.findViewById(R.id.cardview1);
        cardview2 = rootView.findViewById(R.id.cardview2);
        cardview3 = rootView.findViewById(R.id.cardview3);
        cardimage1 = rootView.findViewById(R.id.cardimage1);
        cardimage2 = rootView.findViewById(R.id.cardimage2);
        cardimage3 = rootView.findViewById(R.id.cardimage3);
        cardtitle =rootView. findViewById(R.id.cardtitle);
        carddesc = rootView.findViewById(R.id.carddesc);
        cardtitle2 =rootView. findViewById(R.id.cardtitle2);
        carddesc2 = rootView.findViewById(R.id.carddesc2);

        choicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num = random.nextInt(2);
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(uid).child("Store");
                Query query = mDatabase.orderByChild("num").equalTo(num);
                //메뉴별, 가게별 추천? 어떤식으로 할건지 정한 다음에 다시
                switch (rgroup.getCheckedRadioButtonId()) {
                    case R.id.rbstore:
                        cardview1.setVisibility(View.VISIBLE);
                        cardview2.setVisibility(View.INVISIBLE);
                        cardview3.setVisibility(View.INVISIBLE);

                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    Store st = dataSnapshot.getValue(Store.class);
                                    cardtitle.setText(st.name);
                                    storeimg = st.img;
                                    storename = st.name;
                                    storedesc = st.desc;
                                    storestar = st.star;
                                    storenum = st.num;
                                    Glide.with(getActivity())
                                            .load(storeimg)
                                            .placeholder(R.drawable.cloud)
                                            .error(R.drawable.error)
                                            .into(cardimage1);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.w("RandomFragment","onCancelled");
                            }
                        });
                        break;

                    case R.id.rbmenu:
                        cardview2.setVisibility(View.VISIBLE);
                        cardview3.setVisibility(View.INVISIBLE);
                        cardview1.setVisibility(View.INVISIBLE);

                        //cardimage2.setImageResource(menuimg[num]);
                        //carddesc.setText(menuname[num]);
                        break;


                    case R.id.rbmood:
                        String[] items = new String[]{"기분 좋을 때", "슬플 때", "화날 때"};
                        int[] selectIndex = {0};

                        AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                        dlg.setTitle("원하는 음식 종류를 선택하세요")
                                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        selectIndex[0] = which;
                                    }
                                })
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Toast.makeText(getActivity(), items[selectIndex[0]], Toast.LENGTH_SHORT).show();
                                        cardview3.setVisibility(View.VISIBLE);
                                        cardview1.setVisibility(View.INVISIBLE);
                                        cardview2.setVisibility(View.INVISIBLE);

                                        switch (selectIndex[0]) {
                                            case 0:
                                                query.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                            Store st = dataSnapshot.getValue(Store.class);
                                                            cardtitle2.setText(st.name);
                                                            carddesc2.setText(st.desc);
                                                            storeimg = st.img;
                                                            Glide.with(getActivity())
                                                                    .load(storeimg)
                                                                    .placeholder(R.drawable.cloud)
                                                                    .error(R.drawable.error)
                                                                    .into(cardimage3);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        Log.w("RandomFragment", "onCancelled");
                                                    }
                                                });
                                                break;

                                            case 1:
                                                query.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                            Store st = dataSnapshot.getValue(Store.class);
                                                            cardtitle2.setText(st.name);
                                                            carddesc2.setText(st.desc);
                                                            storeimg = st.img;
                                                            Glide.with(getActivity())
                                                                    .load(storeimg)
                                                                    .placeholder(R.drawable.cloud)
                                                                    .error(R.drawable.error)
                                                                    .into(cardimage3);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        Log.w("RandomFragment", "onCancelled");
                                                    }
                                                });
                                                break;

                                            case 2:
                                                query.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                            Store st = dataSnapshot.getValue(Store.class);
                                                            cardtitle2.setText(st.name);
                                                            carddesc2.setText(st.desc);
                                                            storeimg = st.img;
                                                            Glide.with(getActivity())
                                                                    .load(storeimg)
                                                                    .placeholder(R.drawable.cloud)
                                                                    .error(R.drawable.error)
                                                                    .into(cardimage3);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        Log.w("RandomFragment", "onCancelled");
                                                    }
                                                });
                                                break;
                                        }
                                    }
                                })
                                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getActivity(), "선택 취소하기", Toast.LENGTH_SHORT).show();
                                    }
                                }).create().show();
                        break;
                }

                cardview1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), StoreActivity.class);
                        intent.putExtra("storeName", storename);
                        intent.putExtra("storePicture", storeimg);
                        intent.putExtra("storeStar", storestar);
                        intent.putExtra("storeNum", storenum);
                        intent.putExtra("storeDesc", storedesc);
                        startActivity(intent);
                    }
                });

                cardview3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), StoreActivity.class);
                        intent.putExtra("storeName", storename);
                        intent.putExtra("storePicture", storeimg);
                        intent.putExtra("storeStar", storestar);
                        intent.putExtra("storeNum", storenum);
                        intent.putExtra("storeDesc", storedesc);
                        startActivity(intent);
                    }
                });

            }
        });

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menurandom, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.reset:
                cardview1.setVisibility(View.INVISIBLE);
                cardimage1.setImageResource(R.drawable.questionmark);
                cardview2.setVisibility(View.INVISIBLE);
                cardimage2.setImageResource(R.drawable.questionmark);
                cardview3.setVisibility(View.INVISIBLE);
                cardimage3.setImageResource(R.drawable.questionmark);
                rgroup.clearCheck();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}