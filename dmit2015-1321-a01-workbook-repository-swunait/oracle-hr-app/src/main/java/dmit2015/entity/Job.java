package dmit2015.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "JOBS", schema = "HR", catalog = "")
public class Job implements Serializable {

    @Id
    @Column(name = "JOB_ID")
    private String jobId;
    @Basic
    @Column(name = "JOB_TITLE")
    private String jobTitle;
    @Basic
    @Column(name = "MIN_SALARY")
    private BigDecimal minSalary;
    @Basic
    @Column(name = "MAX_SALARY")
    private BigDecimal maxSalary;
    @OneToMany(mappedBy = "jobsByJobId")
    private Collection<Employee> employeesByJobId;
    @OneToMany(mappedBy = "jobsByJobId")
    private Collection<JobHistory> jobHistoriesByJobId;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public BigDecimal getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(BigDecimal minSalary) {
        this.minSalary = minSalary;
    }

    public BigDecimal getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(BigDecimal maxSalary) {
        this.maxSalary = maxSalary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return Objects.equals(jobId, job.jobId) && Objects.equals(jobTitle, job.jobTitle) && Objects.equals(minSalary, job.minSalary) && Objects.equals(maxSalary, job.maxSalary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId, jobTitle, minSalary, maxSalary);
    }

    public Collection<Employee> getEmployeesByJobId() {
        return employeesByJobId;
    }

    public void setEmployeesByJobId(Collection<Employee> employeesByJobId) {
        this.employeesByJobId = employeesByJobId;
    }

    public Collection<JobHistory> getJobHistoriesByJobId() {
        return jobHistoriesByJobId;
    }

    public void setJobHistoriesByJobId(Collection<JobHistory> jobHistoriesByJobId) {
        this.jobHistoriesByJobId = jobHistoriesByJobId;
    }
}
