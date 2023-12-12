package dmit2015.faces;

import dmit2015.entity.Country;
import dmit2015.entity.Region;
import dmit2015.persistence.CountryRepository;

import dmit2015.persistence.RegionRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Messages;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Named("currentCountryCreateView")
@RequestScoped
public class CountryCreateView {

    @Inject
    private CountryRepository _countryRepository;

    @Inject
    private RegionRepository _regionRepository;

    @Getter
    private Country newCountry = new Country();

    // Define the regionId for the Country
    @Getter @Setter
    private BigInteger selectedRegionId;

    @Getter
    private List<Region> regions;

    @PostConstruct  // After @Inject is complete
    public void init() {
        regions = _regionRepository.findAll();
    }

    public String onCreateNew() {
        String nextPage = "";
        try {
            // Check if the countryId is already in use
            if (_countryRepository.findById(newCountry.getCountryId()).isPresent()) {
                Messages.addGlobalError("CountryID {0} is already in use. Enter another value.", newCountry.getCountryId());
            } else {
                // Find the Region containing the selectedRegionId
                if (selectedRegionId != null) {
//                    Region selectedRegion = _regionRepository.getReference(selectedRegionId);
//                    newCountry.setRegionsByRegionId(selectedRegion);
                    Optional<Region> optionalRegion = _regionRepository.findById(selectedRegionId);
                    optionalRegion.ifPresent(selectedRegion -> newCountry.setRegionsByRegionId(selectedRegion));

                } else {
                    newCountry.setRegionsByRegionId(null);
                }

                _countryRepository.add(newCountry);
                Messages.addFlashGlobalInfo("Create was successful. {0}", newCountry.getCountryId());
                nextPage = "index?faces-redirect=true";
            }
        } catch (RuntimeException e) {
            Messages.addGlobalError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Create was not successful. {0}", e.getMessage());
        }
        return nextPage;
    }

}