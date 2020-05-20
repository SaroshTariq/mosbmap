package com.mosbmap.usersservice.models.daos;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Sarosh
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleAuthoritiyIdentity implements Serializable {

    private String roleName;
    private String authorityName;

    public RoleAuthoritiyIdentity(String roleName, String authorityName) {
        this.roleName = roleName;
        this.authorityName = authorityName;
    }

}
