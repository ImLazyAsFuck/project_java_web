    package com.jarproject.entity;

    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import javax.persistence.*;
    import java.time.LocalDate;
    import java.time.format.DateTimeFormatter;

    @Entity
    @Table(name = "course")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class Course{
        @Id
        @Column(name = "id", columnDefinition = "CHAR(5) CHECK (CHAR_LENGTH(id) = 5)")
        private String id;

        @Column(name = "name", unique = true, length = 100, nullable = false)
        private String name;

        @Column(name = "duration", nullable = false)
        private Integer duration;

        @Column(name = "instructor", nullable = false)
        private String instructor;

        @Column(name = "create_at", columnDefinition = "DATE", nullable = false)
        private LocalDate createAt = LocalDate.now();

        @Column(name = "image", length = 500)
        private String image = null;

        @Column(name = "status")
        private boolean status = true;

        public String getFormattedCreateAt() {
            return createAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

    }
