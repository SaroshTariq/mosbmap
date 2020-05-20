package com.mosbmap.usersservice.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Sarosh
 */
@RestController()
@RequestMapping("/authorities")
public class AuthoritiesController {

    @GetMapping(name = "authorities-get")
    public ResponseEntity<Object> getAuthorities() {
        return new ResponseEntity<>("Get to Home!!", HttpStatus.OK);
    }

    @GetMapping(path = {"/{name}"}, name = "authority-get-by-name")
    public ResponseEntity<Object> getAuthorityByName() {
        return new ResponseEntity<>("Get to Home!!", HttpStatus.OK);
    }

    @PutMapping(path = {"/{name}"}, name = "authority-put-by-name")
    public ResponseEntity<Object> putAuthorityByName() {
        return new ResponseEntity<>("Get to Home!!", HttpStatus.OK);
    }

    @PostMapping(name = "authority-post")
    public ResponseEntity<Object> postAuthority() {
        return new ResponseEntity<>("Post to Home!!", HttpStatus.OK);
    }

    @DeleteMapping(path = {"/{name}"}, name = "authority-delete-by-name")
    public ResponseEntity<Object> deleteAuthorityByName() {
        return new ResponseEntity<>("Get to Home!!", HttpStatus.OK);
    }
}
