package dmit2015.restclient;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aircraft {

    private Long id;

    @NotBlank(message = "The Model field is required.")
    private String model;

    @NotBlank(message = "The Manufacturer field is required.")
    private String manufacturer;

    @NotBlank(message = "The TailNumber field is required.")
    private String tailNumber;

    private Integer version;
}
