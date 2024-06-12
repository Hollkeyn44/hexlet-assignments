package exercise.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductCreateDTO {
    @NotNull
    private Long categoryId;

    @NotBlank
    private String title;

    @NotNull
    private int price;
}
