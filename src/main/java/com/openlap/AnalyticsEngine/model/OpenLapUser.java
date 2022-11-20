package com.openlap.AnalyticsEngine.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class OpenLapUser {
	@Id
	private String email;
	private String password;
	private String firstname;
	private String lastname;
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER, mappedBy = "openLapUser")
	private Set<Roles> roles = new HashSet<>();

	public OpenLapUser() {
	}

	public OpenLapUser(String email, String password, String firstname, String lastname) {
		this.email = email;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
	}

	public OpenLapUser(String email, String password, String firstname, String lastname, Set<Roles> roles) {
		this.email = email;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Set<Roles> getRoles() {
		return roles;
	}

	public void setRoles(Set<Roles> roles) {
		this.roles = roles;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OpenLapUser that = (OpenLapUser) o;
		return getEmail().equals(that.getEmail()) &&
				Objects.equals(getPassword(), that.getPassword()) &&
				Objects.equals(getFirstname(), that.getFirstname()) &&
				Objects.equals(getLastname(), that.getLastname()) &&
				Objects.equals(getRoles(), that.getRoles());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getEmail(), getPassword(), getFirstname(), getLastname(), getRoles());
	}


	//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        OpenLAPUser that = (OpenLAPUser) o;
//        return  email.equals(that.email) &&
//                password.equals(that.password) &&
//                firstname.equals(that.firstname) &&
//                lastname.equals(that.lastname) &&
//                roles.equals(that.roles);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(email, password, firstname, lastname, roles);
//    }

	@Override
	public String toString() {
		return "OpenLAPUser{" +
				"email='" + email + '\'' +
				", password='" + password + '\'' +
				", firstname='" + firstname + '\'' +
				", lastname='" + lastname + '\'' +
				", roles=" + roles +
				'}';
	}
}
