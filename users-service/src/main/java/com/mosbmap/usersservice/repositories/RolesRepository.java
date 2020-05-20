package com.mosbmap.usersservice.repositories;

import com.mosbmap.usersservice.models.daos.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sarosh
 */
@Repository
public interface RolesRepository extends CrudRepository<Role, String> {
    
}
