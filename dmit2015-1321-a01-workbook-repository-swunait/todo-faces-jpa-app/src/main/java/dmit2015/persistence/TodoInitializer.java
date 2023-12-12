package dmit2015.persistence;

import dmit2015.entity.Todo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class TodoInitializer {
    private final Logger _logger = Logger.getLogger(TodoInitializer.class.getName());

    @Inject
    private TodoRepository _todoRepository;


    /**
     * Using the combination of `@Observes` and `@Initialized` annotations, you can
     * intercept and perform additional processing during the phase of beans or events
     * in a CDI container.
     * <p>
     * The @Observers is used to specify this method is in observer for an event
     * The @Initialized is used to specify the method should be invoked when a bean type of `ApplicationScoped` is being
     * initialized
     * <p>
     * Execute code to create the test data for the entity.
     * This is an alternative to using a @WebListener that implements a ServletContext listener.
     * <p>
     * ]    * @param event
     */
    public void initialize(@Observes @Initialized(ApplicationScoped.class) Object event) {
        _logger.info("Initializing todos");

        if (_todoRepository.count() == 0) {

            // You could hard code the test data
            try {
                // TODO: Create a new entity instance
                // TODO: Set the properties of the entity instance
                // TODO: Add the entity instance to the JPA repository
//                Todo todo1 = new Todo();
//                todo1.setTask("Write up assignment 4");
//                todo1.setDone(true);
//                _todoRepository.add(todo1);
//
//                Todo todo2 = new Todo();
//                todo2.setTask("Post assignment 4");
//                _todoRepository.add(todo2);

            } catch (Exception ex) {
//                ex.printStackTrace();
                _logger.fine(ex.getMessage());
            }

            // You could read the data from a CSV file
            try {
                try (var reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/data/csv/todos.csv"))))) {

                    String line;
                    final var delimiter = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
                    // TODO: Uncomment the next line if the first line contains column headings
//                    reader.readLine();
                    while ((line = reader.readLine()) != null) {
                        Optional<Todo> optionalTodo = Todo.parseCsv(line);
                        if (optionalTodo.isPresent()) {
                            Todo csvTodo = optionalTodo.orElseThrow();
                            _todoRepository.add(csvTodo);
                        }
                    }
                }

            } catch (Exception ex) {
//                ex.printStackTrace();
                _logger.fine(ex.getMessage());
            }
        }

        _logger.info("Created " + _todoRepository.count() + " records.");
    }
}