package dmit2015.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Objects;

/**
 * This is the entity for the table Countries
 * @author Lucas Hashimoyo
 * @version 2023.10.19
 */
@Entity
@Table(name = "COUNTRIES", schema = "HR", catalog = "")
public class Country implements Serializable {

    @Id
    @Column(name = "COUNTRY_ID")
    @NotBlank(message = "Country ID must be assigned.")
    @Size(min = 2, max = 2 , message = "Country ID must contain exactly 2 characters.")
    private String countryId;
    @Basic
    @Column(name = "COUNTRY_NAME")
    private String countryName;
    @Basic
    @Column(name = "REGION_ID", insertable=false, updatable=false)
    private BigInteger regionId;
    @ManyToOne
    @JoinColumn(name = "REGION_ID", referencedColumnName = "REGION_ID")
    private Region regionByRegionId;
    @OneToMany(mappedBy = "countriesByCountryId")
    private Collection<Location> locationsByCountryId;

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public BigInteger getRegionId() {
        return regionId;
    }

    public void setRegionId(BigInteger regionId) {
        this.regionId = regionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return Objects.equals(countryId, country.countryId) && Objects.equals(countryName, country.countryName) && Objects.equals(regionId, country.regionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryId, countryName, regionId);
    }

    public Region getRegionsByRegionId() {
        return regionByRegionId;
    }

    public void setRegionsByRegionId(Region regionByRegionId) {
        this.regionByRegionId = regionByRegionId;
    }

    public Collection<Location> getLocationsByCountryId() {
        return locationsByCountryId;
    }

    public void setLocationsByCountryId(Collection<Location> locationsByCountryId) {
        this.locationsByCountryId = locationsByCountryId;
    }
}