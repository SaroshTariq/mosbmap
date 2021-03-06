package com.mosbmap.catalogservice.filters;


import com.mosbmap.catalogservice.models.MySQLUserDetails;
import com.mosbmap.catalogservice.models.daos.Session;
import com.mosbmap.catalogservice.repositories.SessionsRepository;
import com.mosbmap.catalogservice.services.MySQLUserDetailsService;
import com.mosbmap.catalogservice.utils.DateTimeUtil;
import com.mosbmap.catalogservice.utils.LogUtil;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

        //LogUtil.info(logprefix, location, "requestTokenHeader: " + requestTokenHeader, "");
        String sessionId = null;

        // Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            sessionId = requestTokenHeader.substring(7);
        } else {
            LogUtil.warn(logprefix, location, "token does not begin with Bearer String", "");
        }

        if (sessionId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //LogUtil.info(logprefix, location, "sessionId: " + sessionId, "");
            Optional<Session> optSession = sessionRepository.findById(sessionId);
            if (optSession.isPresent()) {
                LogUtil.info(logprefix, location, "sessionId valid", "");
                Session session = optSession.get();
                long diff = 0;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date expiryTime = sdf.parse(session.getExpiry());
                    Date currentTime = sdf.parse(DateTimeUtil.currentTimestamp());

                    //LogUtil.info(logprefix, location, "currentTime: " + currentTime.getTime() + " expiryTime: " + expiryTime.getTime(), "");
                    diff = expiryTime.getTime() - currentTime.getTime();
                } catch (Exception e) {
                    LogUtil.warn(logprefix, location, "error calculating time to session expiry", "");
                }
                LogUtil.info(logprefix, location, "time to session expiry: " + diff + "ms", "");
                if (0 < diff) {
                    MySQLUserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(session.getUser().getUsername());

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } else {
                    LogUtil.warn(logprefix, location, "session expired", "");
                }

            } else {
                LogUtil.warn(logprefix, location, "sessionId not valid", "");
            }

        }
        chain.doFilter(request, response);
    }
}
