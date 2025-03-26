package com.shiki.echo_waves.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.shiki.echo_waves.repositories.SoundRepository;
import com.shiki.echo_waves.repositories.UserRepository;
import com.shiki.echo_waves.repositories.UsersCollectionRepository;
import com.shiki.echo_waves.repositories.TirageRepository;
import com.shiki.echo_waves.repositories.SoundProbabilityRepository;
import com.shiki.echo_waves.models.Sound;
import com.shiki.echo_waves.models.SoundRarety;
import com.shiki.echo_waves.models.User;
import com.shiki.echo_waves.models.UserRoles;
import com.shiki.echo_waves.models.UserCollectionSound;
import com.shiki.echo_waves.models.UsersCollection;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserCollectionSoundServiceTest {
    @Autowired
    private UserCollectionSoundService userCollectionSoundService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SoundRepository soundRepository;
    
    @Autowired
    private UsersCollectionRepository usersCollectionRepository;
    
    @Autowired
    private TirageRepository tirageRepository;
    
    @Autowired
    private SoundProbabilityRepository soundProbabilityRepository;
    
    @BeforeEach
    void setUp() {
        userCollectionSoundService.getAllCollectionSounds()
            .forEach(cs -> userCollectionSoundService.removeFromCollection(cs.getId()));
        tirageRepository.deleteAllInBatch();
        soundProbabilityRepository.deleteAllInBatch();
        soundRepository.deleteAllInBatch();
        usersCollectionRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        
        assertEquals(0, userCollectionSoundService.getAllCollectionSounds().size());
        assertEquals(0, soundProbabilityRepository.count());
        assertEquals(0, tirageRepository.count());
        assertEquals(0, usersCollectionRepository.count());
        assertEquals(0, soundRepository.count());
        assertEquals(0, userRepository.count());
    }
    
    @Test
    void testAddSoundToCollection() {
        User user = createTestUser();
        Sound sound = createTestSound();
        UsersCollection collection = user.getUsersCollection();
        
        UserCollectionSound collectionSound = new UserCollectionSound();
        collectionSound.setCollection(collection);
        collectionSound.setSound(sound);
        
        UserCollectionSound saved = userCollectionSoundService.addSoundToCollection(collectionSound);
        
        assertNotNull(saved.getId());
        assertEquals(1, saved.getQuantity());
    }
    
    @Test
    void testAddDuplicateSoundToCollection() {
        User user = createTestUser();
        Sound sound = createTestSound();
        UsersCollection collection = user.getUsersCollection();
        
        UserCollectionSound collectionSound = new UserCollectionSound();
        collectionSound.setCollection(collection);
        collectionSound.setSound(sound);
        
        userCollectionSoundService.addSoundToCollection(collectionSound);
        UserCollectionSound saved = userCollectionSoundService.addSoundToCollection(collectionSound);
        
        assertEquals(2, saved.getQuantity());
    }
    
    private User createTestUser() {
        User user = new User();
        user.setPseudo("TestUser");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setSecret_bool(false);
        user.setRole(UserRoles.USER);
        
        User savedUser = userRepository.saveAndFlush(user);
        
        UsersCollection collection = new UsersCollection();
        collection.setUser(savedUser);
        UsersCollection savedCollection = usersCollectionRepository.saveAndFlush(collection);
        
        savedUser.setUsersCollection(savedCollection);
        return userRepository.saveAndFlush(savedUser);
    }
    
    private Sound createTestSound() {
        Sound sound = new Sound();
        sound.setNom("Test Sound");
        sound.setType("Test Type");
        sound.setRarete(SoundRarety.COMMON);
        sound.setLink("http://example.com/sound.mp3");
        return soundRepository.saveAndFlush(sound);
    }
} 