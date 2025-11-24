package com.example.myapp.repository;

import androidx.lifecycle.MutableLiveData;
import com.example.myapp.ApiService;
import com.example.myapp.LoginResponse;
import com.example.myapp.RetrofitClient;
import com.example.myapp.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private ApiService apiService;

    public UserRepository() {
        // 기존 RetrofitClient 재사용
        apiService = RetrofitClient.getApiService();
    }

    // 로그인 요청
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

    // 회원가입 요청 (기존 Room 코드 제거 및 Retrofit으로 통합)
    public void register(User user, final MutableLiveData<LoginResponse> responseLiveData, final MutableLiveData<String> errorLiveData) {
        apiService.register(user).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responseLiveData.postValue(response.body());
                } else {
                    LoginResponse errorResponse = response.body(); // 에러 바디 처리 필요 시 추가 구현
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