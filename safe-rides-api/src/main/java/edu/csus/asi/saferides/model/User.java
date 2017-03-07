package edu.csus.asi.saferides.model;

import java.security.SecureRandom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;

/*
 * @author Ryan Long
 * 
 * Model object for User Entity
 * */



@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) // Will generate a unique id automatically
	private Long id;
	
	@Column
	private String name;
	
	@Column
	private String username;
	
	@Column
	private byte[] salt;
	
	@Column
	private char[] password;
	
	// protected Constructor required for JPA
	protected User() { }

	public User(Long id, String name, String username, char[] password) {
		super();
		this.id = id;
		this.name = name;
		this.username = username;
		generateSalt();
		this.password = password;
	}
	
	protected void generateSalt(){
		SecureRandom sr = new SecureRandom(); 
		this.salt = new byte[32];
		sr.nextBytes(this.salt);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getUsername() {
		return username;
	}

	public byte[] getSalt() {
		return salt;
	}

	public void setPassword(char[] password) {
		Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id);
		try {
		    // Hash password
		    String hash = argon2.hash(2, 65536, 1, password);

		    // Verify password
		    if (argon2.verify(hash, password)) {
		        // Hash matches password
		    } else {
		        // Hash doesn't match password
		    }
		} finally {
		// Wipe confidential data
	    argon2.wipeArray(password);
		}
	}
	
	
}
