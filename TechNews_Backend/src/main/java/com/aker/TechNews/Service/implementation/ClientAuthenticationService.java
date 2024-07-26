package com.aker.TechNews.Service.implementation;

import com.aker.TechNews.filter.ApiKeyAuth;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientAuthenticationService {

    private final String apiKey = System.getenv("API_KEY_APP");

    public Optional<Authentication> validateApiKey(HttpServletRequest requestApiKey) {
        String headerKey = requestApiKey.getHeader("API-Key");
        boolean isPresent = (headerKey != null && headerKey.equals(apiKey)) ? true : false;
        if(headerKey == null || !isPresent){
            ResponseEntity.status(HttpStatus.UNAUTHORIZED);
            return Optional.empty();
        }

        return Optional.of(new ApiKeyAuth(headerKey, AuthorityUtils.NO_AUTHORITIES));
    }
}
