package dmit2015.persistence;

import common.jpa.AbstractJpaRepository;
import dmit2015.entity.Region;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.math.BigInteger;

@ApplicationScoped
@Transactional
public class RegionRepository extends AbstractJpaRepository<Region, BigInteger> {

    public RegionRepository() {
        super(Region.class);
    }

    public void add(Region newRegion) {
        BigInteger nextId = getEntityManager()
                .createQuery("SELECT MAX(r.regionId) + 10 FROM Region r", BigInteger.class)
                .getSingleResult();
        newRegion.setRegionId(nextId);

        getEntityManager().persist(newRegion);
    }
}