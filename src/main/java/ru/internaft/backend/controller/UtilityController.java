package ru.internaft.backend.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.internaft.backend.Roles;
import ru.internaft.backend.entity.UserData;
import ru.internaft.backend.repository.LoginsDataRepository;
import ru.internaft.backend.repository.UsersDataRepository;
import ru.internaft.backend.service.UserDetailsServiceImpl;

//Класс для вспомогательных методов
@RestController
public class UtilityController {

    private final UsersDataRepository usersDataRepository;
    private final LoginsDataRepository loginsDataRepository;
    private final UserDetailsServiceImpl userDetailsService;

    public UtilityController(UsersDataRepository usersDataRepository, LoginsDataRepository loginsDataRepository, UserDetailsServiceImpl userDetailsService) {
        this.usersDataRepository = usersDataRepository;
        this.loginsDataRepository = loginsDataRepository;
        this.userDetailsService = userDetailsService;
    }

    //Возвращает Id текущего пользователя


/*    @GetMapping("/h")
    public boolean h() {
        return s();
    }*/

/*    public boolean s(){
        UserData u = new UserData();
        u.setAboutText("SDS");
        u.setEmail("sdsd");
        u.setFullName("Dsds");
        u.setRole();
        usersDataRepository.saveAndFlush(u);
        return true;
    }*/
}
