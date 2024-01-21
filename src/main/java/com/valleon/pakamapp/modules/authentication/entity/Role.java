package com.valleon.pakamapp.modules.authentication.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.valleon.pakamapp.modules.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class
Role {
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer roleID;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "role", cascade = {CascadeType.ALL})
    private Set<Customer> customers = new HashSet<Customer>();


}
