package dmit2015.faces;

import dmit2015.entity.Location;
import dmit2015.persistence.LocationRepository;
import lombok.Getter;
import org.omnifaces.util.Messages;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Named("currentLocationListView")
@ViewScoped
public class LocationListView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    private LocationRepository _locationRepository;

    @Getter
    private List<Location> locationList;

    @PostConstruct  // After @Inject is complete
    public void init() {
        try {
            locationList = _locationRepository.findAll();
        } catch (Exception ex) {
            Messages.addGlobalError(ex.getMessage());
        }
    }
}