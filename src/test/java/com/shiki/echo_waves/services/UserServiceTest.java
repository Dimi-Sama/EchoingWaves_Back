package com.shiki.echo_waves.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.shiki.echo_waves.repositories.UserRepository;
import com.shiki.echo_waves.repositories.UsersCollectionRepository;
import com.shiki.echo_waves.repositories.UserCollectionSoundRepository;
import com.shiki.echo_waves.repositories.SoundProbabilityRepository;
import com.shiki.echo_waves.repositories.TirageRepository;
import com.shiki.echo_waves.models.User;
import com.shiki.echo_waves.models.UserRoles;
import com.shiki.echo_waves.models.UsersCollection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceTest {
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UsersCollectionRepository usersCollectionRepository;
    
    @Autowired
    private UserCollectionSoundRepository userCollectionSoundRepository;
    
    @Autowired
    private SoundProbabilityRepository soundProbabilityRepository;
    
    @Autowired
    private TirageRepository tirageRepository;
    
    @BeforeEach
    void setUp() {
        // Nettoyer dans le bon ordre
        userCollectionSoundRepository.deleteAllInBatch();
        soundProbabilityRepository.deleteAllInBatch();
        tirageRepository.deleteAllInBatch();
        usersCollectionRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        
        // Vérifier le nettoyage
        assertEquals(0, userCollectionSoundRepository.count());
        assertEquals(0, soundProbabilityRepository.count());
        assertEquals(0, tirageRepository.count());
        assertEquals(0, usersCollectionRepository.count());
        assertEquals(0, userRepository.count());
    }
    
    @Test
    void testCreateUser() {
        // Utiliser l'objet User sans collection (le service doit la créer)
        User user = createUserObject("DemonHunter", "vergil@powerseeker.com");
        
        User savedUser = userService.createUser(user);
        
        assertNotNull(savedUser.getId());
        assertEquals("DemonHunter", savedUser.getPseudo());
        assertTrue(passwordEncoder.matches("needmorepowER123", savedUser.getPassword()));
        assertNotNull(savedUser.getUsersCollection());
    }
    
    @Test
    void testCreateUserWithDuplicateEmail() {
        User user1 = createUserObject("DemonHunter1", "vergil@powerseeker.com");
        userService.createUser(user1);
        
        User user2 = createUserObject("DemonHunter2", "vergil@powerseeker.com");
        
        assertThrows(RuntimeException.class, () -> userService.createUser(user2));
    }
    
    @Test
    void testCreateUserWithDuplicatePseudo() {
        User user1 = createUserObject("DemonHunter", "vergil1@powerseeker.com");
        userService.createUser(user1);
        
        User user2 = createUserObject("DemonHunter", "vergil2@powerseeker.com");
        
        assertThrows(RuntimeException.class, () -> userService.createUser(user2));
    }
    
    @Test
    void testGetUserById() {
        User user = userService.createUser(createUserObject("DemonHunter", "vergil@powerseeker.com"));
        
        User foundUser = userService.getUserById(user.getId());
        
        assertEquals("DemonHunter", foundUser.getPseudo());
    }

    private User createUserObject(String pseudo, String email) {
        User user = new User();
        user.setPseudo(pseudo);
        user.setEmail(email);
        user.setPassword("needmorepowER123");
        user.setSecret_bool(false);
        user.setRole(UserRoles.USER);
        return user;
    }
    
    private User createAndSaveUser(String pseudo, String email) {
        User user = createUserObject(pseudo, email);
        // Encoder manuellement le mot de passe pour les tests
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.saveAndFlush(user);
        
        // Créer et sauvegarder manuellement une collection pour cet utilisateur
        UsersCollection collection = new UsersCollection();
        collection.setUser(savedUser);
        UsersCollection savedCollection = usersCollectionRepository.saveAndFlush(collection);
        
        // Mettre à jour l'utilisateur
        savedUser.setUsersCollection(savedCollection);
        return userRepository.saveAndFlush(savedUser);
    }
} 