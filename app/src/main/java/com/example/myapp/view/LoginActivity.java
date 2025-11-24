package com.example.myapp.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapp.R;
import com.example.myapp.model.data.LoginResponse;
import com.example.myapp.model.data.User;
import com.example.myapp.model.remote.RetrofitClient;
import com.example.myapp.viewmodel.LoginViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etId, etPassword;
    private CheckBox chkAutoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etId = findViewById(R.id.etId);
        etPassword = findViewById(R.id.etPassword);
        chkAutoLogin = findViewById(R.id.chkAutoLogin);

        // 1. ViewModel 인스턴스 생성
        LoginViewModel viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // 2. 로그인 버튼 클릭 시 ViewModel 호출
        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            String username = etId.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            viewModel.login(username, password);
        });

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


        viewModel.getLoginResult().observe(this, response -> {
            if (response.isSuccess()) {
                if (chkAutoLogin.isChecked()) {
                    // 자동 로그인 저장 로직 (SharedPreferences)
                }
                startActivity(new Intent(LoginActivity.this, MainScreenActivity.class));
                finish();
            } else {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // 4. 에러 관찰 (실패 시)
        viewModel.getErrorMessage().observe(this, errorMsg -> {
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
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
