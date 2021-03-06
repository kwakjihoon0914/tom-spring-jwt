package com.tom.spring.jwt.security.entity;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@DynamicUpdate
@Table(name = "TOKEN")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TOKEN_SEQ")
    @SequenceGenerator(name = "TOKEN_SEQ", sequenceName = "TOKEN_SEQ", allocationSize = 1)
    private long id;

    @OneToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "id")
    private User user;


    @Column(name = "ACCESS_TOKEN",nullable = false, unique = true)
    private String accessToken;


    @Column(name = "REFRESH_TOKEN",nullable = false, unique = true)
    private String refreshToken;

    @Column(name = "REFRESH_TOKEN_EXPIRY_DATE",nullable = false)
    private LocalDateTime refreshTokenExpiryDate;


    public void updateToken(String accessToken,String refreshToken,LocalDateTime refreshTokenExpiryDate){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiryDate = refreshTokenExpiryDate;
    }



}

