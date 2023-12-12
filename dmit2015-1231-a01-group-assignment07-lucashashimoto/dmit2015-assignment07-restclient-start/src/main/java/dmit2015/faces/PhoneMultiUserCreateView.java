package dmit2015.faces;

import dmit2015.restclient.PhoneMultiUser;
import dmit2015.restclient.PhoneMultiUserMpRestClient;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.util.Messages;
@Named("currentPhoneMultiUserCreateView")
@RequestScoped
public class PhoneMultiUserCreateView {

    @Inject
    private LoginSession _loginSession;
    @Inject
    @RestClient
    private PhoneMultiUserMpRestClient _phoneMultiUserMpRestClient;

    @Getter
    private PhoneMultiUser newPhoneMultiUser = new PhoneMultiUser();

    public String onCreateNew() {
        String nextPage = null;
        try {
            String bearerAuth = _loginSession.getAuthorization();
            Response response = _phoneMultiUserMpRestClient.create(newPhoneMultiUser, bearerAuth);
            String location = response.getHeaderString("Location");
            String idValue = location.substring(location.lastIndexOf("/") + 1);
            newPhoneMultiUser = new PhoneMultiUser();
            Messages.addFlashGlobalInfo("Create was successful. {0}", idValue);
            response.close();
            nextPage = "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Create was not successful. {0}", e.getMessage());
        }
        return nextPage;
    }

}

