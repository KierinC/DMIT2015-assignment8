package dmit2015.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "DEPARTMENTS", schema = "HR", catalog = "")
public class Department implements Serializable {

    @Id
    @Column(name = "DEPARTMENT_ID")
    private short departmentId;
    @Basic
    @Column(name = "DEPARTMENT_NAME")
    private String departmentName;
    @Basic
    @Column(name = "MANAGER_ID", insertable = false, updatable = false)
    private Integer managerId;
    @Basic
    @Column(name = "LOCATION_ID", insertable = false, updatable = false)
    private Short locationId;
    @ManyToOne
    @JoinColumn(name = "MANAGER_ID", referencedColumnName = "EMPLOYEE_ID")
    private Employee employeesByManagerId;
    @ManyToOne
    @JoinColumn(name = "LOCATION_ID", referencedColumnName = "LOCATION_ID")
    private Location locationsByLocationId;
    @OneToMany(mappedBy = "departmentsByDepartmentId")
    private Collection<Employee> employeesByDepartmentId;
    @OneToMany(mappedBy = "departmentsByDepartmentId")
    private Collection<JobHistory> jobHistoriesByDepartmentId;

    public short getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(short departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public Short getLocationId() {
        return locationId;
    }

    public void setLocationId(Short locationId) {
        this.locationId = locationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return departmentId == that.departmentId && Objects.equals(departmentName, that.departmentName) && Objects.equals(managerId, that.managerId) && Objects.equals(locationId, that.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId, departmentName, managerId, locationId);
    }

    public Employee getEmployeesByManagerId() {
        return employeesByManagerId;
    }

    public void setEmployeesByManagerId(Employee employeesByManagerId) {
        this.employeesByManagerId = employeesByManagerId;
    }

    public Location getLocationsByLocationId() {
        return locationsByLocationId;
    }

    public void setLocationsByLocationId(Location locationsByLocationId) {
        this.locationsByLocationId = locationsByLocationId;
    }

    public Collection<Employee> getEmployeesByDepartmentId() {
        return employeesByDepartmentId;
    }

    public void setEmployeesByDepartmentId(Collection<Employee> employeesByDepartmentId) {
        this.employeesByDepartmentId = employeesByDepartmentId;
    }

    public Collection<JobHistory> getJobHistoriesByDepartmentId() {
        return jobHistoriesByDepartmentId;
    }

    public void setJobHistoriesByDepartmentId(Collection<JobHistory> jobHistoriesByDepartmentId) {
        this.jobHistoriesByDepartmentId = jobHistoriesByDepartmentId;
    }
}
