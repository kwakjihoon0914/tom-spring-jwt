package com.tom.spring.jwt.security.config;


import com.tom.spring.jwt.config.JWTConfigurerAdapter;
import com.tom.spring.jwt.config.JwtAccessDeniedHandler;
import com.tom.spring.jwt.config.JwtAuthenticationEntryPoint;
import com.tom.spring.jwt.config.TokenProvider;
import com.tom.spring.jwt.security.authentication.CustomAuthenticationProvider;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint authenticationErrorHandler;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final UserDetailsService userDetailService;

    private JWTConfigurerAdapter securityConfigurerAdapter() {
        return new JWTConfigurerAdapter(tokenProvider);
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        CustomAuthenticationProvider authenticationProvider =
                        new CustomAuthenticationProvider(passwordEncoder(),userDetailService);
        authenticationProvider.setHideUserNotFoundExceptions(false) ;
        //authenticationProvider.setMessageSource(new CustomSecurityMessageSource());

        return authenticationProvider;
    }

    //@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")

                // allow anonymous resource requests
                .antMatchers(
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/h2-console/**"
                );
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {



        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling()
                .authenticationEntryPoint(authenticationErrorHandler)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // enable h2-console
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // no session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()

                .antMatchers("/authenticate").permitAll()
                .antMatchers("/test/**").permitAll()
                .antMatchers("/api/manager/**").hasAuthority("ROLE_ADMIN")
                .antMatchers("/api/**").hasAuthority("ROLE_USER")

                .anyRequest().authenticated()


                .and()
                .apply(securityConfigurerAdapter());


    }




}
