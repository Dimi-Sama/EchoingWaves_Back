package com.shiki.echo_waves.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.shiki.echo_waves.repositories.SoundRepository;
import com.shiki.echo_waves.repositories.UserRepository;
import com.shiki.echo_waves.repositories.UsersCollectionRepository;
import com.shiki.echo_waves.repositories.SoundProbabilityRepository;
import com.shiki.echo_waves.repositories.UserCollectionSoundRepository;
import com.shiki.echo_waves.repositories.TirageRepository;
import com.shiki.echo_waves.dto.BoxCreationDTO;
import com.shiki.echo_waves.dto.SoundProbabilityDTO;
import com.shiki.echo_waves.dto.TirageResultDTO;
import com.shiki.echo_waves.models.*;
import com.shiki.echo_waves.models.UsersCollection;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class BoxServiceTest {
    @Autowired
    private BoxService boxService;
    
    @Autowired
    private SoundRepository soundRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UsersCollectionRepository usersCollectionRepository;
    
    @Autowired
    private SoundProbabilityRepository soundProbabilityRepository;
    
    @Autowired
    private UserCollectionSoundRepository userCollectionSoundRepository;
    
    @Autowired
    private TirageRepository tirageRepository;
    
    @BeforeEach
    void setUp() {
        // Nettoyer dans le bon ordre pour éviter les violations de contraintes de clé étrangère
        userCollectionSoundRepository.deleteAllInBatch();
        soundProbabilityRepository.deleteAllInBatch();
        tirageRepository.deleteAllInBatch();
        usersCollectionRepository.deleteAllInBatch();
        // Supprimer les Sound avant les SoundProbability
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
    void testCreateBoxWithContent() {
        // Créer des sons pour la box
        Sound sound1 = createTestSound("Bury The Light", SoundRarety.LEGENDARY);
        Sound sound2 = createTestSound("Devil Trigger", SoundRarety.EPIC);
        
        BoxCreationDTO boxDTO = new BoxCreationDTO();
        boxDTO.setNom("Test Box");
        boxDTO.setHidden(false);
        
        List<SoundProbabilityDTO> probabilities = new ArrayList<>();
        probabilities.add(createProbabilityDTO(sound1.getId(), 5.0));
        probabilities.add(createProbabilityDTO(sound2.getId(), 15.0));
        boxDTO.setSoundProbabilities(probabilities);
        
        Box createdBox = boxService.createBoxWithContent(boxDTO);
        
        assertNotNull(createdBox.getId());
        assertEquals("Test Box", createdBox.getNom());
        assertFalse(createdBox.getHidden());
    }
    
    @Test
    void testEffectuerTirage() {
        // Créer utilisateur
        User user = createTestUser();
        
        // Créer box avec sons
        Sound sound1 = createTestSound("Bury The Light", SoundRarety.LEGENDARY);
        Sound sound2 = createTestSound("Devil Trigger", SoundRarety.EPIC);
        
        BoxCreationDTO boxDTO = new BoxCreationDTO();
        boxDTO.setNom("Test Box");
        boxDTO.setHidden(false);
        
        List<SoundProbabilityDTO> probabilities = new ArrayList<>();
        probabilities.add(createProbabilityDTO(sound1.getId(), 5.0));
        probabilities.add(createProbabilityDTO(sound2.getId(), 15.0));
        boxDTO.setSoundProbabilities(probabilities);
        
        Box box = boxService.createBoxWithContent(boxDTO);
        
        TirageResultDTO result = boxService.effectuerTirage(box.getId(), user.getId());
        
        assertNotNull(result.getSound());
        assertNotNull(result.getMessage());
        assertTrue(result.getIsNew());
    }
    
    private Sound createTestSound(String nom, SoundRarety rarete) {
        Sound sound = new Sound();
        sound.setNom(nom);
        sound.setType("OST");
        sound.setRarete(rarete);
        sound.setLink("https://storage.com/" + nom.toLowerCase().replace(" ", "-") + ".mp3");
        return soundRepository.save(sound);
    }
    
    private User createTestUser() {
        // Vérifier qu'il n'y a pas d'utilisateur existant
        assertEquals(0, userRepository.count());
        assertEquals(0, usersCollectionRepository.count());
        
        User user = new User();
        user.setPseudo("TestUser");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setSecret_bool(false);
        user.setRole(UserRoles.USER);
        
        // Sauvegarder et flush pour forcer la persistance
        User savedUser = userRepository.saveAndFlush(user);
        
        UsersCollection collection = new UsersCollection();
        collection.setUser(savedUser);
        
        UsersCollection savedCollection = usersCollectionRepository.saveAndFlush(collection);
        
        savedUser.setUsersCollection(savedCollection);
        return userRepository.saveAndFlush(savedUser);
    }
    
    private SoundProbabilityDTO createProbabilityDTO(Integer soundId, Double probability) {
        SoundProbabilityDTO dto = new SoundProbabilityDTO();
        dto.setSoundId(soundId);
        dto.setProbability(probability);
        return dto;
    }
} 