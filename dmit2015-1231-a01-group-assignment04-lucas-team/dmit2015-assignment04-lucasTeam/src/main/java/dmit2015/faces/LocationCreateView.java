package dmit2015.faces;

import dmit2015.entity.Country;
import dmit2015.entity.Location;
import dmit2015.entity.Region;
import dmit2015.persistence.CountryRepository;
import dmit2015.persistence.LocationRepository;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Messages;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.math.BigInteger;
import java.util.List;

@Named("currentLocationCreateView")
@RequestScoped
public class LocationCreateView {

    @Inject
    private LocationRepository _locationRepository;

    @Inject
    private CountryRepository _countryRepository;

    @Getter
    private Location newLocation = new Location();

    @Getter
    private List<Country> countries;

    // Define the countryId for the Location
    @Getter @Setter
    private String selectedCountryId;

    @PostConstruct  // After @Inject is complete
    public void init() {
        countries = _countryRepository.findAll();
    }

    public String onCreateNew() {
        String nextPage = "";
        try {
            // Check if the locationId is already in use
            if (_locationRepository.findById(newLocation.getLocationId()).isPresent()) {
                Messages.addGlobalError("LocationID {0} is already in use. Enter another value.", newLocation.getLocationId());
            } else {
                // Find the Region containing the selectedRegionId
                if (selectedCountryId != null) {
                    Country selectedCountry = _countryRepository.getReference(selectedCountryId);
                    newLocation.setCountriesByCountryId(selectedCountry);
                } else {
                    newLocation.setCountriesByCountryId(null);
                }

            _locationRepository.add(newLocation);
            Messages.addFlashGlobalInfo("Create was successful. {0}", newLocation.getLocationId());
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