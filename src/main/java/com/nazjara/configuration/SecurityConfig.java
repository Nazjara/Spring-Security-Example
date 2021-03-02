package com.nazjara.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/webjars/**", "/resources/**").permitAll()
                .antMatchers("/beers/find", "/beers*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll()
                .anyRequest()
                .authenticated().and()
                .formLogin().and()
                .httpBasic();
    }

    //    Custom in-memory UserDetailsService (1)
//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("username")
//                .password("password")
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin);
//    }


    //    Custom in-memory UserDetailsService (2)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("username")
                .password("{noop}password") //specifying a password encoder
                .roles("ADMIN");
    }
}
