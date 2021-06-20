package com.tom.spring.jwt.security.service.impl;

import com.tom.spring.jwt.security.config.AuthorityType;
import com.tom.spring.jwt.security.entity.User;
import com.tom.spring.jwt.security.authentication.AuthenticationFacade;
import com.tom.spring.jwt.security.repository.UserRepository;
import com.tom.spring.jwt.security.service.UserService;
import com.tom.spring.jwt.security.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

   private final UserRepository userRepository;
   private final AuthenticationFacade authenticationFacade;

   @Transactional(readOnly = true)
   public User getUserWithAuthorities() {
      return SecurityUtils.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername).get();
   }

   @Transactional(readOnly = true)
   public User getCurrentUser() {
      return userRepository.findById(
                  authenticationFacade.getSignedUser().get().getId())
              .get();
   }

   @Override
   public Collection<User> getUsersByRole(AuthorityType authorityType) {

      return userRepository.findByAuthoritiesName(authorityType.name());
   }

}
