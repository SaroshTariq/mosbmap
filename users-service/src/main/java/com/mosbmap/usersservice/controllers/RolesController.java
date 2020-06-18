package com.mosbmap.usersservice.controllers;

import com.mosbmap.usersservice.models.HttpReponse;
import com.mosbmap.usersservice.models.daos.Authority;
import com.mosbmap.usersservice.models.daos.Role;
import com.mosbmap.usersservice.models.daos.RoleAuthorityIdentity;
import com.mosbmap.usersservice.models.daos.RoleAuthority;
import com.mosbmap.usersservice.repositories.RoleAuthoritiesRepository;
import com.mosbmap.usersservice.repositories.RolesRepository;
import com.mosbmap.usersservice.utils.LogUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/roles")
public class RolesController {

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    RoleAuthoritiesRepository roleAuthoritiesRepository;

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

    @DeleteMapping(path = {"/{id}"}, name = "roles-delete-by-id")
    @PreAuthorize("hasAnyAuthority('roles-delete-by-id', 'all')")
    public HttpReponse deleteRoleById(HttpServletRequest request, @PathVariable String id) {
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
        rolesRepository.delete(optRole.get());

        LogUtil.info(logprefix, location, "role deleted", "");
        response.setSuccessStatus(HttpStatus.OK);
        return response;
    }

    @PutMapping(path = {"/{id}"}, name = "roles-put-by-id")
    @PreAuthorize("hasAnyAuthority('roles-put-by-id', 'all')")
    public HttpReponse putRoleById(HttpServletRequest request, @PathVariable String id, @RequestBody Role reqBody) {
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
                    LogUtil.info(logprefix, location, "roleId already exists", "");
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

    @GetMapping(path = {"/{id}/authorities"}, name = "roles-get-authorities-by-roleId")
    @PreAuthorize("hasAnyAuthority('roles-get-authorities-by-roleId', 'all')")
    public HttpReponse getRoleAuthoritiesByRoleId(HttpServletRequest request, @PathVariable String id) {
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
        response.setData(roleAuthoritiesRepository.findByRoleId(id));
        return response;
    }

    @GetMapping(path = {"/{roleId}/authorities/{authorityId}"}, name = "roles-delete-authority-by-id")
    @PreAuthorize("hasAnyAuthority('roles-delete-authority-by-id', 'all')")
    public HttpReponse deleteRoleAuthority(HttpServletRequest request,
            @PathVariable String roleId, @PathVariable String authorityId) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        RoleAuthorityIdentity roleAuthoritiyIdentity = new RoleAuthorityIdentity(roleId, authorityId);
        Optional<RoleAuthority> optRoleAuthority = roleAuthoritiesRepository.findById(roleAuthoritiyIdentity);

        if (!optRoleAuthority.isPresent()) {
            LogUtil.info(logprefix, location, "role_authority not found", "");
            response.setErrorStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        LogUtil.info(logprefix, location, "role_authority found", "");
        roleAuthoritiesRepository.delete(optRoleAuthority.get());

        LogUtil.info(logprefix, location, "role_authority deleted", "");
        response.setSuccessStatus(HttpStatus.OK);
        return response;
    }

    @GetMapping(path = {"/{roleId}/authorities"}, name = "roles-post-authorities-by-roleId")
    @PreAuthorize("hasAnyAuthority('roles-post-authorities-by-roleId', 'all')")
    public HttpReponse postRoleAuthority(HttpServletRequest request, @RequestBody List<Authority> authorities, @PathVariable String id) throws Exception {
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

        RoleAuthority[] roleAuthorities = new RoleAuthority[authorities.size()];
        int i = 0;
        for (Authority authority : authorities) {
            RoleAuthority roleAuthority = new RoleAuthority();
            roleAuthority.setRoleId(id);
            roleAuthority.setAuthorityId(authority.getId());
            roleAuthoritiesRepository.save(roleAuthority);
            roleAuthorities[i] = roleAuthority;
            i = i++;
        }

        LogUtil.info(logprefix, location, i + "role_authorities created", "");
        response.setSuccessStatus(HttpStatus.CREATED);
        response.setData(roleAuthorities);
        return response;
    }
}
