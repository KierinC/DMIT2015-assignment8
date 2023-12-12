package dmit2015.persistence;

import common.jpa.AbstractJpaRepository;
import dmit2015.entity.Country;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class CountryRepository extends AbstractJpaRepository<Country, String> {

    public CountryRepository() {
        super(Country.class);
    }

}