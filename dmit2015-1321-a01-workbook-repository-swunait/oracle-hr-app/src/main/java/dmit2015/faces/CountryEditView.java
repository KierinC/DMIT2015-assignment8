package dmit2015.faces;

import dmit2015.entity.Country;
import dmit2015.entity.Region;
import dmit2015.persistence.CountryRepository;

import dmit2015.persistence.RegionRepository;
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

@Named("currentCountryEditView")
@ViewScoped
public class CountryEditView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    private CountryRepository _countryRepository;

    @Inject
    private RegionRepository _regionRepository;

    // Define the regionId for the Country
    @Getter @Setter
    private BigInteger selectedRegionId;

    @Getter
    private List<Region> regions;

    @Inject
    @ManagedProperty("#{param.editId}")
    @Getter
    @Setter
    private String editId;

    @Getter
    private Country existingCountry;

    @PostConstruct
    public void init() {
        if (!Faces.isPostback()) {
            regions = _regionRepository.findAll();

            if (editId != null) {
                Optional<Country> optionalCountry = _countryRepository.findById(editId);
                if (optionalCountry.isPresent()) {
                    existingCountry = optionalCountry.orElseThrow();

                    selectedRegionId = existingCountry.getRegionId();
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
            // Find the Region containing the selectedRegionId
            if (selectedRegionId != null) {
//                Region selectedRegion = _regionRepository.getReference(selectedRegionId);
//                existingCountry.setRegionsByRegionId(selectedRegion);
                Optional<Region> optionalRegion = _regionRepository.findById(selectedRegionId);
                optionalRegion.ifPresent(selectedRegion -> existingCountry.setRegionsByRegionId(selectedRegion));

            } else {
                existingCountry.setRegionsByRegionId(null);
            }

            _countryRepository.update(existingCountry);
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