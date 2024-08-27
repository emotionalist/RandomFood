package com.cookandroid.suwonrandomfood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class reportActivity extends AppCompatActivity {
    EditText reporter;
    RadioGroup radiogroup;
    RadioButton addstore, reporterror, question;
    Button sendBtn;
    ImageButton back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);
        this.getSupportActionBar().hide();

        reporter = findViewById(R.id.reporter);
        radiogroup = findViewById(R.id.radiogroup);
        addstore = findViewById(R.id.addstore);
        reporterror = findViewById(R.id.reporterror);
        question = findViewById(R.id.question);
        sendBtn = findViewById(R.id.sendBtn);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                String[] address = {"abc123@naver.com"};
                email.putExtra(Intent.EXTRA_EMAIL, address);
                email.putExtra(Intent.EXTRA_SUBJECT, "문의합니다.");
                email.putExtra(Intent.EXTRA_TEXT, reporter.getText().toString());
                startActivity(email);
            }
        });

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radiogroup.getCheckedRadioButtonId()) {
                    case R.id.addstore:
                        reporter.setHint("  추가하고 싶은 가게를 제보해주세요." +
                                "  ex) 가게 이름, 대표 메뉴, 가성비 좋은 가게");
                        break;
                    case R.id.reporterror:
                        reporter.setHint("  오류를 제보해주세요. " +
                                "ex) ~~페이지에서 ~~오류가 있어요.");
                        break;
                    case R.id.question:
                        reporter.setHint("  궁금하신 점을 물어보세요." +
                                "  ex) ~~하는 방법이 궁금해요.");
                        break;
                }
            }
        });
    }
}