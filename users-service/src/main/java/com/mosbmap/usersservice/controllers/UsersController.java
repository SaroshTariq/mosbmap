package com.mosbmap.usersservice.controllers;

import com.mosbmap.usersservice.models.HttpReponse;
import com.mosbmap.usersservice.models.daos.Session;
import com.mosbmap.usersservice.models.daos.User;
import com.mosbmap.usersservice.repositories.RolesRepository;
import com.mosbmap.usersservice.repositories.SessionsRepository;
import com.mosbmap.usersservice.repositories.UsersRepository;
import com.mosbmap.usersservice.utils.DateTimeUtil;
import com.mosbmap.usersservice.utils.JwtUtil;
import com.mosbmap.usersservice.utils.LogUtil;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    UserDetailsService userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    SessionsRepository sessionsRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @GetMapping(path = {"/"}, name = "users-get")
    @PreAuthorize("hasAnyAuthority('users-get', 'all')")
    public HttpReponse getUsers(HttpServletRequest request) {
        String logprefix = request.getRequestURI() + " ";
        String location = "getUsers ";
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        response.setStatus(HttpStatus.OK);
        response.setData(usersRepository.findAll());

        return response;
    }

    @GetMapping(path = {"/{id}"}, name = "users-get-by-id")
    @PreAuthorize("hasAnyAuthority('users-get-by-id', 'all')")
    public HttpReponse getUserById(HttpServletRequest request, @PathVariable String id) {

        String logprefix = request.getRequestURI() + " ";
        String location = "getUserById ";
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        Optional<User> optUser = usersRepository.findById(id);

        if (!optUser.isPresent()) {
            LogUtil.info(logprefix, location, "user not found", "");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        LogUtil.info(logprefix, location, "user found", "");
        response.setStatus(HttpStatus.OK);
        response.setData(optUser.get());
        return response;
    }

    @PutMapping(path = {"/{id}"}, name = "users-put-by-id")
    @PreAuthorize("hasAnyAuthority('users-put-by-id', 'all')")
    public HttpReponse putUser(HttpServletRequest request, @PathVariable String id, @Valid @RequestBody User user) {
        String logprefix = request.getRequestURI() + " ";
        String location = "getUsers ";
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        Optional<User> optUser = usersRepository.findById(id);

        if (!optUser.isPresent()) {
            LogUtil.info(logprefix, location, "user not found", "");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        User updateUser = optUser.get();

        List<User> users = usersRepository.findAll();

        for (User existingUser : users) {
            if (!updateUser.equals(existingUser)) {
                if (existingUser.getUsername().equals(user.getUsername())) {
                    LogUtil.info(logprefix, location, "username already exists", "");
                    response.setStatus(HttpStatus.CONFLICT, "Username already exists");
                    return response;
                }
                if (existingUser.getEmail().equals(user.getEmail())) {
                    LogUtil.info(logprefix, location, "email already exists", "");
                    response.setStatus(HttpStatus.CONFLICT, "Email already exists");
                    return response;
                }
            }

        }

        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        user.setCreated(DateTimeUtil.currentTimestamp());
        user.setUpdated(DateTimeUtil.currentTimestamp());
        user = usersRepository.save(user);

        LogUtil.info(logprefix, location, "user created", "");
        response.setStatus(HttpStatus.CREATED);
        response.setData(user);
        return response;
    }

    @PostMapping(name = "users-post")
    @PreAuthorize("hasAnyAuthority('users-post', 'all')")
    public HttpReponse postUser(HttpServletRequest request, @Valid @RequestBody User user) {
        String logprefix = request.getRequestURI() + " ";
        String location = "getUsers ";
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        List<User> users = usersRepository.findAll();

        for (User existingUser : users) {
            if (existingUser.getUsername().equals(user.getUsername())) {
                LogUtil.info(logprefix, location, "username already exists", "");
                response.setStatus(HttpStatus.CONFLICT, "Username already exists");
                return response;
            }
            if (existingUser.getEmail().equals(user.getEmail())) {
                LogUtil.info(logprefix, location, "email already exists", "");
                response.setStatus(HttpStatus.CONFLICT, "Email already exists");
                return response;
            }
        }

        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        user.setCreated(DateTimeUtil.currentTimestamp());
        user.setUpdated(DateTimeUtil.currentTimestamp());
        user = usersRepository.save(user);

        LogUtil.info(logprefix, location, "user created", "");
        response.setStatus(HttpStatus.CREATED);
        response.setData(user);
        return response;
    }

    @PostMapping(path = "/authenticate", name = "users-authenticate")
    public HttpReponse authenticateUser(@Valid @RequestBody UserAuthenticationBody body, HttpServletRequest request) {
        String logprefix = request.getRequestURI() + " ";
        String location = "authenticateUser ";
        HttpReponse response = new HttpReponse(request.getRequestURI());

        LogUtil.info(logprefix, location, "", "");

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword())
            );

        } catch (BadCredentialsException e) {
            LogUtil.warn(logprefix, location, "BadCredentials", "");
            response.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
            response.setStatus(HttpStatus.UNAUTHORIZED, e.getMessage());
            return response;
        }

        LogUtil.info(logprefix, location, "User authenticated", "");
        //final UserDetails userDetails = userDetailsService.loadUserByUsername(body.getUsername());

        User user = usersRepository.findByUsername(body.getUsername());

        Session session = new Session();
        session.setRemoteAddress(request.getRemoteAddr());
        session.setUserId(user.getId());
        session.setCreated(DateTimeUtil.currentTimestamp());
        session.setUpdated(DateTimeUtil.currentTimestamp());
        session.setStatus("ACTIVE");
        session = sessionsRepository.save(session);

        LogUtil.info(logprefix, location, "Created session with id: " + session.getId(), "");
        String jwt = jwtUtil.generateToken(session);

        class Authentication {

            String token;

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }
        }
        Authentication auth = new Authentication();

        LogUtil.info(logprefix, location, "Generated token", "");
        auth.setToken(jwt);

        response.setStatus(HttpStatus.ACCEPTED);
        response.setData(auth);
        return response;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public HttpReponse handleException(HttpServletRequest request, MethodArgumentNotValidException e) {
        LogUtil.warn(request.getRequestURI() + " ", "handleException ", "Validation failed", "");
        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());
        HttpReponse response = new HttpReponse(request.getRequestURI());
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setData(errors);
        return response;
    }
}

//Bodies of request
@ToString
@Getter
@Setter
class UserAuthenticationBody {

    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "password is required")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
