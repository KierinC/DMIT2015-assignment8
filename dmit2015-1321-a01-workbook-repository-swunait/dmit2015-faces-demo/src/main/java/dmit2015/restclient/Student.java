package dmit2015.restclient;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class Student {

    @NotBlank(message = "First Name cannot be blank.")
    @Size(min = 2, message = "First Name '{validatedValue}' must at minimum contain {min} or more characters")
    private String firstName;

    @NotBlank(message = "Last Name cannot be blank.")
    @Size(min = 2, message = "Last Name must at minimum contain {min} or more characters")
    private String lastName;

    @Email(message = "Email '{validatedValue}' is not a valid email address")
    private String email;

    @Min(value = 18, message = "Age must be at minimum {value}.")
    @Max(value = 67, message = "Age must be at maximum {value}.")
    private int age;

}
