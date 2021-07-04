package com.tom.spring.jwt.security.service.impl;

import com.tom.spring.jwt.security.entity.User;
import com.tom.spring.jwt.security.exception.UserNotActivatedException;
import com.tom.spring.jwt.security.authentication.AuthenticatedUser;
import com.tom.spring.jwt.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

/**
 * Authenticate a user from the database.
 */
@Slf4j
@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

   private final UserRepository userRepository;

   @Override
   @Transactional
   public UserDetails loadUserByUsername(final String userName) {

      if (new EmailValidator().isValid(userName, null)) {
         return userRepository.findOneWithAuthoritiesByEmailIgnoreCase(userName)
            .map(user -> createAuthenticatedUser(userName, user))
            .orElseThrow(() -> new UsernameNotFoundException("User with email " + userName + " was not found in the database"));
      }

      String lowercaseLogin = userName.toLowerCase(Locale.ENGLISH);

      return userRepository.findOneWithAuthoritiesByUsername(lowercaseLogin)
         .map(user -> createAuthenticatedUser(lowercaseLogin, user))
         .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));
   }

   private AuthenticatedUser createAuthenticatedUser(String lowercaseLogin, User user) {

      if (!user.isActivated()) {
         throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
      }

      return AuthenticatedUser.of(user);
   }
}
