package com.valleon.pakamapp.modules.payload;

import com.valleon.pakamapp.utils.AppConstants;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
@AllArgsConstructor

public class PasswordDTO {
    public static final String REGEX_PASSWORD = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";

    @NonNull
    @Pattern(regexp = AppConstants.REGEX_PASSWORD, message = "invalid password")
    private String newPassword;

    private String oldPassword;

    private String customerCode;

    private String token;

}
