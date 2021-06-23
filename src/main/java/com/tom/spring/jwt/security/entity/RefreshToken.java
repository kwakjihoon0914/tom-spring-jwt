package com.tom.spring.jwt.security.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter @Setter
@Table(name = "REFRESH_TOKEN")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REFRESH_TOKEN_SEQ")
    @SequenceGenerator(name = "REFRESH_TOKEN_SEQ", sequenceName = "REFRESH_TOKEN_SEQ", allocationSize = 1)
    private long id;

    @OneToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "id")
    private User user;


    @Column(name = "TOKEN",nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;


}

