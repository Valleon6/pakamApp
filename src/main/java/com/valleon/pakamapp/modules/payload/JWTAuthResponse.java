package com.valleon.pakamapp.modules.payload;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JWTAuthResponse {
    private String accessToken;
    private String tokenType;

}