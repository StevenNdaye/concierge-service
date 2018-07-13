package com.concierge.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Enumerated;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerResponse {
    private Integer id;
    private String firstName;
    private String lastName;

    private Date birthDate;
    private Date enrollment;
    @Enumerated
    private FinancialStatus status;

    public CustomerResponse(Integer id, String firstName, String lastName, Date birthDate, Date enrollment, FinancialStatus status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.enrollment = enrollment;
        this.status = status;
    }

    public CustomerResponse() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Date enrollment) {
        this.enrollment = enrollment;
    }

    public FinancialStatus getStatus() {
        return status;
    }

    public void setStatus(FinancialStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerResponse that = (CustomerResponse) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
        if (birthDate != null ? !birthDate.equals(that.birthDate) : that.birthDate != null) return false;
        if (enrollment != null ? !enrollment.equals(that.enrollment) : that.enrollment != null) return false;
        return status == that.status;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
        result = 31 * result + (enrollment != null ? enrollment.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
