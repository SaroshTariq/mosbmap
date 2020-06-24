package com.mosbmap.catalogservice.repositories;

import com.mosbmap.catalogservice.models.daos.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sarosh
 */
@Repository
public interface StoresRepository extends JpaRepository<Store, String> {
    
}
