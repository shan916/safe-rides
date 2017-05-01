package edu.csus.asi.saferides.security;

import edu.csus.asi.saferides.security.service.JwtUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Configuration settings for authentication and securing the api routes
 * From: https://github.com/szerhusenBC/jwt-spring-security-demo
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // flag to check if the local in-memory database's console is enabled
    @Value("${spring.h2.console.enabled}")
    private boolean h2ConsoleEnabled;

    // dependency injection
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;
    @Autowired
    private JwtUserDetailsServiceImpl userDetailsService;

    /**
     * Dependency injection and specify which userdetails service and password encoder to use
     *
     * @param authenticationManagerBuilder SecurityBuilder used to create an AuthenticationManager
     * @throws Exception if an error occurs when configuring authentication
     */
    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    /**
     * Get PasswordEncoder bean
     * Sets ArgonPasswordEncoder as the PasswordEncoder
     *
     * @return ArgonPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new ArgonPasswordEncoder();
    }

    /**
     * Get AuthenticationTokenFilter bean
     *
     * @return JWTAuthenticationTokenFilter instance from bean
     * @throws Exception if an error occurs with creating a new JwtAuthenticationTokenFilter
     */
    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

    /**
     * Configure the api security
     *
     * @param httpSecurity It allows configuring web based security for specific http request
     * @throws Exception if an error occurs with the configuration of the security
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        // allow access to the h2 console if enabled
        if (h2ConsoleEnabled) {
            httpSecurity.authorizeRequests().antMatchers("/h2-console/**").permitAll();
            httpSecurity.headers().frameOptions().disable();
        }

        httpSecurity
                // we don't need CSRF because our token is invulnerable (we are not passing teh JWT as a cookie but rather in the header)
                .csrf().disable()
                // JwtAuthenticationEntryPoint
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // set up children for requests
                .authorizeRequests()
                // allow POST to /users/auth
                .antMatchers(HttpMethod.POST, "/users/auth", "/users/authrider").permitAll()
                // allow OPTIONS to /rides
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // allow access to the swagger api documentation
                .antMatchers(HttpMethod.GET, "/swagger-ui.html", "/webjars/springfox-swagger-ui/**", "/swagger-resources/**", "/v2/api-docs/**").permitAll()
                // all other request need a JWT
                .anyRequest().authenticated();

        // Custom JWT based security filter
        httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().cacheControl();
    }
}