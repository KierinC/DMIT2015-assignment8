package dmit2015.faces;

import dmit2015.restclient.AircraftMultiUser;
import dmit2015.restclient.AircraftMultiUserMpRestClient;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Named("currentAircraftMultiUserListView")
@ViewScoped
public class AircraftMultiUserListView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    private LoginSession _loginSession;

    @Inject
    @RestClient
    private AircraftMultiUserMpRestClient _aircraftMultiUserMpRestClient;

    @Getter
    private List<AircraftMultiUser> aircraftMultiUserList;

//    @PostConstruct  // After @Inject is complete
    public void init() {
        try {
            String bearerAuth = _loginSession.getAuthorization();
            aircraftMultiUserList = _aircraftMultiUserMpRestClient.findAll(bearerAuth);
        } catch (Exception ex) {
            Messages.addGlobalError(ex.getMessage());
        }
    }
}