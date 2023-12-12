package dmit2015.restclient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Phone {

        private Long id;

        @NotBlank(message = "Name value cannot be blank.")
        private String name;

        @NotNull(message = "The Date field is required")
        private LocalDate date;

        private BigDecimal price;

        @NotBlank(message = "The field Brand is required.")
        private String brand;

        @NotBlank(message = "The field Operating System is required.")
        private String operatingSystem;

        private Integer version;
}
