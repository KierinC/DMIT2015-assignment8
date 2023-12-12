package dmit2015.faces;

import dmit2015.restclient.Student;
import dmit2015.restclient.StudentAuthMpRestClient;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;


@Named("currentStudentListView")
@ViewScoped
public class StudentListView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    @RestClient
    private StudentAuthMpRestClient _studentMpRestClient;

    @Inject
    private FirebaseLoginSession _firebaseLoginSession;

    @Getter
    private Map<String, Student> studentMap;

    @PostConstruct  // After @Inject is complete
    public void init() {
        try {
            String token = _firebaseLoginSession.getFirebaseUser().getIdToken();
            String userUID = _firebaseLoginSession.getFirebaseUser().getLocalId();
            studentMap = _studentMpRestClient.findAll(userUID, token);
        } catch (Exception ex) {
            Messages.addGlobalError(ex.getMessage());
        }
    }
}