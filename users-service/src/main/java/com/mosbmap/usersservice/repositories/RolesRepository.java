package com.mosbmap.usersservice.repositories;

import com.mosbmap.usersservice.models.daos.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sarosh
 */
@Repository
public interface RolesRepository extends PagingAndSortingRepository<Role, String>, JpaRepository<Role, String> {
    
}
