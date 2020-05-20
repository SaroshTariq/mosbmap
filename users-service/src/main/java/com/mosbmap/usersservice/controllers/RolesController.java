package com.mosbmap.usersservice.controllers;

import com.mosbmap.usersservice.models.HttpReponse;
import com.mosbmap.usersservice.models.daos.Role;
import com.mosbmap.usersservice.repositories.RolesRepository;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Sarosh
 */
@RestController()
@RequestMapping("/role")
public class RolesController {

    @Autowired
    private RolesRepository rolesRepositories;

    @GetMapping(name = "role-get")
    public HttpReponse getRoles(HttpServletRequest request) {
        HttpReponse response = new HttpReponse(request.getRequestURI());

        response.setStatus(HttpStatus.OK);
        response.setData(rolesRepositories.findAll());
        return response;
    }

    @GetMapping(path = "/{name}", name = "role-get-by-name")
    public HttpReponse getRoleByName(HttpServletRequest request, @PathVariable("name") String name) {
        HttpReponse response = new HttpReponse(request.getRequestURI());

        response.setData(rolesRepositories.findById(name));

        response.setStatus(HttpStatus.OK);

        return response;
    }

    @PutMapping(path = "/{name}", name = "role-put-by-name")
    public HttpReponse putRoleByName(HttpServletRequest request, @Valid @RequestBody Role role) {
        HttpReponse response = new HttpReponse(request.getRequestURI());

        response.setData(rolesRepositories.save(role));

        response.setStatus(HttpStatus.OK);

        return response;
    }

    @DeleteMapping(path = "/{name}", name = "role-delete-by-name")
    public HttpReponse deleteRoleByName(HttpServletRequest request, @PathVariable("name") String name) {
        rolesRepositories.deleteById(name);

        HttpReponse response = new HttpReponse(request.getRequestURI());

        response.setStatus(HttpStatus.OK);
        response.setData(rolesRepositories.findAll());

        return response;
    }

    @PostMapping(name = "role-post")
    public HttpReponse postRole(HttpServletRequest request) {
        return null;
    }
}
