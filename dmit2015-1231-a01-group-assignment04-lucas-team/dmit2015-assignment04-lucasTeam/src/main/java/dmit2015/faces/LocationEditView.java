package dmit2015.faces;

import dmit2015.entity.Location;
import dmit2015.entity.Country;
import dmit2015.persistence.CountryRepository;
import dmit2015.persistence.LocationRepository;

import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import jakarta.annotation.PostConstruct;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Named("currentLocationEditView")
@ViewScoped
public class LocationEditView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    private LocationRepository _locationRepository;

    @Inject
    private CountryRepository _countryRepository;

    @Getter @Setter
    private String selectedCountryId;

    @Getter
    private List<Country> countries;

    @Inject
    @ManagedProperty("#{param.editId}")
    @Getter
    @Setter
    private BigInteger editId;

    @Getter
    private Location existingLocation;

    @PostConstruct
    public void init() {
        if (!Faces.isPostback()) {
            if (editId != null) {
                countries = _countryRepository.findAll();

                Optional<Location> optionalLocation = _locationRepository.findById(editId);
                if (optionalLocation.isPresent()) {
                    existingLocation = optionalLocation.orElseThrow();
                } else {
                    Faces.redirect(Faces.getRequestURI().substring(0, Faces.getRequestURI().lastIndexOf("/")) + "/index.xhtml");
                }
            } else {
                Faces.redirect(Faces.getRequestURI().substring(0, Faces.getRequestURI().lastIndexOf("/")) + "/index.xhtml");
            }
        }
    }

    public String onUpdate() {
        String nextPage = "";
        try {
            // Find the Country containing the selectedCountryId
            if (selectedCountryId != null) {
                Country selectedCountry = _countryRepository.getReference(selectedCountryId);
                existingLocation.setCountriesByCountryId(selectedCountry);
            } else {
                existingLocation.setCountriesByCountryId(null);
            }

            _locationRepository.update(existingLocation);
            Messages.addFlashGlobalInfo("Update was successful.");
            nextPage = "index?faces-redirect=true";
        } catch (RuntimeException e) {
            Messages.addGlobalError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Update was not successful.");
        }
        return nextPage;
    }
}