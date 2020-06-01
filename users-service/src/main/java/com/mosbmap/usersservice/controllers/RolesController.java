package com.mosbmap.usersservice.controllers;

import com.mosbmap.usersservice.models.HttpReponse;
import com.mosbmap.usersservice.models.daos.Role;
import com.mosbmap.usersservice.repositories.RolesRepository;
import com.mosbmap.usersservice.utils.DateTimeUtil;
import com.mosbmap.usersservice.utils.LogUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/roles")
public class RolesController {

    @Autowired
    RolesRepository rolesRepository;

    @GetMapping(path = {"/"}, name = "roles-get")
    @PreAuthorize("hasAnyAuthority('roles-get', 'all')")
    public HttpReponse getRoles(HttpServletRequest request) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        response.setSuccessStatus(HttpStatus.OK);
        response.setData(rolesRepository.findAll());
        return response;
    }

    @GetMapping(path = {"/{id}"}, name = "roles-get-by-id")
    @PreAuthorize("hasAnyAuthority('roles-get-by-id', 'all')")
    public HttpReponse getRoleById(HttpServletRequest request, @PathVariable String id) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        Optional<Role> optRole = rolesRepository.findById(id);

        if (!optRole.isPresent()) {
            LogUtil.info(logprefix, location, "role not found", "");
            response.setErrorStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        LogUtil.info(logprefix, location, "role found", "");
        response.setSuccessStatus(HttpStatus.OK);
        response.setData(optRole.get());
        return response;
    }

    @PutMapping(path = {"/{id}"}, name = "roles-put-by-id")
    @PreAuthorize("hasAnyAuthority('roles-put-by-id', 'all')")
    public HttpReponse putRole(HttpServletRequest request, @PathVariable String id, @RequestBody Role reqBody) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        Optional<Role> optRole = rolesRepository.findById(id);

        if (!optRole.isPresent()) {
            LogUtil.info(logprefix, location, "role not found", "");
            response.setErrorStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        LogUtil.info(logprefix, location, "role found", "");
        Role role = optRole.get();
        List<String> errors = new ArrayList<>();

        List<Role> roles = rolesRepository.findAll();

        for (Role existingRole : roles) {
            if (!role.equals(existingRole)) {
                if (existingRole.getId().equals(reqBody.getId())) {
                    LogUtil.info(logprefix, location, "roleid already exists", "");
                    response.setErrorStatus(HttpStatus.CONFLICT);
                    errors.add("roleId already exists");
                    response.setData(errors);
                    return response;
                }
            }

        }
        role.updateRole(reqBody);

        LogUtil.info(logprefix, location, "role created", "");
        response.setSuccessStatus(HttpStatus.CREATED);
        response.setData(rolesRepository.save(role));
        return response;
    }

    @PostMapping(name = "roles-post")
    @PreAuthorize("hasAnyAuthority('roles-post', 'all')")
    public HttpReponse postRole(HttpServletRequest request, @Valid @RequestBody Role role) throws Exception {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        LogUtil.info(logprefix, location, role.toString(), "");

        List<Role> roles = rolesRepository.findAll();
        List<String> errors = new ArrayList<>();

        for (Role existingRole : roles) {
            if (existingRole.getId().equals(role.getId())) {
                LogUtil.info(logprefix, location, "roleId already exists", "");
                response.setErrorStatus(HttpStatus.CONFLICT);
                errors.add("roleId already exists");
                response.setData(errors);
                return response;
            }
        }


        role = rolesRepository.save(role);

        LogUtil.info(logprefix, location, "role created with id: " + role.getId(), "");
        response.setSuccessStatus(HttpStatus.CREATED);
        response.setData(role);
        return response;
    }
}
