package com.shiki.echo_waves.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.shiki.echo_waves.repositories.SoundRepository;
import com.shiki.echo_waves.repositories.UserCollectionSoundRepository;
import com.shiki.echo_waves.repositories.SoundProbabilityRepository;
import com.shiki.echo_waves.repositories.TirageRepository;
import com.shiki.echo_waves.repositories.UsersCollectionRepository;
import com.shiki.echo_waves.repositories.UserRepository;
import com.shiki.echo_waves.models.Sound;
import com.shiki.echo_waves.models.SoundRarety;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SoundServiceTest {
    @Autowired
    private SoundService soundService;
    
    @Autowired
    private SoundRepository soundRepository;

    @Autowired
    private UserCollectionSoundRepository userCollectionSoundRepository;

    @Autowired
    private SoundProbabilityRepository soundProbabilityRepository;

    @Autowired
    private TirageRepository tirageRepository;

    @Autowired
    private UsersCollectionRepository usersCollectionRepository;

    @Autowired
    private UserRepository userRepository;
    @BeforeEach
    void setUp() {
        // Nettoyer dans le bon ordre pour éviter les violations de contraintes
        userCollectionSoundRepository.deleteAllInBatch();
        soundProbabilityRepository.deleteAllInBatch();
        tirageRepository.deleteAllInBatch();
        usersCollectionRepository.deleteAllInBatch();
        // Supprimer les Sound après avoir supprimé SoundProbability
        soundRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        
        // Vérifier que tout est bien nettoyé
        assertEquals(0, userCollectionSoundRepository.count());
        assertEquals(0, soundProbabilityRepository.count());
        assertEquals(0, tirageRepository.count());
        assertEquals(0, usersCollectionRepository.count());
        assertEquals(0, soundRepository.count());
        assertEquals(0, userRepository.count());
    }
    
    @Test
    void testCreateSound() {
        Sound sound = new Sound();
        sound.setNom("Bury The Light");
        sound.setType("OST");
        sound.setRarete(SoundRarety.LEGENDARY);
        sound.setLink("https://storage.com/bury-the-light.mp3");
        
        Sound savedSound = soundService.createSound(sound);
        
        assertNotNull(savedSound.getId());
        assertEquals("Bury The Light", savedSound.getNom());
        assertEquals(SoundRarety.LEGENDARY, savedSound.getRarete());
    }
    
    @Test
    void testGetAllSounds() {
        // Créer plusieurs sons
        createTestSound("Bury The Light", SoundRarety.LEGENDARY);
        createTestSound("Devil Trigger", SoundRarety.EPIC);
        
        List<Sound> sounds = soundService.getAllSounds();
        
        assertEquals(2, sounds.size());
    }
    
    @Test
    void testGetSoundById() {
        Sound sound = createTestSound("Rules of Nature", SoundRarety.EPIC);
        
        Sound foundSound = soundService.getSoundById(sound.getId());
        
        assertEquals("Rules of Nature", foundSound.getNom());
    }
    
    @Test
    void testUpdateSound() {
        Sound sound = createTestSound("Devil Trigger", SoundRarety.EPIC);
        
        sound.setNom("Devil Trigger (Remix)");
        Sound updatedSound = soundService.updateSound(sound.getId(), sound);
        
        assertEquals("Devil Trigger (Remix)", updatedSound.getNom());
    }
    
    @Test
    void testDeleteSound() {
        Sound sound = createTestSound("Test Sound", SoundRarety.COMMON);
        
        soundService.deleteSound(sound.getId());
        
        assertThrows(RuntimeException.class, () -> soundService.getSoundById(sound.getId()));
    }
    
    private Sound createTestSound(String nom, SoundRarety rarete) {
        Sound sound = new Sound();
        sound.setNom(nom);
        sound.setType("OST");
        sound.setRarete(rarete);
        sound.setLink("https://storage.com/" + nom.toLowerCase().replace(" ", "-") + ".mp3");
        return soundRepository.save(sound);
    }
} 