package edu.csus.asi.saferides.repository;

import edu.csus.asi.saferides.model.Coordinator;
import edu.csus.asi.saferides.security.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/*
 * @author Zeeshan Khaliq, Nik Sorvari
 * 
 * By extending CrudRepository, DriverRepository inherits several methods for working with Driver persistence, 
 * including methods for saving, deleting, and finding Driver entities
 * @see <a href="http://tinyurl.com/hxz23zt">CrudRepository</a>
 * 
 * */
public interface CoordinatorRepository extends CrudRepository<Coordinator, Long> {

    /*
     * Other query methods can be defined by simply declaring the method signature.
     * Spring Data JPA will automagically create an implementation on the fly.
     * */
    List<Coordinator> findByActive(Boolean active);

    List<Coordinator> findByActiveTrueOrderByModifiedDateDesc();

    List<Coordinator> findByActiveFalseOrderByModifiedDateDesc();

    List<Coordinator> findAllByOrderByModifiedDateDesc();

    Coordinator findByUser(User user);
}
