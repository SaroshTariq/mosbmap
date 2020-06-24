package com.mosbmap.usersservice.filters;

import com.mosbmap.usersservice.models.MySQLUserDetails;
import com.mosbmap.usersservice.models.daos.Session;
import com.mosbmap.usersservice.repositories.SessionsRepository;
import com.mosbmap.usersservice.services.MySQLUserDetailsService;
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
    SessionsRepository sessionRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String logprefix = request.getRequestURI() + " ";
        String location = Thread.currentThread().getStackTrace()[1].getMethodName();
        LogUtil.info("", "", "----------" + logprefix + "----------", "");

        final String requestTokenHeader = request.getHeader("Authorization");

        LogUtil.info(logprefix, "", "requestTokenHeader: " + requestTokenHeader, "");
        String sessionId = null;

        // Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            sessionId = requestTokenHeader.substring(7);
        } else {
            LogUtil.warn(logprefix, location, "Token does not begin with Bearer String", "");
        }

        if (sessionId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            LogUtil.info(logprefix, "", "sessionId: " + sessionId, "");
            Optional<Session> optSession = sessionRepository.findById(sessionId);
            if (optSession.isPresent()) {
                Session session = optSession.get();

                MySQLUserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(session.getUser().getUsername());
                LogUtil.info(logprefix, location, "Token valid", "");
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            }else{
                 LogUtil.info(logprefix, "", "sessionId: " + sessionId, "");
            }

        }
        chain.doFilter(request, response);
    }
}
