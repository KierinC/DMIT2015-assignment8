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

    public void delete(Country existingCountry) {
        // Check if there are any child entity (Location) associated with this parent entity (Country)
        long locationCount = getEntityManager().createQuery("""
select count(l)
from Location l
where l.countryId = :countryIdValue
""", Long.class)
                .setParameter("countryIdValue", existingCountry.getCountryId())
                .getSingleResult();
        if (locationCount > 0) {
            String message = String.format("This record cannot be deleted as there are %d associated records.", locationCount);
            throw new RuntimeException(message);
        }

        super.delete(existingCountry);
    }

}