package uz.auth.auth.security.config;

import uz.auth.auth.security.PasswordValidator;
import uz.auth.auth.security.filter.CustomAuthenticationFilter;
import uz.auth.auth.security.filter.JwtFilter;
import uz.auth.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration // Аннотация, указывающая, что это конфигурационный класс
@EnableWebSecurity // Аннотация для включения веб-безопасности
@EnableMethodSecurity // Аннотация для включения глобальной безопасности методов с пред- и пост-авторизацией
public class SecuryConfig implements WebMvcConfigurer {

    /**
     *
     * Класс SecurityConfig является конфигурационным классом для настройки безопасности веб-приложения. Он реализует интерфейс WebMvcConfigurer.
     * Этот класс управляет аутентификацией, авторизацией, фильтрацией запросов и шифрованием паролей, а также настройками CORS.
     *
     */

    @Lazy
    @Autowired // Автосвязывание зависимостей
    AuthService authService; // Сервис аутентификации
    @Autowired // Автосвязывание зависимостей
    JwtFilter jwtFilter; // Фильтр JWT

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // Метод конфигурации безопасности HTTP запросов
        http
                .csrf(AbstractHttpConfigurer::disable) // Отключение защиты CSRF
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/sign-in", "/api/user/login","/api/user/logout","/api/auth/refreshToken**").permitAll()
                        .requestMatchers("/api/status").permitAll()
                        .requestMatchers("/api/getdiskspace").permitAll()
                        .requestMatchers("/api/getservertime").permitAll()
                        .requestMatchers("/api/auth/validate").permitAll()
                        .anyRequest().authenticated() // Любой другой запрос должен быть аутентифицирован
                )
                .httpBasic(withDefaults()); // Использование базовой HTTP-аутентификации

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Adding JWT filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(new CustomAuthenticationFilter(authenticationManager(http)), UsernamePasswordAuthenticationFilter.class); // Adding custom authentication filter
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Установка политики создания сессий - без сохранения состояния
        http.cors(withDefaults()); // Включение CORS

        return http.build(); // Построение конфигурации безопасности
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception { // Метод для создания бина AuthenticationManager
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(authService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    /**
     * Метод для создания бина PasswordEncoder для шифрования паролей
     * @return PasswordEncoder
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Возврат шифровщика паролей BCrypt
    }

    /**
     * Метод для создания бина PasswordValidator для проверки надежности паролей
     */
    @Bean
    PasswordValidator passwordValidator(){
        return new PasswordValidator(); // Возврат валидатора паролей
    }

    /**
     * Метод для конфигурации CORS (переноса ресурсов между доменами)
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Разрешение CORS для всех путей
                .allowedOrigins("*") // Разрешение запросов с любых источников
                .allowedHeaders("*") // Разрешение любых заголовков
                .allowedMethods("*"); // Разрешение любых методов (GET, POST, PUT и т.д.)
    }
}
