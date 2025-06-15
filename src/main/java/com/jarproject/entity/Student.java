package com.jarproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "student")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "dob", nullable = false, columnDefinition = "DATE")
    private LocalDate dob;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "gender", columnDefinition = "BIT")
    private boolean gender;

    @Column(name = "phone",  nullable = false, length = 20, unique = true)
    private String phone;

    @Column(name = "password",  nullable = false)
    private String password;

    @Column(name = "create_at", columnDefinition = "DATE", nullable = false)
    private LocalDate createAt = LocalDate.now();

    @Column(name = "role", nullable = false)
    private boolean role = false;

    @Column(name = "status", nullable = false)
    private boolean status = true;
}
