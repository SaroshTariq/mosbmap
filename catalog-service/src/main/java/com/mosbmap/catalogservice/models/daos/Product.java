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
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "stock is required")
    private int stock;

    @NotBlank(message = "categoryId is required")
    private String categoryId;

    private String storeId;

    public void updateProduct(Product product) {
        if (null != product.getName()) {
            name = product.getName();
        }

        if (stock != product.getStock()) {
            stock = product.getStock();
        }

        if (null != product.getCategoryId()) {
            categoryId = product.getCategoryId();
        }

        storeId = product.getStoreId();

    }

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", name=" + name + ", stock=" + stock + ", categoryId=" + categoryId + ", storeId=" + storeId + '}';
    }

}
