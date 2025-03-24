package com.shiki.echo_waves.services;

import com.shiki.echo_waves.models.UserCollectionSound;
import com.shiki.echo_waves.repositories.UserCollectionSoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserCollectionSoundService {
    @Autowired
    private UserCollectionSoundRepository userCollectionSoundRepository;
    
    public List<UserCollectionSound> getAllCollectionSounds() {
        return userCollectionSoundRepository.findAll();
    }
    
    public List<UserCollectionSound> getCollectionSoundsByCollectionId(Integer collectionId) {
        return userCollectionSoundRepository.findByCollectionId(collectionId);
    }
    
    public UserCollectionSound addSoundToCollection(UserCollectionSound collectionSound) {
        return userCollectionSoundRepository.findByCollectionIdAndSoundId(
            collectionSound.getCollection().getId(),
            collectionSound.getSound().getId()
        ).map(existing -> {
            existing.setQuantity(existing.getQuantity() + 1);
            return userCollectionSoundRepository.save(existing);
        }).orElseGet(() -> {
            collectionSound.setQuantity(1);
            return userCollectionSoundRepository.save(collectionSound);
        });
    }
    
    public void removeFromCollection(Integer id) {
        userCollectionSoundRepository.deleteById(id);
    }
} 