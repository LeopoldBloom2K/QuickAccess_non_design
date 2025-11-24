package com.example.myapp.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapp.R;
import com.example.myapp.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private EditText etId, etPassword;
    private CheckBox chkAutoLogin;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // View 초기화
        etId = findViewById(R.id.etId);
        etPassword = findViewById(R.id.etPassword);
        chkAutoLogin = findViewById(R.id.chkAutoLogin);

        // ViewModel 초기화
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // [수정됨] 리스너 설정: ViewModel의 login() 호출 (단 하나만 존재해야 함)
        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            String username = etId.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            viewModel.login(username, password);
        });

        // 비밀번호 찾기 버튼
        findViewById(R.id.tvForgotPassword).setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });

        // 자동 로그인 정보 불러오기
        loadLoginDetails();

        // [MVVM 핵심] 로그인 성공 여부 관찰 (Observer)
        viewModel.getLoginResult().observe(this, response -> {
            if (response.isSuccess()) {
                // 로그인 성공 시 처리
                if (chkAutoLogin.isChecked()) {
                    String username = etId.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();
                    saveLoginDetails(username, password);
                }
                Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // 에러 메시지 관찰
        viewModel.getErrorMessage().observe(this, errorMsg -> {
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        });
    }

    // SharedPreferences 관련 코드는 View(Activity)에 남겨도 무방합니다.
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

    // [삭제됨] private void login() { ... } -> 옛날 방식의 직접 호출 코드는 삭제했습니다.
}