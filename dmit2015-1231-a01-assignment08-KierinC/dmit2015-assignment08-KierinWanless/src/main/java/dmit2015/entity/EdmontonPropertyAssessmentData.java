package dmit2015.entity;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;

@Entity
@Table(name = "kchesworthwanle1EdmontonPropertyAssessment")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
public class EdmontonPropertyAssessmentData implements Serializable {

//    @NotBlank(message = "Account Number is required")
//    @EqualsAndHashCode.Include
    @Id
//    @Column(name = "account_number", nullable = false, unique = true)
    public String accountNumber;

//    @Column(name = "suite", nullable = false, length = 128)
    public String suite;

//    @NotBlank(message = "House Number is required")
//    @Column(name = "house_number", nullable = false, length = 128)
    public String houseNumber;

//    @NotBlank(message = "Street Name is required")
//    @Column(name = "street_name", nullable = false, length = 128)
    public String streetName;

//    @NotNull(message = "Garage is required")
//    @Column(name = "garage", nullable = false, length = 128)
    public boolean garage;

//    @NotNull(message = "Neighbourhood Id is required")
    @Column(name = "neighbourhood_id")
    public int neighbourhoodID;

//    @NotBlank(message = "Neighbourhood is required")
//    @Column(name = "neighbourhood", nullable = false, length = 128)
    public String neighbourhood;

//    @NotBlank(message = "Ward is required")
//    @Column(name = "ward", nullable = false, length = 128)
    public String ward;

//    @NotNull(message = "Assessed Value is required")
    @Column(name = "assessed_value")
    public Long assessedValue;

//    @NotNull(message = "Latitude is required")
//    @Column(nullable = false)
    private double latitude;

//    @NotNull(message = "Longitude is required")
//    @Column(nullable = false)
    private double longitude;

//    @NotBlank(message = "Assessment Class 1 is required")
//    @Column(name = "assessment_class_1", nullable = false, length = 128)
    public String assessmentClass1;

    @Column(name = "point_location")
    @jakarta.json.bind.annotation.JsonbTransient
    public Point pointLocation;

    private LocalDateTime createTime;

    @PrePersist
    private void beforePersist() {
        createTime = LocalDateTime.now();
    }

    public static Optional<EdmontonPropertyAssessmentData> parseCsv(String line) {
        final String delimiter = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        String[] values = line.split(delimiter);

        EdmontonPropertyAssessmentData model = new EdmontonPropertyAssessmentData();
//        try {
            model.setAccountNumber(Objects.equals(values[0], "") ? null : values[0]);
            model.setSuite(Objects.equals(values[1], "") ? "" : values[1]);
            model.setHouseNumber(Objects.equals(values[2], "") ? null : values[2]);
            model.setStreetName(Objects.equals(values[3], "") ? null : values[3]);
            model.setGarage(Boolean.parseBoolean(values[4]));
            model.setNeighbourhoodID(values[5].trim().isEmpty() ? 0 : Integer.parseInt(values[5]));
            model.setNeighbourhood(Objects.equals(values[6], "") ? null : values[6]);
            model.setWard(Objects.equals(values[7], "") ? null : values[7]);
            model.setAssessedValue(values[8].trim().isEmpty() ? null : Long.parseLong(values[8]));
            model.setLatitude(values[9].trim().isEmpty() ? null : Double.parseDouble(values[9]));
            model.setLongitude(values[10].trim().isEmpty() ? null : Double.parseDouble(values[10]));

            GeometryFactory geometryFactory = new GeometryFactory();
            Coordinate coordinate = new Coordinate(model.longitude, model.latitude);
            Point point = geometryFactory.createPoint(coordinate);
            model.setPointLocation(point);

            model.setAssessmentClass1(Objects.equals(values[12], "") ? null : values[12]);

            return Optional.of(model);
//        } catch (Exception ex) {
//            return Optional.empty();
//        }
    }
}
