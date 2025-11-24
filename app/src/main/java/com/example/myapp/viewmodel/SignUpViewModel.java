package com.example.myapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// [수정됨] 올바른 패키지 경로로 변경
import com.example.myapp.model.data.LoginResponse;
import com.example.myapp.model.data.User;
import com.example.myapp.model.repository.UserRepository;

public class SignUpViewModel extends ViewModel {
    private UserRepository repository;
    private MutableLiveData<LoginResponse> signUpResult = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public SignUpViewModel() {
        repository = new UserRepository();
    }

    public LiveData<LoginResponse> getSignUpResult() { return signUpResult; }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    public void signUp(String username, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            errorMessage.setValue("비밀번호가 일치하지 않습니다.");
            return;
        }
        if (username.isEmpty() || password.isEmpty()) {
            errorMessage.setValue("아이디와 비밀번호를 입력해주세요.");
            return;
        }

        User user = new User();
        user.username = username;
        user.password = password;
        repository.register(user, signUpResult, errorMessage);
    }
}