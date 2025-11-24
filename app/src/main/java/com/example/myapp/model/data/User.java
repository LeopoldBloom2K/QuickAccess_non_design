package com.example.myapp.model.data;

// @Entity 삭제 -> 순수한 자바 객체(POJO)로 변경
public class User {
    public String username;
    public String password;

    // 기본 생성자
    public User() {
    }

    // 편의를 위한 생성자
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}