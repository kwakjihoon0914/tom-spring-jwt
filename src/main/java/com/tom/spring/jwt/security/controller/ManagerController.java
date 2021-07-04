package com.tom.spring.jwt.security.controller;

import com.tom.spring.jwt.security.entity.User;
import com.tom.spring.jwt.security.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ManagerController {

   private final UserServiceImpl userService;

   @GetMapping("/manager/user")
   public ResponseEntity<User> getActualUser() {

      return ResponseEntity.ok(userService.getUserWithAuthorities());
   }
}
