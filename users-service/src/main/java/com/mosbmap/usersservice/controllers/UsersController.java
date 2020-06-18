package com.mosbmap.usersservice.controllers;

import com.mosbmap.usersservice.models.HttpReponse;
import com.mosbmap.usersservice.models.daos.Buyer;
import com.mosbmap.usersservice.models.daos.Seller;
import com.mosbmap.usersservice.models.daos.Session;
import com.mosbmap.usersservice.models.daos.User;
import com.mosbmap.usersservice.models.requestbodies.UserAuthenticationBody;
import com.mosbmap.usersservice.repositories.BuyersRepository;
import com.mosbmap.usersservice.repositories.SellersRepository;
import com.mosbmap.usersservice.repositories.SessionsRepository;
import com.mosbmap.usersservice.repositories.UsersRepository;
import com.mosbmap.usersservice.utils.DateTimeUtil;
import com.mosbmap.usersservice.utils.LogUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
@RequestMapping("/users")
public class UsersController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    BuyersRepository buyersRepostory;

    @Autowired
    private SellersRepository sellersRepostory;

    @Autowired
    SessionsRepository sessionsRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Value("${session.expiry:3600}")
    private int expiry;

    @GetMapping(path = {"/"}, name = "users-get")
    @PreAuthorize("hasAnyAuthority('users-get', 'all')")
    public HttpReponse getUsers(HttpServletRequest request) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        response.setSuccessStatus(HttpStatus.OK);
        response.setData(usersRepository.findAll());
        return response;
    }

    @GetMapping(path = {"/{id}"}, name = "users-get-by-id")
    @PreAuthorize("hasAnyAuthority('users-get-by-id', 'all')")
    public HttpReponse getUserById(HttpServletRequest request, @PathVariable String id) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        Optional<User> optUser = usersRepository.findById(id);

        if (!optUser.isPresent()) {
            LogUtil.info(logprefix, location, "user not found", "");
            response.setErrorStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        LogUtil.info(logprefix, location, "user found", "");
        response.setSuccessStatus(HttpStatus.OK);
        response.setData(optUser.get());
        return response;
    }

    @DeleteMapping(path = {"/{id}"}, name = "users-delete-by-id")
    @PreAuthorize("hasAnyAuthority('users-delete-by-id', 'all')")
    public HttpReponse deleteUserById(HttpServletRequest request, @PathVariable String id) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        Optional<User> optUser = usersRepository.findById(id);

        if (!optUser.isPresent()) {
            LogUtil.info(logprefix, location, "user not found", "");
            response.setErrorStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        LogUtil.info(logprefix, location, "user found", "");
        usersRepository.delete(optUser.get());

        LogUtil.info(logprefix, location, "user deleted", "");
        response.setSuccessStatus(HttpStatus.OK);
        return response;
    }

    @PutMapping(path = {"/{id}"}, name = "users-put-by-id")
    @PreAuthorize("hasAnyAuthority('users-put-by-id', 'all')")
    public HttpReponse putUserById(HttpServletRequest request, @PathVariable String id, @RequestBody User reqBody) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        Optional<User> optUser = usersRepository.findById(id);

        if (!optUser.isPresent()) {
            LogUtil.info(logprefix, location, "user not found", "");
            response.setErrorStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        LogUtil.info(logprefix, location, "user found", "");
        User user = optUser.get();
        List<String> errors = new ArrayList<>();

        List<User> users = usersRepository.findAll();

        for (User existingUser : users) {
            if (!user.equals(existingUser)) {
                if (existingUser.getUsername().equals(reqBody.getUsername())) {
                    LogUtil.info(logprefix, location, "username already exists", "");
                    response.setErrorStatus(HttpStatus.CONFLICT);
                    errors.add("username already exists");
                    response.setData(errors);
                    return response;
                }
                if (existingUser.getEmail().equals(reqBody.getEmail())) {
                    LogUtil.info(logprefix, location, "email already exists", "");
                    response.setErrorStatus(HttpStatus.CONFLICT);
                    errors.add("email already exists");
                    response.setData(errors);
                    return response;
                }
            }

        }

        if (null != reqBody.getPassword()) {
            reqBody.setPassword(bcryptEncoder.encode(reqBody.getPassword()));
        }

        user.updateUser(reqBody);
        user.setUpdated(DateTimeUtil.currentTimestamp());

        LogUtil.info(logprefix, location, "user created", "");
        response.setSuccessStatus(HttpStatus.CREATED);
        response.setData(usersRepository.save(user));
        return response;
    }

    @PostMapping(name = "users-post")
    @PreAuthorize("hasAnyAuthority('users-post', 'all')")
    public HttpReponse postUser(HttpServletRequest request, @Valid @RequestBody User user) throws Exception {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        LogUtil.info(logprefix, location, user.toString(), "");

        List<User> users = usersRepository.findAll();
        List<String> errors = new ArrayList<>();

        for (User existingUser : users) {
            if (existingUser.getUsername().equals(user.getUsername())) {
                LogUtil.info(logprefix, location, "username already exists", "");
                response.setErrorStatus(HttpStatus.CONFLICT);
                errors.add("username already exists");
                response.setData(errors);
                return response;
            }
            if (existingUser.getEmail().equals(user.getEmail())) {
                LogUtil.info(logprefix, location, "email already exists", "");
                response.setErrorStatus(HttpStatus.CONFLICT);
                errors.add("email already exists");
                response.setData(errors);
                return response;
            }
        }

        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        user.setCreated(DateTimeUtil.currentTimestamp());
        user.setUpdated(DateTimeUtil.currentTimestamp());
        user.setLocked(false);
        user = usersRepository.save(user);
        user.setPassword(null);
        LogUtil.info(logprefix, location, "user created with id: " + user.getId(), "");
        response.setSuccessStatus(HttpStatus.CREATED);
        response.setData(user);
        return response;
    }

    @GetMapping(path = {"/{id}/buyer"}, name = "users-get-buyer-by-userId")
    @PreAuthorize("hasAnyAuthority('users-get-authorities-by-userId', 'all')")
    public HttpReponse getBuyerByUserId(HttpServletRequest request, @PathVariable String id) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        Optional<Buyer> optBuyer = buyersRepostory.findById(id);

        if (!optBuyer.isPresent()) {
            LogUtil.info(logprefix, location, "buyer not found", "");
            response.setErrorStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        LogUtil.info(logprefix, location, "buyer found", "");

        response.setSuccessStatus(HttpStatus.OK);
        response.setData(optBuyer.get());
        return response;
    }

    @PutMapping(path = {"/{id}/buyer"}, name = "users-put-buyer-by-userId")
    @PreAuthorize("hasAnyAuthority('users-put-buyer-by-userId', 'all')")
    public HttpReponse putBuyerByUserId(HttpServletRequest request, @PathVariable String id, @RequestBody Buyer reqBody) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        Optional<Buyer> optBuyer = buyersRepostory.findById(id);

        if (!optBuyer.isPresent()) {
            LogUtil.info(logprefix, location, "buyer not found", "");
            response.setErrorStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        LogUtil.info(logprefix, location, "buyer found", "");
        Buyer buyer = optBuyer.get();
        List<String> errors = new ArrayList<>();

        List<Buyer> buyers = buyersRepostory.findAll();

        for (Buyer existingBuyer : buyers) {
            if (!buyer.equals(existingBuyer)) {
                if (existingBuyer.getMobileNumber().equals(reqBody.getMobileNumber())) {
                    LogUtil.info(logprefix, location, "mobilenumber already exists", "");
                    response.setErrorStatus(HttpStatus.CONFLICT);
                    errors.add("mobileNumber already exists");
                    response.setData(errors);
                    return response;
                }
            }

        }
        reqBody.setUserId(null);

        buyer.updateBuyer(reqBody);

        LogUtil.info(logprefix, location, "user created", "");
        response.setSuccessStatus(HttpStatus.CREATED);
        response.setData(buyersRepostory.save(buyer));
        return response;
    }

    @PostMapping(path = {"/{id}/buyer"}, name = "users-post-buyer-by-userId")
    @PreAuthorize("hasAnyAuthority('users-post-buyer-by-userId', 'all')")
    public HttpReponse postBuyerByUserId(HttpServletRequest request, @PathVariable String id, @Valid @RequestBody Buyer reqBody) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        Optional<User> optUser = usersRepository.findById(id);

        if (!optUser.isPresent()) {
            LogUtil.info(logprefix, location, "user not found", "");
            response.setErrorStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        LogUtil.info(logprefix, location, "user found", "");

        List<String> errors = new ArrayList<>();
        List<Buyer> buyers = buyersRepostory.findAll();

        for (Buyer existingBuyer : buyers) {
            if (existingBuyer.getMobileNumber().equals(reqBody.getMobileNumber())) {
                LogUtil.info(logprefix, location, "mobilenumber already exists", "");
                response.setErrorStatus(HttpStatus.CONFLICT);
                errors.add("mobileNumber already exists");
                response.setData(errors);
                return response;
            }
        }

        reqBody.setUserId(id);

        LogUtil.info(logprefix, location, "buyer created", "");
        response.setSuccessStatus(HttpStatus.CREATED);
        response.setData(buyersRepostory.save(reqBody));
        return response;
    }

    @GetMapping(path = {"/{id}/seller"}, name = "users-get-seller-by-userId")
    @PreAuthorize("hasAnyAuthority('users-get-authorities-by-userId', 'all')")
    public HttpReponse getSellerByUserId(HttpServletRequest request, @PathVariable String id) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        Optional<Seller> optSeller = sellersRepostory.findById(id);

        if (!optSeller.isPresent()) {
            LogUtil.info(logprefix, location, "seller not found", "");
            response.setErrorStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        LogUtil.info(logprefix, location, "seller found", "");

        response.setSuccessStatus(HttpStatus.OK);
        response.setData(optSeller.get());
        return response;
    }

    @PutMapping(path = {"/{id}/seller"}, name = "users-put-seller-by-userId")
    @PreAuthorize("hasAnyAuthority('users-put-seller-by-userId', 'all')")
    public HttpReponse putSellerByUserId(HttpServletRequest request, @PathVariable String id, @RequestBody Seller reqBody) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        Optional<Seller> optSeller = sellersRepostory.findById(id);

        if (!optSeller.isPresent()) {
            LogUtil.info(logprefix, location, "seller not found", "");
            response.setErrorStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        LogUtil.info(logprefix, location, "seller found", "");
        Seller seller = optSeller.get();
        List<String> errors = new ArrayList<>();

        List<Seller> sellers = sellersRepostory.findAll();

        for (Seller existingSeller : sellers) {
            if (!seller.equals(existingSeller)) {
                if (existingSeller.getMobileNumber().equals(reqBody.getMobileNumber())) {
                    LogUtil.info(logprefix, location, "mobilenumber already exists", "");
                    response.setErrorStatus(HttpStatus.CONFLICT);
                    errors.add("mobileNumber already exists");
                    response.setData(errors);
                    return response;
                }
            }

        }
        reqBody.setUserId(null);

        seller.updateSeller(reqBody);

        LogUtil.info(logprefix, location, "user created", "");
        response.setSuccessStatus(HttpStatus.CREATED);
        response.setData(sellersRepostory.save(seller));
        return response;
    }

    @PostMapping(path = {"/{id}/seller"}, name = "users-post-seller-by-userId")
    @PreAuthorize("hasAnyAuthority('users-post-seller-by-userId', 'all')")
    public HttpReponse postSellerByUserId(HttpServletRequest request, @PathVariable String id, @Valid @RequestBody Seller reqBody) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        Optional<User> optUser = usersRepository.findById(id);

        if (!optUser.isPresent()) {
            LogUtil.info(logprefix, location, "user not found", "");
            response.setErrorStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        LogUtil.info(logprefix, location, "user found", "");

        List<String> errors = new ArrayList<>();
        List<Seller> sellers = sellersRepostory.findAll();

        for (Seller existingSeller : sellers) {
            if (existingSeller.getMobileNumber().equals(reqBody.getMobileNumber())) {
                LogUtil.info(logprefix, location, "mobilenumber already exists", "");
                response.setErrorStatus(HttpStatus.CONFLICT);
                errors.add("mobileNumber already exists");
                response.setData(errors);
                return response;
            }
        }

        reqBody.setUserId(id);

        LogUtil.info(logprefix, location, "seller created", "");
        response.setSuccessStatus(HttpStatus.CREATED);
        response.setData(sellersRepostory.save(reqBody));
        return response;
    }

    //authentication
    @PostMapping(path = "/authenticate", name = "users-authenticate")
    public HttpReponse authenticateUser(@Valid @RequestBody UserAuthenticationBody body, HttpServletRequest request) throws Exception {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        try {
            LogUtil.info(logprefix, location, "", "");
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword())
            );
            LogUtil.info(logprefix, location, "", "");
        } catch (BadCredentialsException e) {
            LogUtil.warn(logprefix, location, "error validating user", "");
            response.setErrorStatus(HttpStatus.UNAUTHORIZED, "Bad Craedentiails");
            return response;
        }

        LogUtil.info(logprefix, location, "user authenticated", "");

        User user = usersRepository.findByUsername(body.getUsername());

        Session session = new Session();
        session.setRemoteAddress(request.getRemoteAddr());
        session.setUserId(user.getId());
        session.setCreated(DateTimeUtil.currentTimestamp());
        session.setUpdated(DateTimeUtil.currentTimestamp());
        session.setExpiry(DateTimeUtil.expiryTimestamp(expiry));
        session.setStatus("ACTIVE");
        session = sessionsRepository.save(session);
        LogUtil.info(logprefix, location, "session created with id: " + session.getId(), "");

        session.setUserId(null);
        session.setUpdated(null);
        session.setStatus(null);
        session.setRemoteAddress(null);

        LogUtil.info(logprefix, location, "generated token", "");

        response.setSuccessStatus(HttpStatus.ACCEPTED);
        response.setData(session);
        return response;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public HttpReponse handleExceptionBadRequestException(HttpServletRequest request, MethodArgumentNotValidException e) {
        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        LogUtil.warn(logprefix, location, "Validation failed", "");
        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());
        HttpReponse response = new HttpReponse(request.getRequestURI());
        response.setErrorStatus(HttpStatus.BAD_REQUEST);
        response.setData(errors);
        return response;
    }
}
