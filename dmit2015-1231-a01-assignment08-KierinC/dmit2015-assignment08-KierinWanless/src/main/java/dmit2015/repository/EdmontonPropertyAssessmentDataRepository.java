package dmit2015.repository;

import common.jpa.AbstractJpaRepository;
import dmit2015.entity.EdmontonPropertyAssessmentData;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class EdmontonPropertyAssessmentDataRepository extends AbstractJpaRepository<EdmontonPropertyAssessmentData, String> {

    public EdmontonPropertyAssessmentDataRepository() {
        super(EdmontonPropertyAssessmentData.class);
    }

    public List<EdmontonPropertyAssessmentData> findWithinDistance(
            double longitude,
            double latitude,
            double distanceMeters
    ) {
        List<EdmontonPropertyAssessmentData> propertyList = new ArrayList<>();

        var distanceKm = distanceMeters / 1000;
        final double EARTH_MEAN_RADIUS_KM = 6371.0087714;
        final double DEGREES_TO_RADIANS =  Math.PI / 180;
        final double RADIANS_TO_DEGREES =  1 / DEGREES_TO_RADIANS;
        var distanceCentralAngleDegrees = distanceKm * RADIANS_TO_DEGREES / EARTH_MEAN_RADIUS_KM;

        final String jpql = """
        select p
        from EdmontonPropertyAssessmentData p
        where function('st_distance', p.pointLocation, :pointParam) <= :distanceCentralAngleDegreesParam
        """;
        TypedQuery<EdmontonPropertyAssessmentData> query = getEntityManager().createQuery(jpql, EdmontonPropertyAssessmentData.class);

        org.locationtech.jts.geom.Point geoLocation = new GeometryFactory()
                .createPoint(
                        new Coordinate( longitude, latitude  )
                );
        //       Point<G2D> geoLocation = DSL.point(CoordinateReferenceSystems.WGS84, DSL.g(longitude, latitude));

        query.setParameter("pointParam", geoLocation);
        query.setParameter("distanceCentralAngleDegreesParam", distanceCentralAngleDegrees);
        propertyList = query
                .setMaxResults(25)
                .getResultList();

        return propertyList;
    }

    public Optional<EdmontonPropertyAssessmentData> findByHouseNumberAndStreetNameAndSuite(
            String houseNumber,
            String streetName,
            String suite
    ) {
        Optional<EdmontonPropertyAssessmentData> optionalSingleResult = Optional.empty();

        try {
            EdmontonPropertyAssessmentData querySingleResult = getEntityManager()
                    .createQuery("""
                    select p
                    from EdmontonPropertyAssessmentData p
                    where p.houseNumber = :houseNumberParam
                        and p.streetName = :streetNameParam
                        and coalesce(p.suite, '') = :suiteParam
                    """, EdmontonPropertyAssessmentData.class)
                    .setParameter("houseNumberParam", houseNumber)
                    .setParameter("streetNameParam", streetName)
                    .setParameter("suiteParam", suite)
                    .getSingleResult();
            optionalSingleResult = Optional.of(querySingleResult);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return optionalSingleResult;
    }

}