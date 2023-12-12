package dmit2015.faces;

import dmit2015.entity.Todo;
import dmit2015.persistence.TodoRepository;

import lombok.Getter;
import lombok.Setter;

import jakarta.annotation.PostConstruct;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.omnifaces.util.Faces;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;

@Named("currentTodoDetailsView")
@ViewScoped
public class TodoDetailsView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    private TodoRepository _todoRepository;

    @Inject
    @ManagedProperty("#{param.editId}")
    @Getter
    @Setter
    private Long editId;

    @Getter
    private Todo existingTodo;

    @PostConstruct
    public void init() {
        Optional<Todo> optionalTodo = _todoRepository.findById(editId);
        if (optionalTodo.isPresent()) {
            existingTodo = optionalTodo.orElseThrow();
        } else {
            Faces.redirect(Faces.getRequestURI().substring(0, Faces.getRequestURI().lastIndexOf("/")) + "/index.xhtml");
        }
    }
}