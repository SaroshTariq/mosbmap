package com.mosbmap.catalogservice.repositories;

import com.mosbmap.catalogservice.models.daos.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sarosh
 */
@Repository
public interface SellersRepository extends JpaRepository<Seller, String> {

}
