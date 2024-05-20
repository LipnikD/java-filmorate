package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@Builder
public class Film {
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Length(max = 200)
    private String description;

    private LocalDate releaseDate;

    @Positive
    private int duration;
}
