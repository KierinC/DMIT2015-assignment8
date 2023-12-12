package dmit2015.faces;

import dmit2015.entity.Todo;
import dmit2015.persistence.TodoRepository;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.omnifaces.util.Messages;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("currentTodoCreateView")
@RequestScoped
public class TodoCreateView {

    @Inject
    private TodoRepository _todoRepository;

    @Getter
    private Todo newTodo = new Todo();

    @PostConstruct  // After @Inject is complete
    public void init() {

    }

    public String onCreateNew() {
        String nextPage = "";
        try {
            _todoRepository.add(newTodo);
            Messages.addFlashGlobalInfo("Create was successful. {0}", newTodo.getId());
            nextPage = "index?faces-redirect=true";
        } catch (RuntimeException e) {
            Messages.addGlobalError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Create was not successful. {0}", e.getMessage());
        }
        return nextPage;
    }

}