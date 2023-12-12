package dmit2015.faces;

import dmit2015.restclient.AircraftMultiUser;
import dmit2015.restclient.AircraftMultiUserMpRestClient;

import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import jakarta.annotation.PostConstruct;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named("currentAircraftMultiUserDeleteView")
@ViewScoped
public class AircraftMultiUserDeleteView implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    @RestClient
    private AircraftMultiUserMpRestClient _aircraftmultiuserMpRestClient;

    @Inject
    @ManagedProperty("#{param.editId}")
    @Getter
    @Setter
    private Long editId;
    @Inject
    private LoginSession _loginSession;
    @Getter
    private AircraftMultiUser existingAircraftMultiUser;

    @PostConstruct
    public void init() {
        String bearerAuth = _loginSession.getAuthorization();
        existingAircraftMultiUser = _aircraftmultiuserMpRestClient.findById(editId, bearerAuth);
        if (existingAircraftMultiUser == null) {
            Faces.redirect(Faces.getRequestURI().substring(0, Faces.getRequestURI().lastIndexOf("/")) + "/index.xhtml");
        }
    }

    public String onDelete() {
        String nextPage = "";
        try {
            String bearerAuth = _loginSession.getAuthorization();
            _aircraftmultiuserMpRestClient.delete(editId, bearerAuth);
//            _aircraftmultiuserMpRestClient.delete(editId);
            Messages.addFlashGlobalInfo("Delete was successful.");
            nextPage = "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Delete not successful.");
        }
        return nextPage;
    }
}