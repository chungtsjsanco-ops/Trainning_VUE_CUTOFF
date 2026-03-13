package com.example.backend.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LoginResponse {

    private String token;

    @JsonProperty("token_type")
    private final String tokenType = "Bearer";

    private String username;

    private List<String> roles;

    public LoginResponse(String token, String username, List<String> roles) {
        this.token = token;
        this.username = username;
        this.roles = roles;
    }

    public String getToken() { return token; }
    public String getTokenType() { return tokenType; }
    public String getUsername() { return username; }
    public List<String> getRoles() { return roles; }
}
