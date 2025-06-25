package com.babit.demo.domain.user.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private long id;
    private String password;
    private String name;
    private String email;
    private LocalDate birthDate;
    private String gender;
    private float height;
    private float weight;
    private String disease;
    private String excerciseLevel;
    private boolean isActive;
    private String totalMileage;

}