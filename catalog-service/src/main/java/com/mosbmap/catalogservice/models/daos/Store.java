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
@Table(name = "stores")
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Store implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "city is required")
    private String city;

    @NotBlank(message = "address is required")
    private String address;

    @NotBlank(message = "latitude is required")
    private String latitude;

    @NotBlank(message = "longitude is required")
    private String longitude;

     @NotBlank(message = "sellerId is required")
    private String sellerId;
}
