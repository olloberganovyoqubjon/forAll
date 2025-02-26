package uz.forall.appstore.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import uz.forall.appstore.annotation.CurrentUserId;
import uz.forall.appstore.payload.UserIdResponse;

import java.util.Map;

@Component
public class CurrentUserIdResolver implements HandlerMethodArgumentResolver {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CurrentUserId.class) != null
                && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  org.springframework.web.context.request.NativeWebRequest webRequest,
                                  org.springframework.web.bind.support.WebDataBinderFactory binderFactory) {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("Token required!");
        }

        // Tokenni Auth service-ga jo'natib userId ni olish
        String authServiceUrl = "http://localhost:8081/validate?token=" + token.substring(7);
        ResponseEntity<UserIdResponse> response = restTemplate.getForEntity(authServiceUrl, UserIdResponse.class);


        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().getUserId();
        }

        throw new RuntimeException("Invalid Token!");
    }
}