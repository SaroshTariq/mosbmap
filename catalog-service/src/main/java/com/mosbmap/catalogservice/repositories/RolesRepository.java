package com.mosbmap.catalogservice.repositories;

import com.mosbmap.catalogservice.models.daos.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sarosh
 */
@Repository
public interface RolesRepository extends JpaRepository<Role, String> {
    
}
