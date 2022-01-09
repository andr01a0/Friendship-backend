package com.instantcoffee.tech.authentication;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtUserDetailsService jwtUserDetailsService;
  private final JwtRequestFilter jwtRequestFilter;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    authenticationManagerBuilder.userDetailsService(this.jwtUserDetailsService).passwordEncoder(this.passwordEncoder);
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.cors();

    httpSecurity.csrf().disable().authorizeRequests()
        .antMatchers("/authenticate/signup", "/authenticate/login", "/authenticate/check-token")
        .permitAll().anyRequest().authenticated().and().exceptionHandling()
        .authenticationEntryPoint(this.jwtAuthenticationEntryPoint).and().sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    httpSecurity.addFilterBefore(this.jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
