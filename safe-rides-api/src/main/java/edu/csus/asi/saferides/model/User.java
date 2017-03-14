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
	
	@Column(unique = true)
	private String username;
	
	@Column
	private byte[] salt;
	
	@Column
	private String password;
	
	// protected Constructor required for JPA
	protected User() { }

	public User(String name, String username, String password) {
		super();
		this.name = name;
		this.username = username;
		generateSalt();
		setPassword(password);
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

	public void setPassword(String password) {
		Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id);
		// Hash password
		String saltedPassword = this.salt.toString() + password; 
		String hash = argon2.hash(2, 65536, 1, saltedPassword);
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
		String saltedPassword = this.salt.toString() + password;
		return argon2.verify(this.password, saltedPassword);
	}
}
