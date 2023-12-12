package dmit2015.restclient;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoItem implements Serializable {

    private Long id;

    @NotBlank(message = "Name value cannot be blank.")
    private String name;

    private boolean complete;

    private Integer version;

}