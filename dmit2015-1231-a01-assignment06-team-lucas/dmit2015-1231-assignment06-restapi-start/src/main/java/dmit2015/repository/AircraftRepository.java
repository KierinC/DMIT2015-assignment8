package dmit2015.repository;

import dmit2015.repository.AbstractJpaRepository;
import dmit2015.entity.Aircraft;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class AircraftRepository extends AbstractJpaRepository<Aircraft, Long> {

    public AircraftRepository() {
        super(Aircraft.class);
    }

}