package ru.internaft.backend.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import ru.internaft.backend.entity.UserData;
import ru.internaft.backend.service.UserDataService;

//Класс для вспомогательных методов
@RestController
public class UtilityController {

    private final UserDataService userDataService;

    public UtilityController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    public Integer getCurrentUserId() {
        String loginCurrentUser = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return userDataService.findByEmail(loginCurrentUser).getId();
    }

    public UserData getCurrentUser(){
        String loginCurrentUser = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return userDataService.findByEmail(loginCurrentUser);
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
