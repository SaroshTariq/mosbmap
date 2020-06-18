package com.mosbmap.usersservice.models.daos;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Objects;
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
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Role implements Serializable {

    @Id
    @Column(name = "id")
    @NotBlank(message = "id is required")
    private String id;

    @NotBlank(message = "simoultaneousLogins is required")
    private int simoultaneousLogins;
    private String description;

    public void updateRole(Role role) {
        if (null != role.getId()) {
            id = role.getId();
        }

        if (null != role.getDescription()) {
            description = role.getDescription();
        }

        if (0 != role.getSimoultaneousLogins()) {
            simoultaneousLogins = role.getSimoultaneousLogins();
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
        final Role other = (Role) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    

}
