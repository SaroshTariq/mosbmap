package com.mosbmap.usersservice.repositories;

import com.mosbmap.usersservice.models.daos.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sarosh
 */
@Repository
public interface BuyersRepository extends JpaRepository<Buyer, String> {
    
}
