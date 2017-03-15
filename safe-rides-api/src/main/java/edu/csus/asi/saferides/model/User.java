package edu.csus.asi.saferides.model;

import javax.persistence.Column;
import javax.persistence.Entity;
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
	static final int SALTLEN = 32;
	static final int HASHLEN = 64;
	
	@Column
	private String name;
	
	@Id
	@Column(unique = true)
	private String username;
	
	@Column
	private String password;
	
	// protected Constructor required for JPA
	protected User() { }

	public User(String name, String username, String password) {
		super();
		this.name = name;
		this.username = username;
		setPassword(password);
	}
	

	public String getName() {
		return name;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id, SALTLEN, HASHLEN);
		// Hash password
		String hash = argon2.hash(2, 65536, 1, password);
	    /**
	     * String hash(int iterations, int memory, int parallelism, char[] password);
	     * Hashes a password.
	     * @param iterations  Number of iterations
	     * @param memory      Sets memory usage to x kibibytes
	     * @param parallelism Number of threads and compute lanes
	     * @param password    Password to hash
	     * @param charset     Charset of the password
	     * @return Hashed password.
	     */
		this.password = hash;
	}
	
	public Boolean checkPassword(String password){
		Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id);
		return argon2.verify(this.password, password);
	}
}
