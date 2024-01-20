package com.valleon.pakamapp.modules.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class LoginDTO {
    @NonNull
    private String email;
    @NonNull
    private String password;
}
