package com.mosbmap.usersservice.filters;

import com.mosbmap.usersservice.models.MySQLUserDetails;
import com.mosbmap.usersservice.models.daos.Session;
import com.mosbmap.usersservice.repositories.SessionsRepository;
import com.mosbmap.usersservice.services.MySQLUserDetailsService;
import com.mosbmap.usersservice.utils.JwtUtil;
import com.mosbmap.usersservice.utils.LogUtil;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author Sarosh
 */
@Component
public class SessionRequestFilter extends OncePerRequestFilter {

    @Autowired
    private MySQLUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    SessionsRepository sessionsRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        LogUtil.info("", "", "----------" + request.getRequestURI() + "----------", "");

        String logprefix = request.getRequestURI() + " ";
        String location = "doFilterInternal ";
        final String requestTokenHeader = request.getHeader("Authorization");

        String sessionId = null;
        String jwtToken = null;
        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                sessionId = jwtUtil.getSessionIdToken(jwtToken);
                LogUtil.info(logprefix, location, "Found Token", "");
            } catch (IllegalArgumentException e) {
                LogUtil.warn(logprefix, location, "Unable to get JWT Token", "");
            } catch (Exception e) {
                LogUtil.warn(logprefix, location, "JWT Token error", "");
            }
        } else {
            LogUtil.warn(logprefix, location, "JWT Token does not begin with Bearer String", "");
        }

        if (sessionId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            Optional<Session> optSession = sessionsRepository.findById(sessionId);
            if (optSession.isPresent()) {
                Session session = optSession.get();

                // if token is valid configure Spring Security to manually set authentication
                if (jwtUtil.validateToken(jwtToken, session)) {

                    MySQLUserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(session.getUser().getUsername());
                    LogUtil.info(logprefix, location, "JWT Token valid", "");
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                }
            }

        }
        chain.doFilter(request, response);
    }
}
