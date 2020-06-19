package com.mosbmap.catalogservice.repositories;

import com.mosbmap.catalogservice.models.daos.ProductFeature;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sarosh
 */
@Repository
public interface ProductFeaturesRepository extends JpaRepository<ProductFeature, String> {

    public List<ProductFeature> findByProductId(String productId);  
}
