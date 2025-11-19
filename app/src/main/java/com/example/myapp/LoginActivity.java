package com.example.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etId, etPassword;
    private CheckBox chkAutoLogin;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etId = findViewById(R.id.etId);
        etPassword = findViewById(R.id.etPassword);
        chkAutoLogin = findViewById(R.id.chkAutoLogin);

        db = AppDatabase.getDatabase(this);

        loadLoginDetails();

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        findViewById(R.id.tvForgotPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    private void saveLoginDetails(String id, String password) {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_id", id);
        editor.putString("user_password", password);
        editor.apply();
    }

    private void loadLoginDetails() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String id = preferences.getString("user_id", null);
        String password = preferences.getString("user_password", null);

        if (id != null && password != null) {
            etId.setText(id);
            etPassword.setText(password);
            chkAutoLogin.setChecked(true);
        }
    }

    private void login() {
        String username = etId.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // User 객체 생성 및 데이터 설정
        User user = new User();
        user.username = username;
        user.password = password;

        // 서버로 로그인 요청 전송
        RetrofitClient.getApiService().login(user).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 로그인 성공 처리
                    if (chkAutoLogin.isChecked()) {
                        saveLoginDetails(username, password);
                    }
                    Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "서버 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
