package com.nazjara.configuration;

import com.nazjara.security.RestHeaderAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
        var filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(restHeaderAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();

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
                .withUser("user1")
                .password("{bcrypt}$2a$10$l3W1NilbcK3HOCJjTtuwX.OTY1v100wxixIyQRd.h.E8/XrypccFe")
                .roles("ADMIN").and()
                .withUser("user2")
                .password("{sha256}8e3f0d456b53d2c976dc90d4993fc50bcbd6c628805a07960837ed8cfc8b2d9f1c6ad6cbc806146e")
                .roles("USER");
    }
}
