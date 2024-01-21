package com.valleon.pakamapp.modules.payload;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
@AllArgsConstructor
public class RegisterDTO {
    @NonNull
    private String email;

    @NonNull
    @NotEmpty
    private String firstName;

    @NonNull
    @NotEmpty
    private String lastName;

    @NonNull
    @NotEmpty
    private String password;

    private String role;

    @NonNull
    @NotEmpty
    private String phoneNumber;
}

