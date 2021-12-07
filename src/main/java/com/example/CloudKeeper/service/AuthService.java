package com.example.CloudKeeper.service;

import com.example.CloudKeeper.DTO.AuthorizationRequestDTO;
import com.example.CloudKeeper.exception.CloudException;
import com.example.CloudKeeper.exception.ErrorInputData;
import com.example.CloudKeeper.repository.CloudRepository;
import com.example.CloudKeeper.security.JwtUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final CloudRepository cloudRepository;
    private static final Logger LOGGER = Logger.getLogger(AuthService.class);
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthService(CloudRepository cloudRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.cloudRepository = cloudRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public String login(AuthorizationRequestDTO authorization) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authorization.getLogin(), authorization.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtils.generateJwtToken(userPrincipal);
            cloudRepository.login(jwt, userPrincipal);
            LOGGER.info(String.format("User %s authorization success. JWT: %s", userPrincipal.getUsername(), jwt));
            return jwt;
        } catch (BadCredentialsException e) {
            throw new ErrorInputData("Bad credentials");
        }
    }

    public void logout(String authToken) {
        UserDetails userPrincipal = cloudRepository.logout(authToken).orElseThrow(() -> new CloudException("Error while remove JWT"));
        LOGGER.info(String.format("User %s logout success", userPrincipal.getUsername()));
    }
}
