package dmit2015.persistence;

import common.jpa.AbstractJpaRepository;
import dmit2015.entity.Location;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class LocationRepository extends AbstractJpaRepository<Location, Short> {

    public LocationRepository() {
        super(Location.class);
    }

    public void add(Location newLocation) {
        short nextId = getEntityManager()
                .createQuery("SELECT MAX(l.locationId) + 100 FROM Location l", Short.class)
                .getSingleResult();
        newLocation.setLocationId(nextId);

        getEntityManager().persist(newLocation);
    }

}