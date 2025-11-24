package com.example.myapp.model.repository; // 패키지명 주의

import androidx.lifecycle.MutableLiveData;

import com.example.myapp.model.remote.ApiService;
import com.example.myapp.model.data.LoginResponse;
import com.example.myapp.model.remote.RetrofitClient;
import com.example.myapp.model.data.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private ApiService apiService;

    public UserRepository() {
        apiService = RetrofitClient.getApiService();
    }

    public void login(User user, final MutableLiveData<LoginResponse> responseLiveData, final MutableLiveData<String> errorLiveData) {
        apiService.login(user).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responseLiveData.postValue(response.body());
                } else {
                    errorLiveData.postValue("로그인 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                errorLiveData.postValue("서버 연결 실패: " + t.getMessage());
            }
        });
    }

    public void register(User user, final MutableLiveData<LoginResponse> responseLiveData, final MutableLiveData<String> errorLiveData) {
        apiService.register(user).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responseLiveData.postValue(response.body());
                } else {
                    LoginResponse errorResponse = response.body();
                    errorLiveData.postValue("회원가입 실패: " + (errorResponse != null ? errorResponse.getMessage() : "오류 발생"));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                errorLiveData.postValue("서버 연결 실패: " + t.getMessage());
            }
        });
    }
}