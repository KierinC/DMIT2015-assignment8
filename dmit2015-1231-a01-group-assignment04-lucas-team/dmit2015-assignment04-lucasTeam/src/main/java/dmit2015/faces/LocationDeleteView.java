package dmit2015.faces;

import dmit2015.entity.Location;
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
import java.util.Optional;

@Named("currentLocationDeleteView")
@ViewScoped
public class LocationDeleteView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    private LocationRepository _locationRepository;

    @Inject
    @ManagedProperty("#{param.editId}")
    @Getter
    @Setter
    private short editId;

    @Getter
    private Location existingLocation;

    @PostConstruct
    public void init() {
        Optional<Location> optionalLocation = _locationRepository.findById(editId);
        if (optionalLocation.isPresent()) {
            existingLocation = optionalLocation.orElseThrow();
        } else {
            Faces.redirect(Faces.getRequestURI().substring(0, Faces.getRequestURI().lastIndexOf("/")) + "/index.xhtml");
        }
    }

    public String onDelete() {
        String nextPage = "";
        try {
            _locationRepository.delete(existingLocation);
            Messages.addFlashGlobalInfo("Delete was successful.");
            nextPage = "index?faces-redirect=true";
        } catch (RuntimeException e) {
            Messages.addGlobalError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Delete not successful.");
        }
        return nextPage;
    }
}