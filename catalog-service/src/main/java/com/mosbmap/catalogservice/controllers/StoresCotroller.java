package com.mosbmap.catalogservice.controllers;


import com.mosbmap.catalogservice.repositories.StoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Sarosh
 */
@RestController()
@RequestMapping("/stores")
public class StoresCotroller {

    @Autowired
    StoresRepository storesRepository;
}
