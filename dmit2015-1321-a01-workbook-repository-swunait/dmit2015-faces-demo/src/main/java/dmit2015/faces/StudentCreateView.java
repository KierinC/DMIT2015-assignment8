package dmit2015.faces;

import dmit2015.restclient.Student;
import dmit2015.restclient.StudentAuthMpRestClient;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.JsonObject;
import lombok.Getter;
import net.datafaker.Faker;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.util.Messages;

@Named("currentStudentCreateView")
@RequestScoped
public class StudentCreateView {

    @Inject
    @RestClient
    private StudentAuthMpRestClient _studentMpRestClient;

    @Inject
    private FirebaseLoginSession _firebaseLoginSession;

    @Getter
    private Student newStudent = new Student();

    public String onCreateNew() {
        String nextPage = null;
        try {
            String token = _firebaseLoginSession.getFirebaseUser().getIdToken();
            String userUID = _firebaseLoginSession.getFirebaseUser().getLocalId();
            JsonObject responseBody = _studentMpRestClient.create(userUID, newStudent, token);
            String documentKey = responseBody.getString("name");
            newStudent = new Student();
            Messages.addFlashGlobalInfo("Create was successful. {0}", documentKey);
            nextPage = "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Create was not successful. {0}", e.getMessage());
        }
        return nextPage;
    }

    public void onGenerateData() {
        // create a new instance of Faker
        var faker = new Faker();
        // Generate a random first name, last name, email, and age
        newStudent.setFirstName(faker.name().firstName());
        newStudent.setLastName(faker.name().lastName());
        newStudent.setEmail(faker.internet().emailAddress());
        newStudent.setAge(faker.number().numberBetween(18,67));
    }


}