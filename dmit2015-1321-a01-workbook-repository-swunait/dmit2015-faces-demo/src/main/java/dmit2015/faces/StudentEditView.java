package dmit2015.faces;

import dmit2015.restclient.Student;
import dmit2015.restclient.StudentAuthMpRestClient;
import jakarta.annotation.PostConstruct;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import java.io.Serial;
import java.io.Serializable;

@Named("currentStudentEditView")
@ViewScoped
public class StudentEditView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    @RestClient
    private StudentAuthMpRestClient _studentMpRestClient;

    @Inject
    private FirebaseLoginSession _firebaseLoginSession;

    @Inject
    @ManagedProperty("#{param.editId}")
    @Getter
    @Setter
    private String editId;

    @Getter
    private Student existingStudent;

    @PostConstruct
    public void init() {
        if (!Faces.isPostback()) {
            if (editId != null) {
                String token = _firebaseLoginSession.getFirebaseUser().getIdToken();
                String userUID = _firebaseLoginSession.getFirebaseUser().getLocalId();
                existingStudent = _studentMpRestClient.findById(userUID, editId, token);
                if (existingStudent == null) {
                    Faces.redirect(Faces.getRequestURI().substring(0, Faces.getRequestURI().lastIndexOf("/")) + "/index.xhtml");
                }
            } else {
                Faces.redirect(Faces.getRequestURI().substring(0, Faces.getRequestURI().lastIndexOf("/")) + "/index.xhtml");
            }
        }
    }

    public String onUpdate() {
        String nextPage = null;
        try {
            String token = _firebaseLoginSession.getFirebaseUser().getIdToken();
            String userUID = _firebaseLoginSession.getFirebaseUser().getLocalId();
            _studentMpRestClient.update(userUID, editId, existingStudent, token);
            Messages.addFlashGlobalInfo("Update was successful.");
            nextPage = "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Update was not successful.");
        }
        return nextPage;
    }
}