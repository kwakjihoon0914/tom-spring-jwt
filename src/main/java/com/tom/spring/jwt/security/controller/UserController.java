package com.tom.spring.jwt.security.controller;

import com.tom.spring.jwt.security.config.AuthorityType;
import com.tom.spring.jwt.security.entity.User;
import com.tom.spring.jwt.security.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

   private final UserServiceImpl userService;

   @GetMapping("/users/me")
   public ResponseEntity<User> getActualUser() {
      return ResponseEntity.ok(userService.getCurrentUser());
   }

   @GetMapping("/users/authority")
   public ResponseEntity<Collection<User>> getUsersByAuthority() {
      return ResponseEntity.ok(userService.getUsersByRole(AuthorityType.ROLE_USER));
   }

}
