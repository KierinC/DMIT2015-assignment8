package dmit2015.faces;

import dmit2015.restclient.AircraftRbac;
import dmit2015.restclient.AircraftRbacMpRestClient;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.util.Messages;

@Named("currentAircraftRbacCreateView")
@RequestScoped
public class AircraftRbacCreateView {

    @Inject
    private LoginSession _loginSession;

    @Inject
    @RestClient
    private AircraftRbacMpRestClient _aircraftRbacMpRestClient;

    @Getter
    private AircraftRbac newAircraftRbac = new AircraftRbac();

    public String onCreateNew() {
        String nextPage = null;
        try {
            String bearerAuth = _loginSession.getAuthorization();
            Response response = _aircraftRbacMpRestClient.create(newAircraftRbac, bearerAuth);
            String location = response.getHeaderString("Location");
            String idValue = location.substring(location.lastIndexOf("/") + 1);
            newAircraftRbac = new AircraftRbac();
            Messages.addFlashGlobalInfo("Create was successful. {0}", idValue);
            nextPage = "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Create was not successful. {0}", e.getMessage());
        }
        return nextPage;
    }

}