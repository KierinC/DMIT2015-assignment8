package dmit2015.repository;

import dmit2015.entity.Aircraft;
import dmit2015.repository.AbstractJpaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
@Transactional
public class AircraftRepository extends AbstractJpaRepository<Aircraft, Long> {

    public AircraftRepository() {
        super(Aircraft.class);
    }

    public List<Aircraft> findAllByUsername(String username) {
        return getEntityManager().createQuery("select o from Aircraft o where o.username = :usernameValue", Aircraft.class)
                .setParameter("usernameValue", username)
                .getResultList();
    }

}