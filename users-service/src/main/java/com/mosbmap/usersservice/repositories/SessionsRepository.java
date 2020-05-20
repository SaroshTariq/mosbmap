package com.mosbmap.usersservice.repositories;

import com.mosbmap.usersservice.models.daos.Session;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Sarosh
 */
public interface SessionsRepository extends JpaRepository<Session, String> {

}
