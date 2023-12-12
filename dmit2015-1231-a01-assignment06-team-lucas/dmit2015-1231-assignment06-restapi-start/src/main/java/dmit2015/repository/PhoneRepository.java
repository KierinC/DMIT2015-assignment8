package dmit2015.repository;

import dmit2015.repository.AbstractJpaRepository;
import dmit2015.entity.Phone;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class PhoneRepository extends AbstractJpaRepository<Phone, Long> {

    public PhoneRepository() {
        super(Phone.class);
    }

}