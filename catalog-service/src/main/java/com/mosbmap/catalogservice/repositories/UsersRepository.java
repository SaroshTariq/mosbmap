package com.mosbmap.catalogservice.repositories;

import com.mosbmap.catalogservice.models.daos.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sarosh
 */
@Repository
public interface UsersRepository extends JpaRepository<User, String> {

    User findByUsername(String userName);
}
