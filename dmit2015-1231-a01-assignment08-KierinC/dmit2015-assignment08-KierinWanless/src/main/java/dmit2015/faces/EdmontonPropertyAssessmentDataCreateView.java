package dmit2015.faces;

import dmit2015.entity.EdmontonPropertyAssessmentData;
import dmit2015.repository.EdmontonPropertyAssessmentDataRepository;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Messages;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.Optional;

@Named("currentEdmontonPropertyAssessmentDataCreateView")
@RequestScoped
public class EdmontonPropertyAssessmentDataCreateView {

    @Inject
    private EdmontonPropertyAssessmentDataRepository _edmontonpropertyassessmentdataRepository;

    @Getter
    private Optional<EdmontonPropertyAssessmentData> newEdmontonPropertyAssessmentData = Optional.of(new EdmontonPropertyAssessmentData()) ;

    @Getter
    @Setter
    private String houseNumber;

    @Getter
    @Setter
    private String streetName;

    @Getter
    @Setter
    private String suite;

    @PostConstruct  // After @Inject is complete
    public void init() {

    }

    public void onCreateNew() {
        try {
            newEdmontonPropertyAssessmentData = _edmontonpropertyassessmentdataRepository.findByHouseNumberAndStreetNameAndSuite(houseNumber, streetName, suite);

        } catch (RuntimeException e) {
            Messages.addGlobalError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("not successful. {0}", e.getMessage());
        }
    }

}