package com.c2.lc.ms.security.bos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginBO implements Serializable {

    private static final long serialVersionUID = 7078477960859486435L;

    private String jwtToken;

    private String tokenType="Bearer ";

    private Long expiryDuration;

    private String refreshJwtToken;

    private Long custId;

    private String apiKey;

    private String apiToken;

/*
    @Overide
    public String toString() {
        return "LoginPayload[" +
                "jwtToken='" + jwtToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", expiryDuration=" + expiryDuration + '\'' +
                ", cust_id=" + custId + '\'' +
                ", apiKey=" + apiKey + '\'' +
                ", apiToken=" + apiToken +
                ']';
    }
*/
}
