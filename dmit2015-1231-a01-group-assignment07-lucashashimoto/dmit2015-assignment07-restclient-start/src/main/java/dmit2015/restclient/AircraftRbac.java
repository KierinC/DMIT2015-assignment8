package dmit2015.restclient;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AircraftRbac {

    private Long id;

    private String model;

    private String manufacturer;

    private String tailNumber;

    private Integer version;

}
