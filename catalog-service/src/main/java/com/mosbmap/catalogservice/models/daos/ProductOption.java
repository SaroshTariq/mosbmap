package com.mosbmap.catalogservice.models.daos;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Sarosh
 */
@Entity
@Table(name = "product_options")
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductOption implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotBlank(message = "name is required")
    private String name;

    private String imageUrl;
    
    @NotBlank(message = "stock is required")
    private int stock;
    
    
    @NotBlank(message = "type is required")
    private String type;
    
    
    @NotBlank(message = "optionCategory is required")
    private String optionCategory;

    
    @NotBlank(message = "productId is required")
    private String productId;
}
