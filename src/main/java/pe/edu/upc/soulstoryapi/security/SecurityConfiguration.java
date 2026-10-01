package pe.edu.upc.soulstoryapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    private static final String[] LISTA_PERMIT_ALL = {

            // Frontend estático
            "/",
            "/index.html",
            "/styles.css",
            "/app.js",

            // Swagger / OpenAPI
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/api-docs",
            "/api-docs/**",

            // Endpoints públicos de usuarios
            "/api/usuarios/login",
            "/api/usuarios/registro",
            "/api/usuarios/recuperar-contrasena",
            "/api/usuarios/restablecer-contrasena"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /*

    1. Cuales seran los Request que seran evaluados para confirmar si el usuario tiene permisos
        a. AnyRequest -> Aplica al 100% de los Request
        b. RequestMatcher -> Se evalua solo los que coincidan con la ruta especifica
        c. RequestMatcher + HttpMethod -> Se evalua solo los que coincidan con la ruta especifica y con el metodo HTTP solicitado

    2. Cual es la norma/regla de autorizacion que se va aplicar a los Request a evaluar
        a. permitAll()
        b. denyAll()
        c. hasAnyAuthority()
        d. hasAuthority()
        e. hasRole()
        f. hasAnyRole()
        g. SpEL -> Spring Expression Language
        h. authenticated()

     */

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        http.cors(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(
                (authorizationRegistry) -> authorizationRegistry
                        //Inicio de configuracion de autorizaciones
                        .requestMatchers(LISTA_PERMIT_ALL).permitAll()

                        // El valor de Usuario.rol es libre (se registra desde el cliente), por eso aqui
                        // solo se exige estar autenticado. Si luego se fijan los roles, se agregan reglas
                        // por ruta, por ejemplo:
                        // .requestMatchers("/api/cuidadores/**").hasAuthority("CUIDADOR")

                        .anyRequest().authenticated()
                //Fin de configuracion de autorizaciones
        );

        // Sin token valido se responde 401 (por defecto Spring Security respondería 403)
        http.exceptionHandling(
                (exceptions) -> exceptions
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        );

        http.sessionManagement(
                (sessionManager) -> sessionManager
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        return http.build();
    }

}
