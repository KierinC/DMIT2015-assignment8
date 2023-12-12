package dmit2015.faces;

import dmit2015.entity.Region;
import dmit2015.persistence.RegionRepository;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

@Named("currentRegionCrudView")
@ViewScoped
public class RegionCrudView implements Serializable {

    @Inject
    private RegionRepository _regionRepository;

    @Getter
    private List<Region> regionList;

    @Getter
    @Setter
    private Region selectedRegion;

    @Getter
    @Setter
    private BigInteger selectedId;

    @PostConstruct  // After @Inject is complete
    public void init() {
        try {
            regionList = _regionRepository.findAll();
        } catch (Exception ex) {
            Messages.addGlobalError(ex.getMessage());
        }
    }

    public void onOpenNew() {
        selectedRegion = new Region();
    }

    public void onSave() {
        if (selectedId == null) {
            try {
                _regionRepository.add(selectedRegion);
                Messages.addGlobalInfo("Create was successful. {0}", selectedRegion.getRegionId());
                regionList = _regionRepository.findAll();
            } catch (RuntimeException e) {
                Messages.addGlobalError(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                Messages.addGlobalError("Create was not successful. {0}", e.getMessage());
            }
        } else {
            try {
                _regionRepository.update(selectedRegion);
                Messages.addFlashGlobalInfo("Update was successful.");
                regionList = _regionRepository.findAll();
            } catch (RuntimeException e) {
                Messages.addGlobalError(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                Messages.addGlobalError("Update was not successful.");
            }
        }

        PrimeFaces.current().executeScript("PF('manageRegionDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-Regions");
    }

    public void onDelete() {
        try {
            _regionRepository.delete(selectedRegion);
            selectedRegion = null;
            Messages.addGlobalInfo("Delete was successful.");
            regionList = _regionRepository.findAll();
            PrimeFaces.current().ajax().update("form:messages", "form:dt-Regions");
        } catch (RuntimeException e) {
            Messages.addGlobalError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Delete not successful.");
        }
    }

}