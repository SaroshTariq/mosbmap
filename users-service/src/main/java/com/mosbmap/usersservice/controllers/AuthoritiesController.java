package com.mosbmap.usersservice.controllers;

import com.mosbmap.usersservice.models.HttpReponse;
import com.mosbmap.usersservice.models.daos.Authority;
import com.mosbmap.usersservice.repositories.AuthoritiesRepository;
import com.mosbmap.usersservice.utils.LogUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/authorities")
public class AuthoritiesController {

    @Autowired
    AuthoritiesRepository authoritiesRepository;

    @GetMapping(path = {"/"}, name = "authorities-get")
    @PreAuthorize("hasAnyAuthority('authorities-get', 'all')")
    public ResponseEntity<HttpReponse> getAuthorities(HttpServletRequest request) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        response.setSuccessStatus(HttpStatus.OK);
        response.setData(authoritiesRepository.findAll());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(path = {"/{id}"}, name = "authorities-get-by-id")
    @PreAuthorize("hasAnyAuthority('authorities-get-by-id', 'all')")
    public ResponseEntity<HttpReponse> getAuthorityById(HttpServletRequest request, @PathVariable String id) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        Optional<Authority> optAuthority = authoritiesRepository.findById(id);

        if (!optAuthority.isPresent()) {
            LogUtil.info(logprefix, location, "authority not found", "");
            response.setErrorStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        LogUtil.info(logprefix, location, "authority found", "");
        response.setSuccessStatus(HttpStatus.OK);
        response.setData(optAuthority.get());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @DeleteMapping(path = {"/{id}"}, name = "authorities-delete-by-id")
    @PreAuthorize("hasAnyAuthority('authorities-delete-by-id', 'all')")
    public ResponseEntity<HttpReponse> deleteAuthorityById(HttpServletRequest request, @PathVariable String id) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        Optional<Authority> optAuthority = authoritiesRepository.findById(id);

        if (!optAuthority.isPresent()) {
            LogUtil.info(logprefix, location, "authority not found", "");
            response.setErrorStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        LogUtil.info(logprefix, location, "authority found", "");
        authoritiesRepository.delete(optAuthority.get());
        
        LogUtil.info(logprefix, location, "authority deleted", "");
        response.setSuccessStatus(HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(path = {"/{id}"}, name = "authorities-put-by-id")
    @PreAuthorize("hasAnyAuthority('authorities-put-by-id', 'all')")
    public ResponseEntity<HttpReponse> putAuthorityById(HttpServletRequest request, @PathVariable String id, 
            @RequestBody Authority body) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");
        LogUtil.info(logprefix, location, body.toString(), "");

        Optional<Authority> optAuthority = authoritiesRepository.findById(id);

        if (!optAuthority.isPresent()) {
            LogUtil.info(logprefix, location, "authority not found", "");
            response.setErrorStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        LogUtil.info(logprefix, location, "authority found", "");
        Authority authority = optAuthority.get();
        List<String> errors = new ArrayList<>();

        List<Authority> authorities = authoritiesRepository.findAll();

        for (Authority existingAuthority : authorities) {
            if (!authority.equals(existingAuthority)) {
                if (existingAuthority.getId().equals(body.getId())) {
                    LogUtil.info(logprefix, location, "authorityId already exists", "");
                    response.setErrorStatus(HttpStatus.CONFLICT);
                    errors.add("authorityId already exists");
                    response.setData(errors);
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
                }
            }

        }
        authority.updateAuthority(body);

        LogUtil.info(logprefix, location, "authority updated for id: "+body.getId(), "");
        response.setSuccessStatus(HttpStatus.ACCEPTED);
        response.setData(authoritiesRepository.save(authority));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PostMapping(name = "authorities-post")
    @PreAuthorize("hasAnyAuthority('authorities-post', 'all')")
    public ResponseEntity<HttpReponse> postAuthority(HttpServletRequest request, 
            @Valid @RequestBody Authority body) throws Exception {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

       LogUtil.info(logprefix, location, "", "");
        LogUtil.info(logprefix, location, body.toString(), "");

        List<Authority> authorities = authoritiesRepository.findAll();
        List<String> errors = new ArrayList<>();

        for (Authority existingAuthority : authorities) {
            if (existingAuthority.getId().equals(body.getId())) {
                LogUtil.info(logprefix, location, "authorityId already exists", "");
                response.setErrorStatus(HttpStatus.CONFLICT);
                errors.add("authorityId already exists");
                response.setData(errors);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
        }

        body = authoritiesRepository.save(body);

        LogUtil.info(logprefix, location, "authority created with id: " + body.getId(), "");
        response.setSuccessStatus(HttpStatus.CREATED);
        response.setData(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
