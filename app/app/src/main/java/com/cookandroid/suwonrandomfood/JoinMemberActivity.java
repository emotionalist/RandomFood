package com.cookandroid.suwonrandomfood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class JoinMemberActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    private FirebaseAuth mAuth;
    FirebaseUser user;
    Button backToLogin, registerBtn;
    EditText userEmail;
    EditText userPW;
    ArrayList<Store> stores = new ArrayList<>();
    String uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joinmember);
        mAuth = FirebaseAuth.getInstance();

        backToLogin = findViewById(R.id.backToLogin);
        userEmail = findViewById(R.id.userEmail);
        userPW = findViewById(R.id.userPW);
        registerBtn = findViewById(R.id.registerBtn);

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            private void createUser(String email, String password) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(JoinMemberActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthWeakPasswordException e) {
                                        Toast.makeText(JoinMemberActivity.this, "비밀번호가 너무 짧습니다.", Toast.LENGTH_SHORT).show();
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        Toast.makeText(JoinMemberActivity.this, "이메일 형식에 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                                    } catch (FirebaseAuthUserCollisionException e) {
                                        Toast.makeText(JoinMemberActivity.this, "이미 존재하는 이메일입니다.", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Toast.makeText(JoinMemberActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(JoinMemberActivity.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                                    user = mAuth.getCurrentUser();
                                    uid = user.getUid();
                                    prepareData();
                                    startActivity(new Intent(JoinMemberActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
            }

            @Override
            public void onClick(View v) {
                //이메일과 비밀번호가 공백이 아닌 경우
                if (!userEmail.getText().toString().equals("") && !userPW.getText().toString().equals("")) {
                    createUser(userEmail.getText().toString(), userPW.getText().toString());
                } else {
                    // 이메일과 비밀번호가 공백인 경우
                    Toast.makeText(JoinMemberActivity.this, "계정과 비밀번호를 입력하세요", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void prepareData() {
        stores.add(new Store(0, "연습1", "연습입니다.1", "3.5", "https://firebasestorage.googleapis.com/v0/b/randomfood-677da.appspot.com/o/FB_IMG_1477346240661.jpg?alt=media&token=be088c30-9938-4a8e-a2b8-5845593594ae", false, false));
        stores.add(new Store(1, "연습2", "연습입니다.2", "4.0", "https://firebasestorage.googleapis.com/v0/b/randomfood-677da.appspot.com/o/FB_IMG_1472812247230.jpg?alt=media&token=42b9e488-5dff-4a70-a9ae-fa61cd1436ee", false, false));
        for (int i = 0; i < stores.size(); i++) {
            databaseReference.child(uid).child("Store").child("Store" + i).setValue(stores.get(i));
        }
    }
}