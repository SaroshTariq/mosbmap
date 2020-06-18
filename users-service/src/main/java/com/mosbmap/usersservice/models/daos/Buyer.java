package com.mosbmap.usersservice.models.daos;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Objects;
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
@Table(name = "buyers")
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Buyer implements Serializable {

    @NotBlank(message = "city is required")
    private String city;

    @NotBlank(message = "mobileNumber is required")
    private String mobileNumber;

    @NotBlank(message = "address is required")
    private String address;

    @NotBlank(message = "zipcode is required")
    private String zipcode;

    
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String userId;


    public void updateBuyer(Buyer buyer) {
        if (null != buyer.getCity()) {
            city = buyer.getCity();
        }

        if (null != buyer.getMobileNumber()) {
            mobileNumber = buyer.getMobileNumber();
        }

        if (null != buyer.getZipcode()) {
            zipcode = buyer.getZipcode();
        }

        if (null != buyer.getUserId()) {
            userId = buyer.getUserId();
        }

    }



    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Buyer other = (Buyer) obj;
        if (!Objects.equals(this.userId, other.userId)) {
            return false;
        }
        return true;
    }

    
}

