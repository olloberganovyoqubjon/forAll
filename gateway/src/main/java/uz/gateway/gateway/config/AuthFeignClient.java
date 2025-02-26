package uz.gateway.gateway.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@FeignClient(name = "auth", url = "http://localhost:8081")
//@FeignClient(name = "auth", configuration = FeignConfig.class)
public interface AuthFeignClient {
    @GetMapping("/validate")
    Map<String, Long> validateToken(@RequestParam("token") String token);
}

