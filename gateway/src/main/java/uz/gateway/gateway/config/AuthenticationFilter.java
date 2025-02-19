package uz.gateway.gateway.config;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import reactor.core.publisher.Mono;
//
//@Component
//public class AuthenticationFilter implements GatewayFilter {
//
//    @Autowired
//    private WebClient.Builder webClientBuilder;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//
//        // Token borligini tekshirish
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//
//        String token = authHeader.substring(7);  // "Bearer " ni olib tashlash
//
//        // Auth servisi orqali tokenni tekshirish
//        return webClientBuilder.build()
//                .get()
//                .uri("http://api/auth/validate?token=" + token)
//                .retrieve()
//                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
//                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                    return exchange.getResponse().setComplete().then(Mono.error(new RuntimeException("Auth failed")));
//                })
//                .toBodilessEntity()
//                .flatMap(response -> chain.filter(exchange));  // Agar token to'g'ri bo'lsa, so'rovni davom ettirish
//
//    }
//}


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private WebClient.Builder webClientBuilder;

    private static final List<String> EXCLUDED_PATHS = List.of(
            "/parsing/presidentNews"
    );

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String path = exchange.getRequest().getURI().getPath();
            // Agar yo'l authentication tekshiruvidan istisno bo'lsa, filterni ishlatmaymiz
            if (EXCLUDED_PATHS.contains(path)) {
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            // Token borligini tekshirish
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7);  // "Bearer " ni olib tashlash

            // Auth servisi orqali tokenni tekshirish
            return webClientBuilder.build()
                    .get()
                    .uri("lb://auth/api/auth/validate?token=" + token)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete().then(Mono.error(new RuntimeException("Auth failed")));
                    })
                    .toBodilessEntity()
                    .flatMap(response -> chain.filter(exchange));  // Agar token to'g'ri bo'lsa, so'rovni davom ettirish
        };
    }

    // Konfiguratsiya uchun ichki klass (hozircha bo'sh, keyin kerak bo'lsa sozlash mumkin)
    public static class Config {
        // Qo'shimcha konfiguratsiya parametrlari bu yerga qo'shiladi
    }
}
