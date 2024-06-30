package com.aker.TechNews.Service.implementation;

import com.aker.TechNews.Repository.UserRepository;
import com.aker.TechNews.entity.User;
import com.aker.TechNews.filter.ApiKeyAuth;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientAuthenticationService {

    @Autowired
    private UserRepository userRepository;

    public Optional<Authentication> validateApiKey(HttpServletRequest requestApiKey) {
        String headerKey = requestApiKey.getHeader("API-Key");
        boolean isPresent = userRepository.findByApiKey(headerKey).isPresent() ? true : false;
        if(headerKey == null || !isPresent){
            ResponseEntity.status(HttpStatus.UNAUTHORIZED);
            return Optional.empty();
        }

        return Optional.of(new ApiKeyAuth(headerKey, AuthorityUtils.NO_AUTHORITIES));
    }
}
