package dmit2015.faces;

import dmit2015.restclient.PhoneRbac;
import dmit2015.restclient.PhoneRbacMpRestClient;
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

@Named("currentPhoneRbacListView")
@ViewScoped
public class PhoneRbacListView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    @RestClient
    private PhoneRbacMpRestClient _phoneRbacMpRestClient;

    @Getter
    private List<PhoneRbac> phoneRbacList;

    @PostConstruct  // After @Inject is complete
    public void init() {
        try {
            phoneRbacList = _phoneRbacMpRestClient.findAll();
        } catch (Exception ex) {
            Messages.addGlobalError(ex.getMessage());
        }
    }
}
