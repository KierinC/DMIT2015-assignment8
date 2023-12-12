package dmit2015.faces;

import dmit2015.restclient.TodoItemRbac;
import dmit2015.restclient.TodoItemRbacMpRestClient;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.util.Messages;

@Named("currentTodoItemRbacCreateView")
@RequestScoped
public class TodoItemRbacCreateView {

    @Inject
    private LoginSession _loginSession;

    @Inject
    @RestClient
    private TodoItemRbacMpRestClient _todoItemRbacMpRestClient;

    @Getter
    private TodoItemRbac newTodoItemRbac = new TodoItemRbac();

    public String onCreateNew() {
        String nextPage = null;
        try {
            String bearerAuth = _loginSession.getAuthorization();
            Response response = _todoItemRbacMpRestClient.create(newTodoItemRbac, bearerAuth);
            String location = response.getHeaderString("Location");
            String idValue = location.substring(location.lastIndexOf("/") + 1);
            newTodoItemRbac = new TodoItemRbac();
            Messages.addFlashGlobalInfo("Create was successful. {0}", idValue);
            nextPage = "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Create was not successful. {0}", e.getMessage());
        }
        return nextPage;
    }

}