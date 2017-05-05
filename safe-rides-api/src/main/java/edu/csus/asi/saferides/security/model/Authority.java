package edu.csus.asi.saferides.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Authority / Role for defining user access rights
 */
@Entity
public class Authority {

    /**
     * Primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Name of authority / role
     */
    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private AuthorityName name;

    /**
     * List of users in authority / role
     */
    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> users;

    /**
     * Constructor used by JPA
     */
    protected Authority() {
    }

    /**
     * Constructor for instantiation a new Authority
     *
     * @param name of authority
     */
    public Authority(AuthorityName name) {
        this.name = name;
    }

    /**
     * Get authority's id
     *
     * @return authority's primary key
     */
    public Long getId() {
        return id;
    }

    /**
     * Set authority's id
     *
     * @param id of authority
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get authority's name
     *
     * @return authority name
     */
    public AuthorityName getName() {
        return name;
    }

    /**
     * Set authority's name
     *
     * @param name of authority
     */
    public void setName(AuthorityName name) {
        this.name = name;
    }

    /**
     * Get authority's users
     *
     * @return users
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Set authority's users
     *
     * @param users in authority
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }
}