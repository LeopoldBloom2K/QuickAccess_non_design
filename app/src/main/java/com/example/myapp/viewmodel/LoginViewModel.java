package com.example.myapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// [수정됨] 올바른 패키지 경로로 변경
import com.example.myapp.model.data.LoginResponse;
import com.example.myapp.model.data.User;
import com.example.myapp.model.repository.UserRepository;

public class LoginViewModel extends ViewModel {
    private UserRepository repository;
    private MutableLiveData<LoginResponse> loginResult = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LoginViewModel() {
        repository = new UserRepository();
    }

    public LiveData<LoginResponse> getLoginResult() { return loginResult; }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    public void login(String username, String password) {
        User user = new User();
        user.username = username;
        user.password = password;
        repository.login(user, loginResult, errorMessage);
    }
}