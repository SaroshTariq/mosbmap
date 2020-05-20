package com.mosbmap.usersservice.repositories;


import com.mosbmap.usersservice.models.daos.Authority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sarosh
 */
@Repository
public interface AuthoritiesRepository extends CrudRepository<Authority, String> {

}
