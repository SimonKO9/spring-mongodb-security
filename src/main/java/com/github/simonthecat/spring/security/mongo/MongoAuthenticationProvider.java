package com.github.simonthecat.spring.security.mongo;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.List;

public class MongoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private MongoUserService mongoUserService;

    private PasswordEncoder encoder;

    public MongoAuthenticationProvider(MongoUserService mongoUserService, PasswordEncoder encoder) {
        this.mongoUserService = mongoUserService;
        this.encoder = encoder;
    }


    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        String password = (String) authentication.getCredentials();

        if (!StringUtils.hasText(password)) {
            logger.warn("Username {}: no password provided");
            throw new BadCredentialsException("Please enter password");
        }

        UserAccount account = mongoUserService.getUserByUsername(username);
        if (account == null) {
            logger.warn("Account not found");
            throw new BadCredentialsException("User not found");
        }

        if (!encoder.matches(password, account.getHash())) {
            logger.warn("Invalid password");
            throw new BadCredentialsException("Invalid password");
        }

        List<GrantedAuthority> auths = AuthorityUtils.createAuthorityList(account.getRoles().toArray(new String[account.getRoles().size()]));
        return new User(username, password, auths);
    }
}
