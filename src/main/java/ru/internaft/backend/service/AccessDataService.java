package ru.internaft.backend.service;

import org.springframework.stereotype.Component;
import ru.internaft.backend.entity.AccessData;
import ru.internaft.backend.repository.AccessDataRepository;

import java.util.Optional;

@Component
public class AccessDataService {
    private final AccessDataRepository accessDataRepository;

    public AccessDataService(AccessDataRepository accessDataRepository) {
        this.accessDataRepository = accessDataRepository;
    }

    public AccessData findByEmail(String string){
        return accessDataRepository.findByEmail(string);
    }
    public Optional<AccessData> findById(Integer id){
        return accessDataRepository.findById(id);
    }
}
