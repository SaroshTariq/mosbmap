package com.mosbmap.usersservice.repositories;

import com.mosbmap.usersservice.models.daos.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sarosh
 */
@Repository
public interface UsersRepository extends PagingAndSortingRepository<User, String>, JpaRepository<User, String> {

    User findByUsername(String userName);
}
