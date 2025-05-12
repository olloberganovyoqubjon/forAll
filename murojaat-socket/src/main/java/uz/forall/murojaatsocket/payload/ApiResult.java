package uz.forall.murojaatsocket.payload;

import lombok.Data;

import java.io.File;
import java.util.Arrays;

@Data
public class ApiResult {


    private String message;

    private boolean success;

    private String token;
    private String refreshToken;

    private Object object;
    private Object object2;
    private Object object3;
    private Object user;
    private Long userId;
    private Object role;

    private byte [] bytes;

    private File file;


    public ApiResult() {}

    public ApiResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public ApiResult(String message, boolean success, Object object) {
        this.message = message;
        this.success = success;
        this.object = object;
    }

    public ApiResult(String message, boolean success, String token, String refreshToken, Object role) {
        this.message = message;
        this.success = success;
        this.token = token;
        this.refreshToken = refreshToken;
        this.role = role;
    }

    public ApiResult(String message, boolean success, String token, String refreshToken, Object role, Object user) {
        this.message = message;
        this.success = success;
        this.token = token;
        this.refreshToken = refreshToken;
        this.role = role;
        this.user = user;
    }

    public ApiResult(String message, boolean success, String token, Object role) {
        this.message = message;
        this.success = success;
        this.token = token;
        this.role = role;
    }

    public ApiResult(String message, boolean success, Object object, Object object2, Object object3) {
        this.message = message;
        this.success = success;
        this.object = object;
        this.object2 = object2;
        this.object3 = object3;
    }

    public ApiResult(String message, boolean success, Object object, Object object2, Long userId) {
        this.message = message;
        this.success = success;
        this.object = object;
        this.object2 = object2;
        this.userId = userId;
    }

    public ApiResult(String message, boolean success, String token, Object object, Object object2, Long userId) {
        this.message = message;
        this.success = success;
        this.token = token;
        this.object = object;
        this.object2 = object2;
        this.userId = userId;
    }

    public ApiResult(String message, boolean success, String token, Object object, Object object2, Long userId, Object user) {
        this.message = message;
        this.success = success;
        this.token = token;
        this.object = object;
        this.object2 = object2;
        this.userId = userId;
        this.user = user;
    }

    public ApiResult(String message, boolean success, String token, Object object, Object object2, Long userId, Object user, String refreshToken) {
        this.message = message;
        this.success = success;
        this.token = token;
        this.object = object;
        this.object2 = object2;
        this.userId = userId;
        this.user = user;
        this.refreshToken = refreshToken;
    }

    public ApiResult(byte [] bytes, boolean success) {
        this.bytes = bytes;
        this.success = success;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "message='" + message + '\'' +
                ", success=" + success +
                ", token='" + token + '\'' +
                ", object=" + object +
                ", object2=" + object2 +
                ", user=" + user +
                ", userId=" + userId +
                ", bytes=" + Arrays.toString(bytes) +
                ", file=" + file +
                '}';
    }
}
