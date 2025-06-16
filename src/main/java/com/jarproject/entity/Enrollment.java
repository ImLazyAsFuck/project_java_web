package com.jarproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "enrollment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Enrollment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime registeredAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollmentStatus status = EnrollmentStatus.WAITING;

    public String getFormattedRegisteredAt() {
        return registeredAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getStatusColorClass() {
        switch (this.status) {
            case WAITING:
                return "text-yellow-500";
            case CONFIRMED:
                return "text-green-500";
            case DENIED:
            case CANCELED:
                return "text-red-500";
            default:
                return "text-red-500";
        }
    }

}
