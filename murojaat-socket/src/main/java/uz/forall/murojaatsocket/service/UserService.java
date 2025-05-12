package uz.forall.murojaatsocket.service;

import org.springframework.stereotype.Service;
import uz.forall.murojaatsocket.model.User;
import uz.forall.murojaatsocket.payload.ApiResult;
import uz.forall.murojaatsocket.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ApiResult getUsers(Long organizationId) {
        try {
            List<User> userList = userRepository.findByOrganization_Id(organizationId);
            return new ApiResult("success", true, userList);
        } catch (Exception e) {
            return new ApiResult("error", false, e.getMessage());
        }
    }

    public ApiResult getUser(Long userId) {
        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            return optionalUser.map(user -> new ApiResult("success", true, user))
                    .orElseGet(() -> new ApiResult("Foydalanuvchi topilmadi", false, userId));
        }catch (Exception e) {
            return new ApiResult("error", false, e.getMessage());
        }
    }
}
