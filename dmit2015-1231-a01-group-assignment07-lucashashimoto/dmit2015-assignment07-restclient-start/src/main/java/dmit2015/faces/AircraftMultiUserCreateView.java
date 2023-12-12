package dmit2015.faces;

import dmit2015.restclient.AircraftMultiUser;
import dmit2015.restclient.AircraftMultiUserMpRestClient;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.util.Messages;

@Named("currentAircraftMultiUserCreateView")
@RequestScoped
public class AircraftMultiUserCreateView {

    @Inject
    private LoginSession _loginSession;
    @Inject
    @RestClient
    private AircraftMultiUserMpRestClient _aircraftMultiUserMpRestClient;

    @Getter
    private AircraftMultiUser newAircraftMultiUser = new AircraftMultiUser();

    public String onCreateNew() {
        String nextPage = null;
        try {
            String bearerAuth = _loginSession.getAuthorization();
            Response response = _aircraftMultiUserMpRestClient.create(newAircraftMultiUser, bearerAuth);
            String location = response.getHeaderString("Location");
            String idValue = location.substring(location.lastIndexOf("/") + 1);
            newAircraftMultiUser = new AircraftMultiUser();
            Messages.addFlashGlobalInfo("Create was successful. {0}", idValue);
            response.close();
            nextPage = "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Create was not successful. {0}", e.getMessage());
        }
        return nextPage;
    }

}