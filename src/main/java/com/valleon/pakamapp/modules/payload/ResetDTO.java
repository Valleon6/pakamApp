package com.valleon.pakamapp.modules.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
@AllArgsConstructor
public class ResetDTO {
    @NonNull
    private String email;

}
