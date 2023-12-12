package dmit2015.faces;

import dmit2015.restclient.PhoneMultiUser;
import dmit2015.restclient.PhoneMultiUserMpRestClient;

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

@Named("currentPhoneMultiUserDeleteView")
@ViewScoped
public class PhoneMultiUserDeleteView implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    @RestClient
    private PhoneMultiUserMpRestClient _phonemultiuserMpRestClient;

    @Inject
    @ManagedProperty("#{param.editId}")
    @Getter
    @Setter
    private Long editId;
    @Inject
    private LoginSession _loginSession;
    @Getter
    private PhoneMultiUser existingPhoneMultiUser;

    @PostConstruct
    public void init() {
        String bearerAuth = _loginSession.getAuthorization();
        existingPhoneMultiUser = _phonemultiuserMpRestClient.findById(editId, bearerAuth);
        if (existingPhoneMultiUser == null) {
            Faces.redirect(Faces.getRequestURI().substring(0, Faces.getRequestURI().lastIndexOf("/")) + "/index.xhtml");
        }
    }

    public String onDelete() {
        String nextPage = "";
        try {
            String bearerAuth = _loginSession.getAuthorization();
            _phonemultiuserMpRestClient.delete(editId, bearerAuth);
//            _phonemultiuserMpRestClient.delete(editId);
            Messages.addFlashGlobalInfo("Delete was successful.");
            nextPage = "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Delete not successful.");
        }
        return nextPage;
    }
}