package dmit2015.faces;

import dmit2015.entity.Todo;
import dmit2015.persistence.TodoRepository;
import lombok.Getter;
import org.omnifaces.util.Messages;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Named("currentTodoListView")
@ViewScoped
public class TodoListView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    private TodoRepository _todoRepository;

    @Getter
    private List<Todo> todoList;

    @PostConstruct  // After @Inject is complete
    public void init() {
        try {
            todoList = _todoRepository.findAll();
        } catch (Exception ex) {
            Messages.addGlobalError(ex.getMessage());
        }
    }
}