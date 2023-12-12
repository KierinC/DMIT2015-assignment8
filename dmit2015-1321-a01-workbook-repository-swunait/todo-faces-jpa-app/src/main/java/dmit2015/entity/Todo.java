package dmit2015.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Entity
//@Table(schema = "CustomSchemaName", name="CustomTableName")
@Getter
@Setter
public class Todo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id", nullable = false)
    private Long id;

    @NotBlank(message = "Task cannot be blank.")
    @Column(length = 128)
    private String task;

    private boolean done;

    public Todo() {

    }

    @Version
    private Integer version;

    @Column(nullable = false)
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @PrePersist
    private void beforePersist() {
        createTime = LocalDateTime.now();
    }

    @PreUpdate
    private void beforeUpdate() {
        updateTime = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object obj) {
        return (
                (obj instanceof Todo other) &&
                        Objects.equals(id, other.id)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public static Optional<Todo> parseCsv(String line) {
        Optional<Todo> optionalTodo = Optional.empty();
        final var DELIMITER = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        String[] tokens = line.split(DELIMITER, -1);  // The -1 limit allows for any number of fields and not discard trailing empty fields
        /*
         * The order of the columns are:
         * 0 - task
         * 1 - done
         * 2 - column3
         * 3 - column4
         */
        if (tokens.length == 2) {
            Todo parsedTodo = new Todo();

            try {
                 String task = tokens[0].replaceAll("\"","");
                 boolean done = Boolean.parseBoolean(tokens[1]);

                 parsedTodo.setTask(task);
                 parsedTodo.setDone(done);

                optionalTodo = Optional.of(parsedTodo);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return optionalTodo;
    }

}