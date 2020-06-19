package com.mosbmap.catalogservice.repositories;

import com.mosbmap.catalogservice.models.daos.Session;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Sarosh
 */
public interface SessionsRepository extends JpaRepository<Session, String> {

}
