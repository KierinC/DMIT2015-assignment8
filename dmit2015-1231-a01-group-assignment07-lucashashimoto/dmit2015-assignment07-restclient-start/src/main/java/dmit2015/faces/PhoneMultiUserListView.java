package dmit2015.faces;

import dmit2015.restclient.PhoneMultiUser;
import dmit2015.restclient.PhoneMultiUserMpRestClient;
import dmit2015.restclient.PhoneMultiUser;
import lombok.Getter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;


@Named("currentPhoneMultiUserListView")
@ViewScoped
public class PhoneMultiUserListView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Inject
    private LoginSession _loginSession;
    @Inject
    @RestClient
    private PhoneMultiUserMpRestClient _phonemultiuserMpRestClient;

    @Getter
    private List<PhoneMultiUser> phoneMultiUserList;


    public void init() {
        try {
            String bearerAuth = _loginSession.getAuthorization();
            phoneMultiUserList = _phonemultiuserMpRestClient.findAll(bearerAuth);
        } catch (Exception ex) {
            Messages.addGlobalError(ex.getMessage());
        }
    }
}