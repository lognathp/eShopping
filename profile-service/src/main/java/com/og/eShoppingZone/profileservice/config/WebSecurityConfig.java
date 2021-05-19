package com.og.eShoppingZone.profileservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.og.eShoppingZone.profileservice.auth.JwtAuthenticationEntryPoint;
import com.og.eShoppingZone.profileservice.auth.JwtRequestFilter;
import com.og.eShoppingZone.profileservice.service.ProfileService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

@Autowired
private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

@Autowired
private JwtRequestFilter jwtRequestFilter;



@Override
protected void configure(HttpSecurity httpSecurity) throws Exception {
// We don't need CSRF for this example
httpSecurity.csrf().disable()
// dont authenticate this particular request
.authorizeRequests().antMatchers("/api/v1/profile/login").permitAll().
// all other requests need to be authenticated
//.antMatchers("/api/v1/profile").hasAnyRole("MERCHANT","CUSTOMER").
anyRequest().authenticated().and().
// make sure we use stateless session; session won't be used to
// store user's state.
exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

// Add a filter to validate the tokens with every request
httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
}
}
