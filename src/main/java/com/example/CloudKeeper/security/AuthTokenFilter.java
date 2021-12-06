package com.example.CloudKeeper.security;

import com.example.CloudKeeper.service.CloudService;
import com.example.CloudKeeper.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userDetailsService;

    private static final Logger LOGGER = Logger.getLogger(CloudService.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String login = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userPrincipal = userDetailsService.loadUserByUsername(login);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userPrincipal, null, userPrincipal.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException exc) {
            LOGGER.error(String.format("Error ExpiredJwtException: JWT has been expired"));
        } catch (Exception exc) {
            LOGGER.error(String.format("Error %s while getting JWT: %s", exc.getClass().getName(), exc.getLocalizedMessage()));
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("auth-token");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        } else {
            LOGGER.error("Error Invalid auth-token format: should start with 'Bearer '");
        }
        return null;
    }
}
