package com.valleon.pakamapp.modules.assessment.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.valleon.pakamapp.modules.customer.entity.Customer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "assessment")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Assessment {

    @Id
    @JsonIgnore
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "assessment_id", nullable = false, unique = true)
    private String assessmentId;

    @NotNull
    @Column(name = "assessment_code")
    private String assessmentCode;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "is_exist")
    private boolean isExist;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
