package ru.internaft.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.internaft.backend.entity.User;
import ru.internaft.backend.repository.UsersRepository;

@RestController
public class RequestsController {
    private final UsersRepository repository;

    public RequestsController(UsersRepository repository) {
        this.repository = repository;
    }

    // TODO: Replace tmp mappings below to normal API

    @GetMapping("/h")
    public String get() {
        return "hello";
    }

    @GetMapping("/g")
    public User getUser() {
        return repository.findAll().stream().findAny().orElse(null);
    }

    @GetMapping("/p")
    public Iterable<User> getUsers() {
        return repository.findAll();
    }

    @GetMapping("/a")
    public String addUser() {
        repository.save(new User());
        repository.save(new User());
        repository.save(new User());
        return "correct";
    }
}
