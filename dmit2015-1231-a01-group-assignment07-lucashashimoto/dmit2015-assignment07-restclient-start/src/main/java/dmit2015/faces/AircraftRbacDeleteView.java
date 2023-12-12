package dmit2015.faces;

import dmit2015.restclient.AircraftRbac;
import dmit2015.restclient.AircraftRbacMpRestClient;

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

@Named("currentAircraftRbacDeleteView")
@ViewScoped
public class AircraftRbacDeleteView implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private LoginSession _loginSession;


    @Inject
    @RestClient
    private AircraftRbacMpRestClient _aircraftrbacMpRestClient;

    @Inject
    @ManagedProperty("#{param.editId}")
    @Getter
    @Setter
    private Long editId;

    @Getter
    private AircraftRbac existingAircraftRbac;

    @PostConstruct
    public void init() {
        existingAircraftRbac = _aircraftrbacMpRestClient.findById(editId);
        if (existingAircraftRbac == null) {
            Faces.redirect(Faces.getRequestURI().substring(0, Faces.getRequestURI().lastIndexOf("/")) + "/index.xhtml");
        }
    }

    public String onDelete() {
        String nextPage = "";
        try {
            String bearerAuth = _loginSession.getAuthorization();
            _aircraftrbacMpRestClient.delete(editId, bearerAuth);
            Messages.addFlashGlobalInfo("Delete was successful.");
            nextPage = "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Delete not successful.");
        }
        return nextPage;
    }
}