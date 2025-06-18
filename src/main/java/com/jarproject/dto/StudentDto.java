package com.jarproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto{
    private Integer id;

    @NotBlank(message = "Name can't be empty")
    private String name;

    @NotBlank(message = "Username can't be empty")
    private String username;


    @NotNull(message = "Date of birth can't be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @NotBlank(message = "Email can't be empty")
    private String email;

    private boolean gender;

    @NotBlank(message = "Phone can't be empty")
    @Pattern(
            regexp = "^$|^0[0-9]{9}$",
            message = "Invalid phone number. It must start with 0 and contain 10 digits"
    )
    private String phone;

    @NotBlank(message = "Password can't be empty")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*[\\d_@#-]).{8,}$",
            message = "Password must be at least 8 characters and include at least one letter and one number or special character (_, -, @, #)"
    )
    private String password;

    private LocalDate createAt;
    private boolean role;
    private boolean status;
}
