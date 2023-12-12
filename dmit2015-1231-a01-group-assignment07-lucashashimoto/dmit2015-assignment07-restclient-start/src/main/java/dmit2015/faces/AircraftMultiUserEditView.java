package dmit2015.faces;

import dmit2015.restclient.AircraftMultiUser;
import dmit2015.restclient.AircraftMultiUserMpRestClient;

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

import java.io.Serial;
import java.io.Serializable;

@Named("currentAircraftMultiUserEditView")
@ViewScoped
public class AircraftMultiUserEditView implements Serializable {
    @Inject
    private LoginSession _loginSession;
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    @RestClient
    private AircraftMultiUserMpRestClient _aircraftmultiuserMpRestClient;

    @Inject
    @ManagedProperty("#{param.editId}")
    @Getter
    @Setter
    private Long editId;

    @Getter
    private AircraftMultiUser existingAircraftMultiUser;

    @PostConstruct
    public void init() {
        if (!Faces.isPostback()) {
            if (editId != null) {
                String bearerAuth = _loginSession.getAuthorization();
                existingAircraftMultiUser = _aircraftmultiuserMpRestClient.findById(editId, bearerAuth);
                if (existingAircraftMultiUser == null) {
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
            String bearerAuth = _loginSession.getAuthorization();
            _aircraftmultiuserMpRestClient.update(editId, existingAircraftMultiUser, bearerAuth);
            Messages.addFlashGlobalInfo("Update was successful.");
            nextPage = "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Update was not successful.");
        }
        return nextPage;
    }
}