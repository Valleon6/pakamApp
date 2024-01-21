package com.valleon.pakamapp.modules.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AssessmentDTO {
    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    @NotBlank(message = "Please provide a description")
    private String description;

    @NotBlank(message = "Please enter a quantity")
    private int quantity;

}
