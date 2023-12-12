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

import java.io.Serial;
import java.io.Serializable;

@Named("currentStudentDetailsView")
@ViewScoped
public class StudentDetailsView implements Serializable {

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
        String token = _firebaseLoginSession.getFirebaseUser().getIdToken();
        String userUID = _firebaseLoginSession.getFirebaseUser().getLocalId();
        existingStudent = _studentMpRestClient.findById(userUID,editId,token);
        if (existingStudent == null) {
            Faces.redirect(Faces.getRequestURI().substring(0, Faces.getRequestURI().lastIndexOf("/")) + "/index.xhtml");
        }
    }
}