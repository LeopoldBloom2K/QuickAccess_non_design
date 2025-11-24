package com.example.myapp.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.etEmail);

        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPassword();
            }
        });
    }

    private void findPassword() {
        String email = etEmail.getText().toString().trim();

        // 비밀번호 찾기 로직
        // 예: 데이터베이스에서 이메일을 기반으로 비밀번호 조회 및 '*' 문자 포함하여 반환

        String foundPassword = "t********";

        if (foundPassword != null) {
            Toast.makeText(this, "비밀번호: " + foundPassword, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "가입된 이메일이 아닙니다", Toast.LENGTH_SHORT).show();
        }
    }
}
