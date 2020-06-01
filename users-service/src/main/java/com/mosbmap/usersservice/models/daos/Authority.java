package com.mosbmap.usersservice.models.daos;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Sarosh
 */
@Entity
@Table(name = "authorities")
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Authority implements Serializable {

    @Id
    @Column(name = "id")
    @NotBlank(message = "id is required")
    private String id;

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "description is required")
    private String description;

    public void updateAuthority(Authority authority) {
        if (null != authority.getId()) {
            id = authority.getId();
        }

        if (null != authority.getName()) {
            name = authority.getName();
        }

        if (null != authority.getDescription()) {
            name = authority.getDescription();
        }

    }
}
