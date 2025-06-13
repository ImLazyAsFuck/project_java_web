package com.jarproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto{
    @NotBlank(message = "Id can't be empty")
    @Pattern(regexp = "^(|C\\d{4})$", message = "Id must start with 'C' followed by 4 digits")
    private String id;

    @NotBlank(message = "Name can't be empty")
    private String name;

    @NotBlank(message = "Instructor can't be empty")
    private String instructor;

    @NotNull(message = "duration can't be null")
    private Integer duration;

    private String image;
}
