package com.valleon.pakamapp.modules.payload;

import com.valleon.pakamapp.modules.customer.entity.Customer;
import com.valleon.pakamapp.utils.AppConstants;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto extends Customer {
    @Pattern(regexp = AppConstants.REGEX_EMAIL, message = "invalid email")
    private String email;

    private String customer_code;

    private String first_name;

    private String last_name;

    @Pattern(regexp = AppConstants.REGEX_PHONE_NUMBER, message = "invalid phone number")
    private String phone;

//    private String userName;

}
