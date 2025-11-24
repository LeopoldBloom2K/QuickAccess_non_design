package com.example.myapp.model.remote;

import com.example.myapp.model.data.LoginResponse;
import com.example.myapp.model.data.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    // 회원가입: User 객체를 보내고, 응답으로 LoginResponse(성공여부/메시지)를 받음
    @POST("/register")
    Call<LoginResponse> register(@Body User user);

    // 로그인: User 객체를 보내고, 응답으로 LoginResponse를 받음
    @POST("/login")
    Call<LoginResponse> login(@Body User user);
}