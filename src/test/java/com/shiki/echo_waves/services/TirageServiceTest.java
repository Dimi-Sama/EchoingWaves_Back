package com.shiki.echo_waves.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;

import com.shiki.echo_waves.repositories.BoxRepository;
import com.shiki.echo_waves.repositories.TirageRepository;
import com.shiki.echo_waves.repositories.UserRepository;
import com.shiki.echo_waves.repositories.BoxContentRepository;
import com.shiki.echo_waves.repositories.SoundProbabilityRepository;
import com.shiki.echo_waves.models.Box;
import com.shiki.echo_waves.models.Tirage;
import com.shiki.echo_waves.models.User;
import com.shiki.echo_waves.models.UserRoles;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class TirageServiceTest {
    @Autowired
    private TirageService tirageService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BoxRepository boxRepository;
    
    @Autowired
    private TirageRepository tirageRepository;
    
    @Autowired
    private BoxContentRepository boxContentRepository;
    
    @Autowired
    private SoundProbabilityRepository soundProbabilityRepository;
    
    @BeforeEach
    void setUp() {
        tirageRepository.deleteAllInBatch();
        soundProbabilityRepository.deleteAllInBatch();
        boxRepository.deleteAllInBatch();
        boxContentRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }
    
    @Test
    void testCreateTirage() {
        User user = new User();
        user.setPseudo("TestUser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(UserRoles.USER);
        user = userRepository.save(user);
        
        Box box = new Box();
        box.setNom("Test Box");
        box.setHidden(false);
        box = boxRepository.save(box);
        
        Tirage tirage = new Tirage();
        tirage.setUser(user);
        tirage.setBox(box);
        
        Tirage savedTirage = tirageService.createTirage(tirage);
        
        assertNotNull(savedTirage.getId());
        assertEquals(user.getId(), savedTirage.getUser().getId());
        assertEquals(box.getId(), savedTirage.getBox().getId());
    }
    
    @Test
    void testGetTiragesByUser() {
        User user = createTestUser();
        Box box = createTestBox();
        
        createTestTirage(user, box, "Tirage 1");
        createTestTirage(user, box, "Tirage 2");
        
        List<Tirage> tirages = tirageService.getTiragesByUser(user.getId());
        
        assertEquals(2, tirages.size());
    }
    
    private User createTestUser() {
        User user = new User();
        user.setPseudo("TestUser");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setSecret_bool(false);
        user.setRole(UserRoles.USER);
        return userRepository.save(user);
    }
    
    private Box createTestBox() {
        Box box = new Box();
        box.setNom("Test Box");
        box.setHidden(false);
        return boxRepository.save(box);
    }
    
    private Tirage createTestTirage(User user, Box box, String info) {
        Tirage tirage = new Tirage();
        tirage.setUser(user);
        tirage.setBox(box);
        tirage.setInfo_tirage(info);
        tirage.setDate_tirage(new Date());
        return tirageRepository.save(tirage);
    }
} 