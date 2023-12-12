package dmit2015.restclient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PhoneMultiUser {

    private Long id;

    private String name;

    private LocalDate date;

    private BigDecimal price;

    private String brand;

    private String operatingSystem;

    private Integer version;
}
