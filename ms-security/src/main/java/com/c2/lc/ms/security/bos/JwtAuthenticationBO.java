package com.c2.lc.ms.security.bos;

import lombok.*;

public class JwtAuthenticationBO {

    private String accessToken;

    private String tokenType;

    private Long expiryDuration;

    public JwtAuthenticationBO(String accessToken, Long expiryDuration) {
        this.accessToken = accessToken;
        this.expiryDuration = expiryDuration;
        tokenType = "Bearer ";
    }

    public JwtAuthenticationBO(String refreshedToken) {
    }

    public JwtAuthenticationBO() {

    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiryDuration() {
        return expiryDuration;
    }

    public void setExpiryDuration(Long expiryDuration) {
        this.expiryDuration = expiryDuration;
    }



}
