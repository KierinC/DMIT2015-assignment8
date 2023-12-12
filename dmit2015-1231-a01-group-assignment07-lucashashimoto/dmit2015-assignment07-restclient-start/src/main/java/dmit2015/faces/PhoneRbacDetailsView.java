package dmit2015.faces;

import dmit2015.restclient.PhoneRbac;
import dmit2015.restclient.PhoneRbacMpRestClient;

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

@Named("currentPhoneRbacDetailsView")
@ViewScoped
public class PhoneRbacDetailsView implements Serializable {
    @Inject
    private LoginSession _loginSession;
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    @RestClient
    private PhoneRbacMpRestClient _phonerbacMpRestClient;

    @Inject
    @ManagedProperty("#{param.editId}")
    @Getter
    @Setter
    private Long editId;

    @Getter
    private PhoneRbac existingPhoneRbac;

    @PostConstruct
    public void init() {
        existingPhoneRbac = _phonerbacMpRestClient.findById(editId);
        if (existingPhoneRbac == null) {
            Faces.redirect(Faces.getRequestURI().substring(0, Faces.getRequestURI().lastIndexOf("/")) + "/index.xhtml");
        }
    }
}