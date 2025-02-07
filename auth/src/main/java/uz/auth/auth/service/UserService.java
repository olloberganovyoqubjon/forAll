package uz.auth.auth.service;


import uz.auth.auth.entity.Role;
import uz.auth.auth.entity.Users;
import uz.auth.auth.exceptions.ResourceNotFoundException;
import uz.auth.auth.payload.ApiResult;
import uz.auth.auth.payload.EditUserDto;
import uz.auth.auth.payload.LoginDto;
import uz.auth.auth.payload.RegisterDto;
import uz.auth.auth.repository.RoleRepository;
import uz.auth.auth.repository.UserRepository;
import uz.auth.auth.security.PasswordValidator;
import uz.auth.auth.security.tokenGenerator.JwtProvider;
import uz.auth.auth.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service   // Аннотация для обозначения класса как сервисного компонента Spring
public class UserService {

    private final UserRepository repository; // Внедрение зависимости для работы с репозиторием пользователей

    private final PasswordEncoder passwordEncoder; // Внедрение зависимости для кодирования паролей

    private final PasswordValidator passwordValidator; // Внедрение зависимости для проверки паролей

    private final RoleRepository roleRepository; // Внедрение зависимости для работы с репозиторием ролей

    private final JwtProvider jwtProvider; // Внедрение зависимости для работы с JWT-токенами
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, PasswordValidator passwordValidator, RoleRepository roleRepository, JwtProvider jwtProvider, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.passwordValidator = passwordValidator;
        this.roleRepository = roleRepository;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }


    public ApiResult register(RegisterDto dto) {

        // Проверка совпадения паролей
        if (!dto.getPassword().equals(dto.getPrePassword()))
            return new ApiResult("Parollar mos emas!", true);

        // Проверка существования пользователя с таким же именем пользователя
        if (repository.findByUsername(dto.getUsername()).isPresent())
            return new ApiResult("Bunday login mavjud!", true);

        // Проверка валидности пароля


        // Проверка существования роли с указанным идентификатором
        Optional<Role> optionalRole = roleRepository.findById(dto.getRoleId());
        if (optionalRole.isEmpty())
            return new ApiResult("Bunday rol mavjud emas!", true);


        if (AppConstants.ADMIN.equals(optionalRole.get().getName())) {
            if (passwordValidator.isValid(dto.getPassword()))
                return new ApiResult("Parol yetarli darajada murakkab emas!", false);
        }


        // Создание нового пользователя
        Users user = new Users();
        user.setFirstName(dto.getFirstName()); // Установка фамилии пользователя
        user.setLastName(dto.getLastName()); // Установка фамилии пользователя
        user.setUsername(dto.getUsername()); // Установка имени пользователя
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // Кодирование и установка пароля
        user.setRole(roleRepository.findById(dto.getRoleId()).orElseThrow(() -> new ResourceNotFoundException(
                "role", "id", dto.getRoleId()
        ))); // Установка роли пользователя

        user.setEnabled(dto.isActive()); // Установка активности пользователя


        // Сохранение пользователя в репозитории
        Users saved = repository.save(user);
        saved.setPassword(null);

        // Возвращение успешного результата регистрации
        return new ApiResult("Muvoffaqiyatli ro'yxatdan o'tkazildi!", true, saved);
    }


    public ApiResult edit(Long id, RegisterDto dto) {
        // Поиск пользователя по идентификатору
        Optional<Users> optionalUser = repository.findById(id);
        if (optionalUser.isEmpty())
            return new ApiResult("Foydalanuvchi ma'lumotlari to'liq emas!", true);

        // Проверка существования пользователя с таким же именем пользователя, но другим идентификатором
        if (repository.doesUserExistByUsernameAndIdNotEqual(dto.getUsername(), id))
            return new ApiResult("Bunday login mavjud!", false);

        // Проверка существования роли с указанным идентификатором

        Optional<Role> optionalRole = roleRepository.findById(dto.getRoleId());
        if (optionalRole.isEmpty())
            return new ApiResult("Bunday toifa mavjud emas!", true);


        if (AppConstants.ADMIN.equals(optionalRole.get().getName())) {
            if (passwordValidator.isValid(dto.getPassword()))
                return new ApiResult("Parol yetarli darajada murakkab emas!", false);
        }


        // Создание нового пользователя
        Users user = optionalUser.get();
        user.setFirstName(dto.getFirstName()); // Установка фамилии пользователя
        user.setLastName(dto.getLastName());
        user.setUsername(dto.getUsername()); // Установка имени пользователя
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // Кодирование и установка пароля
        user.setRole(roleRepository.findById(dto.getRoleId()).orElseThrow(() -> new ResourceNotFoundException(
                "role", "id", dto.getRoleId()
        ))); // Установка роли пользователя

        user.setEnabled(dto.isActive()); // Установка активности пользователя


        // Сохранение пользователя в репозитории
        repository.save(user);

        // Возвращение успешного результата редактирования
        return new ApiResult("Muvoffaqiyatli tahrirlandi!", true, repository.findAllWhereDeleteIsNotTrue());
    }


    public ApiResult editUserForUser(Users userinSystem, EditUserDto dto) {

        try {
            // Проверка текущего пароля пользователя
            if (!passwordEncoder.matches(dto.getPasswordNow(), userinSystem.getPassword())) {
                return new ApiResult("Foydalanuvchi paroli to'g'ri emas!", false);
            }
            // Проверка совпадения нового пароля и его подтверждения
            if (!dto.getPassword().equals(dto.getPrePassword()))
                return new ApiResult("Parollar bir biriga mos emas!", false);


            // Проверка валидности нового пароля
            if (userinSystem.getRole().getName().equals(AppConstants.ADMIN)) {
                if (passwordValidator.isValid(dto.getPassword()))
                    return new ApiResult("Parol mustahkam emas!", false);
            }

            // Установка нового пароля
            userinSystem.setPassword(passwordEncoder.encode(dto.getPassword()));

            // Сохранение изменений в базе данных
            Users user = repository.save(userinSystem);

            // Генерация нового токена
            String token = jwtProvider.generatorToken(user.getUsername(), user.getRole());
            // Генерация нового токена обновления

            // Возвращение успешного результата с новыми токенами
            return new ApiResult("Token", true, token);
        } catch (Exception e) {
            // Возвращение результата в случае ошибки
            return new ApiResult("Token", false, null, null);
        }
    }


    /**
     * Получает список всех пользователей, у которых не установлен флаг удаления.
     *
     * @return Список объектов Users.
     */
    public List<Users> getAll() {
        return repository.findAllWhereDeleteIsNotTrue();
    }

    /**
     * Получает пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return Объект Users, если найден, или null, если не найден.
     */
    public Users getOne(Long id) {
        return repository.findById(id).orElse(null);
    }


    /**
     * Удаляет пользователя по его идентификатору, устанавливая флаги отключения и удаления.
     *
     * @param id Идентификатор пользователя.
     * @return Результат операции в виде ApiResult.
     */
    public ApiResult delete(Long id) {

        try {
            // Проверка существования пользователя по идентификатору
            boolean existsById = repository.existsById(id);
            if (!existsById)
                return new ApiResult("Bunday foydalanuvchi mavjud emas", false);

            // Получение пользователя по идентификатору
            Users users = repository.findById(id).get();
            // Установка флага отключения пользователя
            users.setEnabled(false);
            // Установка флага неактивности пользователя
            users.setDelete(true);

            // Сохранение изменений в базе данных
            repository.save(users);

            // Возвращение успешного результата удаления
            return new ApiResult("O'chirildi", true, repository.findAllWhereDeleteIsNotTrue());
        } catch (Exception e) {
            // Возвращение результата в случае ошибки
            return new ApiResult("Xatolik", false);
        }
    }


    /**
     * Активирует или деактивирует пользователя по его идентификатору.
     *
     * @param id     Идентификатор пользователя.
     * @param active Флаг активности (true для активации, false для деактивации).
     * @return Результат операции в виде ApiResult.
     */
    public ApiResult active(Long id, boolean active) {

        try {
            // Проверка существования пользователя по идентификатору
            boolean existsById = repository.existsById(id);
            if (!existsById)
                return new ApiResult("Bunday foydalanuvchi mavjud emas", false);

            // Получение пользователя по идентификатору
            Users users = repository.findById(id).get();
            // Установка флага активности пользователя
            users.setEnabled(active);

            // Сохранение изменений в базе данных
            repository.save(users);

            // Возвращение успешного результата изменения активности
            return new ApiResult("O'zgartirildi", true, repository.findAll());

        } catch (Exception e) {
            // Возвращение результата в случае ошибки
            return new ApiResult("Xatolik", false);
        }
    }


    /**
     * Выполняет выход пользователя, устанавливая его подразделение как офлайн.
     *
     * @param user Объект пользователя, выполняющего выход.
     */
    public void logout(Users user) {
        // Поиск подразделения пользователя по его идентификатору
        if (!user.getRole().getName().equals(AppConstants.ADMIN)) {
            // Установка статуса подразделения как офлайн
            user.setOnline(false);
            // Сохранение изменений в базе данных
            repository.save(user);
        }
    }

    public ApiResult login(LoginDto dto) {

        try {

            Optional<Users> optional = repository.findByUsername(dto.getUsername());
            if (optional.isEmpty() || !passwordEncoder.matches(dto.getPassword(), optional.get().getPassword())) {
                return new ApiResult("Invalid username or password!", false);
            }

            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    dto.getUsername(),
                    dto.getPassword()
            ));

            Users user = (Users) authenticate.getPrincipal();
            if(!user.isEnabled() ){
                return new ApiResult("Tizimga kirish taqiqlangan!", false);
            }

            String token = JwtProvider.generatorToken(user.getUsername(), user.getRole());
            String refreshToken = JwtProvider.generateRefreshToken(user.getUsername(), user.getRole());

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            user.setEntryTime(timestamp);
            user.setOnline(true);
            if (user.getRole().getName().equals(AppConstants.ADMIN)) {
                repository.save(user);
                return new ApiResult("Token", true, token, refreshToken, AppConstants.ADMIN,user.getUsername());
            }
            repository.save(user);
            return new ApiResult("Token", true, token, refreshToken, AppConstants.USER, user.getUsername());
        } catch (BadCredentialsException e) {
            return new ApiResult("Login yoki parol xato!", false);
        }
    }

}
