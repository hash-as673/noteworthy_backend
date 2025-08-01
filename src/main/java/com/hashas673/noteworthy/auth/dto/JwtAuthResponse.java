package com.hashas673.noteworthy.auth.dto;


import lombok.Data;

@Data
public class JwtAuthResponse {
    private String token;
    private String username;

    public JwtAuthResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }

}
