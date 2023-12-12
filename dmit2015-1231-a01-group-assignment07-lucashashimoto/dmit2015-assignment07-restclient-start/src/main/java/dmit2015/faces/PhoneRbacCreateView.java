package dmit2015.faces;

import dmit2015.restclient.Phone;
import dmit2015.restclient.PhoneRbacMpRestClient;
import dmit2015.restclient.PhoneRbac;
import dmit2015.restclient.PhoneRbacMpRestClient;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.util.Messages;

@Named("currentPhoneRbacCreateView")
@RequestScoped

public class PhoneRbacCreateView {


    @Inject
    private LoginSession _loginSession;

    @Inject
    @RestClient
    private PhoneRbacMpRestClient _phoneRbacMpRestClient;

    @Getter
    private PhoneRbac newPhoneRbac = new PhoneRbac();

    public String onCreateNew() {
        String nextPage = null;
        try {
            String bearerAuth = _loginSession.getAuthorization();
            Response response = _phoneRbacMpRestClient.create(newPhoneRbac, bearerAuth);
            String location = response.getHeaderString("Location");
            String idValue = location.substring(location.lastIndexOf("/") + 1);
            newPhoneRbac = new PhoneRbac();
            Messages.addFlashGlobalInfo("Create was successful. {0}", idValue);
            nextPage = "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Create was not successful. {0}", e.getMessage());
        }
        return nextPage;
    }

}