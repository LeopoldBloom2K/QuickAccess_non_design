package com.example.myapp.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapp.R;
import com.example.myapp.model.data.User;
import com.example.myapp.viewmodel.SignUpViewModel;

public class SignUpActivity extends AppCompatActivity {

    private SignUpViewModel viewModel;
    private EditText etId, etPassword, etConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etId = findViewById(R.id.etId);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);


        findViewById(R.id.btnSignUp).setOnClickListener(v -> {
            String username = etId.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirm = etConfirmPassword.getText().toString().trim();
            viewModel.signUp(username, password, confirm);
        });

        // 3. 결과 관찰
        viewModel.getSignUpResult().observe(this, response -> {
            if (response.isSuccess()) {
                Toast.makeText(this, "회원가입 완료", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // 4. 에러 관찰
        viewModel.getErrorMessage().observe(this, errorMsg -> {
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        });


    }
}