package dmit2015.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class JobHistoryPK implements Serializable {
    @Column(name = "EMPLOYEE_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int employeeId;
    @Column(name = "START_DATE")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private LocalDate startDate;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobHistoryPK that = (JobHistoryPK) o;
        return employeeId == that.employeeId && Objects.equals(startDate, that.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, startDate);
    }
}
