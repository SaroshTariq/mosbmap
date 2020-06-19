package com.mosbmap.catalogservice.controllers;

import com.mosbmap.catalogservice.models.HttpReponse;
import com.mosbmap.catalogservice.models.daos.Product;
import com.mosbmap.catalogservice.models.daos.ProductFeature;
import com.mosbmap.catalogservice.models.daos.ProductOption;
import com.mosbmap.catalogservice.repositories.ProductFeaturesRepository;
import com.mosbmap.catalogservice.repositories.ProductOptionsRepository;
import com.mosbmap.catalogservice.repositories.ProductsRepository;
import com.mosbmap.catalogservice.utils.LogUtil;
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
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    ProductsRepository productsRepository;

    @Autowired
    ProductFeaturesRepository productFeaturesRepository;
    
    @Autowired
    ProductOptionsRepository productOptionsRepository;

    @GetMapping(path = {"/"}, name = "products-get")
    @PreAuthorize("hasAnyProduct('products-get', 'all')")
    public HttpReponse getProducts(HttpServletRequest request) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        response.setSuccessStatus(HttpStatus.OK);
        response.setData(productsRepository.findAll());
        return response;
    }

    @GetMapping(path = {"/{id}"}, name = "products-get-by-id")
    @PreAuthorize("hasAnyProduct('products-get-by-id', 'all')")
    public HttpReponse getProductById(HttpServletRequest request, @PathVariable String id) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        Optional<Product> optProduct = productsRepository.findById(id);

        if (!optProduct.isPresent()) {
            LogUtil.info(logprefix, location, "product not found", "");
            response.setErrorStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        LogUtil.info(logprefix, location, "product found", "");
        response.setSuccessStatus(HttpStatus.OK);
        response.setData(optProduct.get());
        return response;
    }

    @DeleteMapping(path = {"/{id}"}, name = "products-delete-by-id")
    @PreAuthorize("hasAnyProduct('products-delete-by-id', 'all')")
    public HttpReponse deleteProductById(HttpServletRequest request, @PathVariable String id) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        Optional<Product> optProduct = productsRepository.findById(id);

        if (!optProduct.isPresent()) {
            LogUtil.info(logprefix, location, "product not found", "");
            response.setErrorStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        LogUtil.info(logprefix, location, "product found", "");
        productsRepository.delete(optProduct.get());

        LogUtil.info(logprefix, location, "product deleted", "");
        response.setSuccessStatus(HttpStatus.OK);
        return response;
    }

    @PutMapping(path = {"/{id}"}, name = "products-put-by-id")
    @PreAuthorize("hasAnyProduct('products-put-by-id', 'all')")
    public HttpReponse putProductById(HttpServletRequest request, @PathVariable String id,
            @RequestBody Product body) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");
        LogUtil.info(logprefix, location, body.toString(), "");

        Optional<Product> optProduct = productsRepository.findById(id);

        if (!optProduct.isPresent()) {
            LogUtil.info(logprefix, location, "product not found", "");
            response.setErrorStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        LogUtil.info(logprefix, location, "product found", "");
        Product product = optProduct.get();
        List<String> errors = new ArrayList<>();

        List<Product> products = productsRepository.findAll();

        for (Product existingProduct : products) {
            if (!product.equals(existingProduct)) {
                if (existingProduct.getId().equals(body.getId())) {
                    LogUtil.info(logprefix, location, "productId already exists", "");
                    response.setErrorStatus(HttpStatus.CONFLICT);
                    errors.add("productId already exists");
                    response.setData(errors);
                    return response;
                }
            }

        }
        product.updateProduct(body);

        LogUtil.info(logprefix, location, "product created with id: " + product.getId(), "");
        response.setSuccessStatus(HttpStatus.CREATED);
        response.setData(productsRepository.save(product));
        return response;
    }

    @PostMapping(name = "products-post")
    @PreAuthorize("hasAnyProduct('products-post', 'all')")
    public HttpReponse postProduct(HttpServletRequest request,
            @Valid @RequestBody Product body) throws Exception {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");
        LogUtil.info(logprefix, location, body.toString(), "");

        List<Product> products = productsRepository.findAll();
        List<String> errors = new ArrayList<>();

        for (Product existingProduct : products) {
            if (existingProduct.getId().equals(body.getId())) {
                LogUtil.info(logprefix, location, "productId already exists", "");
                response.setErrorStatus(HttpStatus.CONFLICT);
                errors.add("productId already exists");
                response.setData(errors);
                return response;
            }
        }

        body = productsRepository.save(body);

        LogUtil.info(logprefix, location, "product created with id: " + body.getId(), "");
        response.setSuccessStatus(HttpStatus.CREATED);
        response.setData(body);
        return response;
    }

    @GetMapping(path = {"/{productId}/features"}, name = "products-get-features-by-productId")
    @PreAuthorize("hasAnyAuthority('products-get-features-by-productId', 'all')")
    public HttpReponse getProductFeatureByProductId(HttpServletRequest request,
            @PathVariable String productId) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        Optional<Product> optProductFeature = productsRepository.findById(productId);

        if (!optProductFeature.isPresent()) {
            LogUtil.info(logprefix, location, "product not found", "");
            response.setErrorStatus(HttpStatus.NOT_FOUND, "product not found");
            return response;
        }

        LogUtil.info(logprefix, location, "product found", "");

        response.setSuccessStatus(HttpStatus.OK);
        response.setData(productFeaturesRepository.findByProductId(productId));
        return response;
    }

    @DeleteMapping(path = {"/features/{id}"}, name = "products-delete-features-by-id")
    @PreAuthorize("hasAnyAuthority('products-delete-features-by-id', 'all')")
    public HttpReponse deleteProductFeature(HttpServletRequest request,
            @PathVariable String id) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        Optional<ProductFeature> optProductFeature = productFeaturesRepository.findById(id);

        if (!optProductFeature.isPresent()) {
            LogUtil.info(logprefix, location, "product_feature not found", "");
            response.setErrorStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        LogUtil.info(logprefix, location, "product_feature found", "");
        productFeaturesRepository.delete(optProductFeature.get());

        LogUtil.info(logprefix, location, "product_feature deleted", "");
        response.setSuccessStatus(HttpStatus.OK);
        return response;
    }

    @PostMapping(path = {"/{productId}/features"}, name = "products-post-features-by-productId")
    @PreAuthorize("hasAnyAuthority('products-post-features-by-productId', 'all')")
    public HttpReponse postProductFeatureByProductId(HttpServletRequest request,
            @PathVariable String productId,
            @Valid @RequestBody List<ProductFeature> body) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");
        LogUtil.info(logprefix, location, body.toString(), "");

        Optional<Product> optProduct = productsRepository.findById(productId);

        if (!optProduct.isPresent()) {
            LogUtil.info(logprefix, location, "product not found", "");
            response.setErrorStatus(HttpStatus.NOT_FOUND, "product not found");
            return response;
        }

        LogUtil.info(logprefix, location, "product found", "");
        ProductFeature[] productFeatures = new ProductFeature[body.size()];

        int i = 0;
        for (ProductFeature productFeature : body) {
            productFeature.setProductId(productId);
            productFeatures[i] = productFeaturesRepository.save(productFeature);
            i++;
        }

        LogUtil.info(logprefix, location, "product_features created for productId: " + productId, "");
        response.setSuccessStatus(HttpStatus.CREATED);
        response.setData(productFeatures);
        return response;
    }
    
    
    @GetMapping(path = {"/{productId}/options"}, name = "products-get-options-by-productId")
    @PreAuthorize("hasAnyAuthority('products-get-options-by-productId', 'all')")
    public HttpReponse getProductOptionByProductId(HttpServletRequest request,
            @PathVariable String productId) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        Optional<Product> optProductOption = productsRepository.findById(productId);

        if (!optProductOption.isPresent()) {
            LogUtil.info(logprefix, location, "product not found", "");
            response.setErrorStatus(HttpStatus.NOT_FOUND, "product not found");
            return response;
        }

        LogUtil.info(logprefix, location, "product found", "");

        response.setSuccessStatus(HttpStatus.OK);
        response.setData(productOptionsRepository.findByProductId(productId));
        return response;
    }

    @DeleteMapping(path = {"/options/{id}"}, name = "products-delete-options-by-id")
    @PreAuthorize("hasAnyAuthority('products-delete-options-by-id', 'all')")
    public HttpReponse deleteProductOption(HttpServletRequest request,
            @PathVariable String id) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        Optional<ProductOption> optProductOption = productOptionsRepository.findById(id);

        if (!optProductOption.isPresent()) {
            LogUtil.info(logprefix, location, "product_option not found", "");
            response.setErrorStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        LogUtil.info(logprefix, location, "product_option found", "");
        productOptionsRepository.delete(optProductOption.get());

        LogUtil.info(logprefix, location, "product_option deleted", "");
        response.setSuccessStatus(HttpStatus.OK);
        return response;
    }

    @PostMapping(path = {"/{productId}/options"}, name = "products-post-options-by-productId")
    @PreAuthorize("hasAnyAuthority('products-post-options-by-productId', 'all')")
    public HttpReponse postProductOptionByProductId(HttpServletRequest request,
            @PathVariable String productId,
            @Valid @RequestBody List<ProductOption> body) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");
        LogUtil.info(logprefix, location, body.toString(), "");

        Optional<Product> optProduct = productsRepository.findById(productId);

        if (!optProduct.isPresent()) {
            LogUtil.info(logprefix, location, "product not found", "");
            response.setErrorStatus(HttpStatus.NOT_FOUND, "product not found");
            return response;
        }

        LogUtil.info(logprefix, location, "product found", "");
        ProductOption[] productOptions = new ProductOption[body.size()];

        int i = 0;
        for (ProductOption productOption : body) {
            productOption.setProductId(productId);
            productOptions[i] = productOptionsRepository.save(productOption);
            i++;
        }

        LogUtil.info(logprefix, location, "product_options created for productId: " + productId, "");
        response.setSuccessStatus(HttpStatus.CREATED);
        response.setData(productOptions);
        return response;
    }

}
