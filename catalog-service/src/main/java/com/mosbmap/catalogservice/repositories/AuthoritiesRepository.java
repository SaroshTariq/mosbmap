package com.mosbmap.catalogservice.repositories;

import com.mosbmap.catalogservice.models.daos.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sarosh
 */
@Repository
public interface AuthoritiesRepository extends JpaRepository<Authority, String> {

}
