package dmit2015.faces;

import dmit2015.restclient.Todo;
import dmit2015.restclient.TodoAuthMpRestClient;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.util.Map;

@Named("currentTodoCrudView")
@ViewScoped
public class TodoCrudView implements Serializable {

    @Inject
    @RestClient
    private TodoAuthMpRestClient _TodoMpRestClient;

    @Inject
    private FirebaseLoginSession _firebaseLoginSession;

    @Getter
    private Map<String, Todo> TodoMap;

    @Getter
    @Setter
    private Todo selectedTodo;

    @Getter
    @Setter
    private String selectedKey;

    @PostConstruct  // After @Inject is complete
    public void init() {
        try {
            String token = _firebaseLoginSession.getFirebaseUser().getIdToken();
            String userUID = _firebaseLoginSession.getFirebaseUser().getLocalId();
            TodoMap = _TodoMpRestClient.findAll(userUID, token);
        } catch (Exception ex) {
            Messages.addGlobalError(ex.getMessage());
        }
    }

    public void onOpenNew() {
        selectedTodo = new Todo();
    }

    public void onSave() {
        if (selectedKey == null) {
            try {
                String token = _firebaseLoginSession.getFirebaseUser().getIdToken();
                String userUID = _firebaseLoginSession.getFirebaseUser().getLocalId();
                JsonObject responseBody = _TodoMpRestClient.create(userUID, selectedTodo, token);
                String documentKey = responseBody.getString("name");
                Messages.addGlobalInfo("Create was successful. {0}", documentKey);
                TodoMap = _TodoMpRestClient.findAll(userUID,token);
            } catch (Exception e) {
                e.printStackTrace();
                Messages.addGlobalError("Create was not successful. {0}", e.getMessage());
            }
        } else {
            try {
                String token = _firebaseLoginSession.getFirebaseUser().getIdToken();
                String userUID = _firebaseLoginSession.getFirebaseUser().getLocalId();
                _TodoMpRestClient.update(userUID, selectedKey, selectedTodo, token);
                Messages.addFlashGlobalInfo("Update was successful.");
            } catch (Exception e) {
                e.printStackTrace();
                Messages.addGlobalError("Update was not successful.");
            }
        }

        PrimeFaces.current().executeScript("PF('manageTodoDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-Todos");
    }

    public void onDelete() {
        try {
            String token = _firebaseLoginSession.getFirebaseUser().getIdToken();
            String userUID = _firebaseLoginSession.getFirebaseUser().getLocalId();
            _TodoMpRestClient.delete(userUID, selectedKey, token);
            selectedTodo = null;
            Messages.addGlobalInfo("Delete was successful.");
            TodoMap = _TodoMpRestClient.findAll(userUID, token);
            PrimeFaces.current().ajax().update("form:messages", "form:dt-Todos");
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Delete not successful.");
        }
    }

}