package com.mosbmap.usersservice.controllers;


import com.mosbmap.usersservice.models.HttpReponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Sarosh
 */
@RestController()
@RequestMapping("/role_authorities")
public class RoleAuthoritiesController {

    @GetMapping(name = "role_authorities-get")
    public HttpReponse getRoleAuthorities() {
        return null;
    }

    @PostMapping(name = "role_authority-post")
    public HttpReponse postRoleAuthority(HttpServletRequest request) {
        return null;
    }

    @DeleteMapping(path = {"/{role}/{authority}"}, name = "role_authority-delete-by-role_authority")
    public HttpReponse deleteRoleAuthorityByRoleAuthority() {
        return null;
    }
}
