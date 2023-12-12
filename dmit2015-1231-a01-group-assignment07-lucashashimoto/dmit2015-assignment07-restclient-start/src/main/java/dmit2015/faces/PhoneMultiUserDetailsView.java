package dmit2015.faces;

import dmit2015.restclient.PhoneMultiUser;
import dmit2015.restclient.PhoneMultiUserMpRestClient;

import lombok.Getter;
import lombok.Setter;

import jakarta.annotation.PostConstruct;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.util.Faces;

import java.io.Serial;
import java.io.Serializable;

@Named("currentPhoneMultiUserDetailsView")
@ViewScoped
public class PhoneMultiUserDetailsView implements Serializable {

    @Inject
    private LoginSession _loginSession;
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    @RestClient
    private PhoneMultiUserMpRestClient _phonemultiuserMpRestClient;

    @Inject
    @ManagedProperty("#{param.editId}")
    @Getter
    @Setter
    private Long editId;

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
}