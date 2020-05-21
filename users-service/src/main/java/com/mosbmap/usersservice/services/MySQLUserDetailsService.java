package com.mosbmap.usersservice.services;

import com.mosbmap.usersservice.models.MySQLUserDetails;
import com.mosbmap.usersservice.models.daos.RoleAuthority;
import com.mosbmap.usersservice.models.daos.User;
import com.mosbmap.usersservice.repositories.RoleAuthoritiesRepository;
import com.mosbmap.usersservice.repositories.UsersRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Sarosh
 */
@Service
public class MySQLUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RoleAuthoritiesRepository roleAuthoritiesRepository;

    @Override
    public MySQLUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        List<RoleAuthority> auths = roleAuthoritiesRepository.findByRoleId(user.getRoleId());

        return new MySQLUserDetails(user, auths);
    }
    
    

}
