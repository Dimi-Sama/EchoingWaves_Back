package com.shiki.echo_waves.services;

import com.shiki.echo_waves.models.UsersCollection;
import com.shiki.echo_waves.repositories.UsersCollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsersCollectionService {
    @Autowired
    private UsersCollectionRepository usersCollectionRepository;
    
    public List<UsersCollection> getAllCollections() {
        return usersCollectionRepository.findAll();
    }
    
    public UsersCollection getCollectionById(Integer id) {
        return usersCollectionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Collection non trouvée"));
    }
    
    public UsersCollection getCollectionByUserId(Integer userId) {
        return usersCollectionRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Collection non trouvée pour cet utilisateur"));
    }
    
    public UsersCollection createCollection(UsersCollection collection) {
        return usersCollectionRepository.save(collection);
    }
    
    public void deleteCollection(Integer id) {
        usersCollectionRepository.deleteById(id);
    }
} 