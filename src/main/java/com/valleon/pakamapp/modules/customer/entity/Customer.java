package com.valleon.pakamapp.modules.customer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.valleon.pakamapp.modules.assessment.entity.Assessment;
import com.valleon.pakamapp.modules.authentication.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "customer", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"})
})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @JsonIgnore
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "customer_id", nullable = false, unique = true)
    private String customerId;

    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "email")
    private String email;

//    @NotNull
//    @Column(name = "username")
//    private String username;

    @JsonIgnore
    @NotNull
    @Column(name = "password")
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private List<Assessment> assessments;

    @Column(name = "customer_code")
    private String customerCode;

    @Column(name = "phone")
    private String phone;

    @Column(name = "is_active")
    private Boolean isActive;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
    private Set<Role> role;

    @Column(name = "date_created")
    private LocalDateTime dateCreated = LocalDateTime.now();


}
