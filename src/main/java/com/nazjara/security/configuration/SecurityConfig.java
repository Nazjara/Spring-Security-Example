package com.nazjara.security.configuration;

import com.nazjara.security.RestHeaderAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
        var filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    // needed for use with Spring Data JPA SPeL
    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(restHeaderAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .csrf().ignoringAntMatchers("/h2-console/**", "/api/**");

        http
                .authorizeRequests()
//                .antMatchers(HttpMethod.DELETE, "/api/v1/beer/**").hasRole("ADMIN")
//                .antMatchers(HttpMethod.GET, "/brewery/breweries/**").hasAnyRole("ADMIN", "CUSTOMER")
//                .antMatchers(HttpMethod.GET, "/api/v1/breweries").hasAnyRole("ADMIN", "CUSTOMER")
//                .antMatchers("/beers/find", "/beers*").hasAnyRole("ADMIN", "USER", "CUSTOMER")
//                .antMatchers(HttpMethod.GET, "/api/v1/beer/**").hasAnyRole("ADMIN", "USER", "CUSTOMER")
//                .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").hasAnyRole("ADMIN", "USER", "CUSTOMER")
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/", "/webjars/**", "/resources/**").permitAll()
                .anyRequest()
                .authenticated().and()
                .formLogin().and()
                .httpBasic();

        //h2 console config
        http
                .headers()
                .frameOptions()
                .sameOrigin();
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
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user1")
//                .password("{bcrypt}$2a$10$l3W1NilbcK3HOCJjTtuwX.OTY1v100wxixIyQRd.h.E8/XrypccFe")
//                .roles("ADMIN").and()
//                .withUser("user2")
//                .password("{sha256}8e3f0d456b53d2c976dc90d4993fc50bcbd6c628805a07960837ed8cfc8b2d9f1c6ad6cbc806146e")
//                .roles("USER");
//    }


}
