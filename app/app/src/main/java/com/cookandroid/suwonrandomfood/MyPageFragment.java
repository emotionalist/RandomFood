package com.cookandroid.suwonrandomfood;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyPageFragment extends Fragment {
    FirebaseAuth mAuth;
    TextView toolbar_text;

    Button develope, will, have, logout, revoke;
    TextView username, useremail;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String uid = user.getUid();
    String email = user.getEmail();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();

    public static MyPageFragment newInstance() {
        MyPageFragment fragment = new MyPageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    private void signOut() {
        mAuth.getInstance().signOut();
    }

    private void revokeAccess() {
        mAuth.getInstance().getCurrentUser().delete();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentManager fm = getActivity().getSupportFragmentManager();

        View rootView = inflater.inflate(R.layout.fragment_my_page, container, false);

        develope = rootView.findViewById(R.id.develope);
        will = rootView.findViewById(R.id.will);
        have = rootView.findViewById(R.id.have);
        logout = rootView.findViewById(R.id.logout);
        revoke = rootView.findViewById(R.id.revoke);
        username = rootView.findViewById(R.id.username);
        useremail = rootView.findViewById(R.id.useremail);

        username.setText(uid);
        useremail.setText(email);

        develope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), reportActivity.class);
                startActivity(intent);
            }
        });

        will.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getActivity(), willActivity.class);
                startActivity(intent2);
            }
        });

        have.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(getActivity(), haveActivity.class);
                startActivity(intent3);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();

                Toast.makeText(getActivity(), "로그아웃 성공", Toast.LENGTH_SHORT).show();
                Intent intent4 = new Intent(getActivity(), MainActivity.class);
                startActivity(intent4);
            }
        });

        revoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revokeAccess();
                Toast.makeText(getActivity(), "회원탈퇴 성공", Toast.LENGTH_SHORT).show();
                databaseReference = database.getReference();
                databaseReference.child(uid).removeValue();
                Intent intent5 = new Intent(getActivity(), MainActivity.class);
                startActivity(intent5);
            }
        });

        return rootView;
    }
}