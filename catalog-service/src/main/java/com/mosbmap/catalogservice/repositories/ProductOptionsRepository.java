package com.mosbmap.catalogservice.repositories;

import com.mosbmap.catalogservice.models.daos.ProductOption;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sarosh
 */
@Repository
public interface ProductOptionsRepository extends JpaRepository<ProductOption, String> {

    public List<ProductOption> findByProductId(String productId);
}
