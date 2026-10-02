package com.event.EventManagement.config;

import com.event.EventManagement.security.CustomUserDetailService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;


@Configuration
public class WebSecurityConfig {

    @Autowired
    private CustomUserDetailService userDetailService;

    JwtAuthenticationConverter jwtAuthConverter(){
        JwtGrantedAuthoritiesConverter jwtAuth = new JwtGrantedAuthoritiesConverter();
        jwtAuth.setAuthoritiesClaimName("roles");
        jwtAuth.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtConvertor = new JwtAuthenticationConverter();
        jwtConvertor.setJwtGrantedAuthoritiesConverter(jwtAuth);
        return jwtConvertor;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers("/login", "/register").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(customizer -> customizer.jwtAuthenticationConverter(jwtAuthConverter())))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager( AuthenticationConfiguration config) throws  Exception{
        return config.getAuthenticationManager();
    }


    @Bean
    public AuthenticationProvider authenticationProvider(){

      DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }

    @Bean
    public KeyPair keyPair (){
        try{
            var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                        keyPairGenerator.initialize(2048);
                      return   keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public RSAKey rsaKey(){

        KeyPair keyPair = keyPair();
        return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey)keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();

    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(){
        JWKSet jwkSet = new JWKSet(rsaKey());
        return ((jwkSelector,context) -> jwkSelector.select(jwkSet));
        /*
        above lambda exp that we are returning is shortcut for the below method
        you can either do the below way or the above way
                return new JWKSource<>() {
                    @Override
                    public List<JWK> get(JWKSelector jwkSelector, SecurityContext context) throws KeySourceException {
                        return jwkSelector.select(jwkSet);
                    }
                };
        */
    }

    @Bean
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource){
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    JwtDecoder jwtDecoder() throws JOSEException {
        return NimbusJwtDecoder.withPublicKey(rsaKey().toRSAPublicKey()).build();
    }

}
