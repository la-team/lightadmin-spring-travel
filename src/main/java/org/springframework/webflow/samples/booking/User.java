package org.springframework.webflow.samples.booking;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * A user who can book hotels.
 */
@Entity
@Table(name = "Customer")
public class User implements Serializable {

	@Id
	@NotBlank
    private String username;

    private String password;

    private String name;

    public User() {
    }

    public User(String username, String password, String name) {
	this.username = username;
	this.password = password;
	this.name = name;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Override
    public String toString() {
	return "User(" + username + ")";
    }

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj ) {
			return true;
		}

		if ( this.username == null || obj == null || !( this.getClass().equals( obj.getClass() ) ) ) {
			return false;
		}

		User that = ( User ) obj;

		return this.username.equals( that.getUsername() );
	}

	@Override
	public int hashCode() {
		return username == null ? 0 : username.hashCode();
	}
}
