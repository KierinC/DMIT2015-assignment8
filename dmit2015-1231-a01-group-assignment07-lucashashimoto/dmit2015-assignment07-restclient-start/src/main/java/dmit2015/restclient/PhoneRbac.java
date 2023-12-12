package dmit2015.restclient;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PhoneRbac {
    private Long id;
    private String name;

    private LocalDate date;

    private BigDecimal price;

    private String brand;

    private String operatingSystem;

    private Integer version;
}
