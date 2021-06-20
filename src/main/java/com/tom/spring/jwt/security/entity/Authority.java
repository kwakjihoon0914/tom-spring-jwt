package com.tom.spring.jwt.security.entity;



import com.tom.spring.jwt.security.config.AuthorityType;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Getter
@Table(name = "AUTHORITY")
public class Authority {

    @Id
    @Column(name = "NAME", length = 50)
    @NotNull
    private String name;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authority authority = (Authority) o;
        return name == authority.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Authority{" +
                "name=" + name +
                '}';
    }
}