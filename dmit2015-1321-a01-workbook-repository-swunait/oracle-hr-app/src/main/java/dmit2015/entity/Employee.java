package dmit2015.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "EMPLOYEES", schema = "HR", catalog = "")
public class Employee implements Serializable {

    @Id
    @Column(name = "EMPLOYEE_ID")
    private int employeeId;
    @Basic
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Basic
    @Column(name = "LAST_NAME")
    private String lastName;
    @Basic
    @Column(name = "EMAIL")
    private String email;
    @Basic
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Basic
    @Column(name = "HIRE_DATE")
    private LocalDate hireDate;
    @Basic
    @Column(name = "JOB_ID", insertable = false, updatable = false)
    private String jobId;
    @Basic
    @Column(name = "SALARY")
    private BigDecimal salary;
    @Basic
    @Column(name = "COMMISSION_PCT")
    private BigDecimal commissionPct;
    @Basic
    @Column(name = "MANAGER_ID", insertable = false, updatable = false)
    private Integer managerId;
    @Basic
    @Column(name = "DEPARTMENT_ID", insertable = false, updatable = false)
    private Short departmentId;
    @OneToMany(mappedBy = "employeesByManagerId")
    private Collection<Department> departmentsByEmployeeId;
    @ManyToOne
    @JoinColumn(name = "JOB_ID", referencedColumnName = "JOB_ID", nullable = false)
    private Job jobsByJobId;
    @ManyToOne
    @JoinColumn(name = "MANAGER_ID", referencedColumnName = "EMPLOYEE_ID")
    private Employee employeesByManagerId;
    @OneToMany(mappedBy = "employeesByManagerId")
    private Collection<Employee> employeesByEmployeeId;
    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID", referencedColumnName = "DEPARTMENT_ID")
    private Department departmentsByDepartmentId;
    @OneToMany(mappedBy = "employeesByEmployeeId")
    private Collection<JobHistory> jobHistoriesByEmployeeId;

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public BigDecimal getCommissionPct() {
        return commissionPct;
    }

    public void setCommissionPct(BigDecimal commissionPct) {
        this.commissionPct = commissionPct;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
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
        Employee employee = (Employee) o;
        return employeeId == employee.employeeId && Objects.equals(firstName, employee.firstName) && Objects.equals(lastName, employee.lastName) && Objects.equals(email, employee.email) && Objects.equals(phoneNumber, employee.phoneNumber) && Objects.equals(hireDate, employee.hireDate) && Objects.equals(jobId, employee.jobId) && Objects.equals(salary, employee.salary) && Objects.equals(commissionPct, employee.commissionPct) && Objects.equals(managerId, employee.managerId) && Objects.equals(departmentId, employee.departmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, firstName, lastName, email, phoneNumber, hireDate, jobId, salary, commissionPct, managerId, departmentId);
    }

    public Collection<Department> getDepartmentsByEmployeeId() {
        return departmentsByEmployeeId;
    }

    public void setDepartmentsByEmployeeId(Collection<Department> departmentsByEmployeeId) {
        this.departmentsByEmployeeId = departmentsByEmployeeId;
    }

    public Job getJobsByJobId() {
        return jobsByJobId;
    }

    public void setJobsByJobId(Job jobsByJobId) {
        this.jobsByJobId = jobsByJobId;
    }

    public Employee getEmployeesByManagerId() {
        return employeesByManagerId;
    }

    public void setEmployeesByManagerId(Employee employeesByManagerId) {
        this.employeesByManagerId = employeesByManagerId;
    }

    public Collection<Employee> getEmployeesByEmployeeId() {
        return employeesByEmployeeId;
    }

    public void setEmployeesByEmployeeId(Collection<Employee> employeesByEmployeeId) {
        this.employeesByEmployeeId = employeesByEmployeeId;
    }

    public Department getDepartmentsByDepartmentId() {
        return departmentsByDepartmentId;
    }

    public void setDepartmentsByDepartmentId(Department departmentsByDepartmentId) {
        this.departmentsByDepartmentId = departmentsByDepartmentId;
    }

    public Collection<JobHistory> getJobHistoriesByEmployeeId() {
        return jobHistoriesByEmployeeId;
    }

    public void setJobHistoriesByEmployeeId(Collection<JobHistory> jobHistoriesByEmployeeId) {
        this.jobHistoriesByEmployeeId = jobHistoriesByEmployeeId;
    }
}
