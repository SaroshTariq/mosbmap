package com.mosbmap.catalogservice.repositories;

import com.mosbmap.catalogservice.models.daos.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sarosh
 */
@Repository
public interface ProductsRepository extends JpaRepository<Product, String> {
    
}
