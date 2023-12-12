package dmit2015.faces;

import dmit2015.restclient.AircraftRbac;
import dmit2015.restclient.AircraftRbacMpRestClient;
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

@Named("currentAircraftRbacListView")
@ViewScoped
public class AircraftRbacListView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    @RestClient
    private AircraftRbacMpRestClient _aircraftRbacMpRestClient;

    @Getter
    private List<AircraftRbac> aircraftRbacList;

    @PostConstruct  // After @Inject is complete
    public void init() {
        try {
            aircraftRbacList = _aircraftRbacMpRestClient.findAll();
        } catch (Exception ex) {
            Messages.addGlobalError(ex.getMessage());
        }
    }
}