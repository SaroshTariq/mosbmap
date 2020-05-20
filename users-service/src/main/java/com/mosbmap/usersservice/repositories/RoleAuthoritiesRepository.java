package com.mosbmap.usersservice.repositories;

import com.mosbmap.usersservice.models.daos.RoleAuthoritiyIdentity;
import com.mosbmap.usersservice.models.daos.RoleAuthority;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sarosh
 */
@Repository
public interface RoleAuthoritiesRepository extends JpaRepository<RoleAuthority, RoleAuthoritiyIdentity> {

    public List<RoleAuthority> findByRoleName(String roleName);
}
