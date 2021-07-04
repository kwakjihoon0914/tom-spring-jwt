package com.tom.spring.jwt.common;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD }) //Scope
@Retention(RetentionPolicy.RUNTIME) //life cycle
@Documented //Can be documented
@Inherited //
public @interface ResultUnite {
}