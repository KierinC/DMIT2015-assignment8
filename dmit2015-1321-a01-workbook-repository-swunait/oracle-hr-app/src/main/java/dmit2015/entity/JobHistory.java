package dmit2015.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "JOB_HISTORY", schema = "HR", catalog = "")
@IdClass(JobHistoryPK.class)
public class JobHistory implements Serializable {

    @Id
    @Column(name = "EMPLOYEE_ID")
    private int employeeId;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "START_DATE")
    private LocalDate startDate;
    @Basic
    @Column(name = "END_DATE")
    private LocalDate endDate;
    @Basic
    @Column(name = "JOB_ID", insertable = false, updatable = false)
    private String jobId;
    @Basic
    @Column(name = "DEPARTMENT_ID", insertable = false, updatable = false)
    private Short departmentId;
    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "EMPLOYEE_ID", nullable = false)
    private Employee employeesByEmployeeId;
    @ManyToOne
    @JoinColumn(name = "JOB_ID", referencedColumnName = "JOB_ID", nullable = false)
    private Job jobsByJobId;
    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID", referencedColumnName = "DEPARTMENT_ID")
    private Department departmentsByDepartmentId;

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Short getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Short departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobHistory that = (JobHistory) o;
        return employeeId == that.employeeId && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(jobId, that.jobId) && Objects.equals(departmentId, that.departmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, startDate, endDate, jobId, departmentId);
    }

    public Employee getEmployeesByEmployeeId() {
        return employeesByEmployeeId;
    }

    public void setEmployeesByEmployeeId(Employee employeesByEmployeeId) {
        this.employeesByEmployeeId = employeesByEmployeeId;
    }

    public Job getJobsByJobId() {
        return jobsByJobId;
    }

    public void setJobsByJobId(Job jobsByJobId) {
        this.jobsByJobId = jobsByJobId;
    }

    public Department getDepartmentsByDepartmentId() {
        return departmentsByDepartmentId;
    }

    public void setDepartmentsByDepartmentId(Department departmentsByDepartmentId) {
        this.departmentsByDepartmentId = departmentsByDepartmentId;
    }
}
